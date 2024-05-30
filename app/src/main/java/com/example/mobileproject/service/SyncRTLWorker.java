package com.example.mobileproject.service;

import static com.example.mobileproject.utils.Constants.ELEMENTS_LAZY_LOADING;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.dataLayer.repositories.PostResponseCallback;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.utils.Constants;
import com.example.mobileproject.utils.DataStoreSingleton;
import com.example.mobileproject.utils.ServiceLocator;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe per effettuare la sincronizzazione da sorgente remota (RTL -> Remote To Local)
 */
public final class SyncRTLWorker extends ListenableWorker{


    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public SyncRTLWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }
    //TODO: strategia quando viene fermato
    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        return CallbackToFutureAdapter.getFuture(completer -> {
            PostRepository pr = ServiceLocator.getInstance().getPostRepo(getApplicationContext());
            long last = new DataStoreSingleton(getApplicationContext()).readLongData("NotInstaServiceData", "lastUpdate");
            PostResponseCallback c = new PostResponseCallback() {
                final PostRepository ipr = pr;
                int totalCalls = 0, successfulCalls = 0;
                boolean endedCalls = false;
                //int images = 0, totalImages = 0;
                final List<Post> tmpl = new ArrayList<>();
                final long lastUpdate = last;
                @Override
                public void onResponseOwnedPosts(com.example.mobileproject.utils.Result r) {
                    if (r.successful()){
                        tmpl.clear();
                        tmpl.addAll(((com.example.mobileproject.utils.Result.PostResponseSuccess) r).getData().getPostList());
                        if(tmpl.size() < ELEMENTS_LAZY_LOADING*5){//TODO: refactor
                            endedCalls = true;
                            new DataStoreSingleton(getApplicationContext()).writeLongData("NotInstaServiceData","lastUpdate", System.currentTimeMillis());
                            //TODO: migliorare la data
                        }
                        //TODO: scaricare le immagini
                        ipr.putPosts(tmpl);
                    }
                    else completer.set(Result.failure());
                }
                @Override
                public void onResponseCreation(com.example.mobileproject.utils.Result r){
                    totalCalls++;
                    if (r.successful()){
                        successfulCalls++;
                    }
                    if(endedCalls){
                        HashMap<String, Object> m = new HashMap<>();
                        m.put("Total", totalCalls);
                        m.put("Succeeded", successfulCalls);
                        completer.set(Result.success(new Data.Builder().putAll(m).build()));
                    } else {
                        ipr.getNoSyncPostsFromRemote(totalCalls, lastUpdate);
                    }
                }
            };
            pr.setCallback(c);
            pr.getNoSyncPostsFromRemote(0, last);
            return c;
        });
    }
}
