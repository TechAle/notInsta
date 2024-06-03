package com.example.mobileproject.service;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.dataLayer.repositories.PostResponseCallback;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.utils.ServiceLocator;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe per effettuare la sincronizzazione da sorgente locale (LTR -> Local To Remote).
 * Carica un post alla volta
 */
public class SyncLTRWorker extends ListenableWorker{

    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public SyncLTRWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }
    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        return CallbackToFutureAdapter.getFuture(completer -> {
            PostRepository pr = ServiceLocator.getInstance().getPostRepo(getApplicationContext());
            PostResponseCallback c = new PostResponseCallback() {
                final PostRepository ipr = pr;
                final List<Post> pl = new LinkedList<>();
                int totalCalls = 0;
                int endedCalls = 0;
                int successfulCalls = 0;
                @Override
                public void onResponseOwnedPosts(com.example.mobileproject.utils.Result r) {
                    if (r.successful()){
                        pl.addAll(((com.example.mobileproject.utils.Result.PostResponseSuccess) r).getData().getPostList());
                        totalCalls = pl.size();
                        if(totalCalls == 0){
                            completer.set(Result.success());
                        }
                        ipr.loadPost(pl.get(0));
                    }
                    else completer.set(Result.failure());
                }
                @Override
                public void onResponseCreation(com.example.mobileproject.utils.Result r){
                    Post p = pl.remove(0);
                    if (r.successful()){
                        Post p2 = new Post(p);
                        p2.setId(((com.example.mobileproject.utils.Result.UserCreationSuccess)r).getData());
                        ipr.substitutePost(p, p2);
                    } else {
                        if(endedCalls == totalCalls){
                            HashMap<String, Object> m = new HashMap<>();
                            m.put("Total", totalCalls);
                            m.put("Succeeded", successfulCalls);
                            completer.set(Result.success(new Data.Builder().putAll(m).build()));
                        } else {
                            endedCalls++;
                            ipr.loadPost(pl.get(0));
                        }
                    }
                }
                @Override
                public void onResponseAdvPost(com.example.mobileproject.utils.Result r){
                    if(r.successful()){
                        successfulCalls++;
                    }
                    if(endedCalls == totalCalls){
                        HashMap<String, Object> m = new HashMap<>();
                        m.put("Total", totalCalls);
                        m.put("Succeeded", successfulCalls);
                        completer.set(Result.success(new Data.Builder().putAll(m).build()));
                    } else {
                        endedCalls++;
                        ipr.loadPost(pl.get(0));
                    }
                }
            };
            //TODO: strategia di cancellazione (se serve)
            //completer.addCancellationListener(???, ???);
            pr.setCallback(c);
            pr.getNoSyncPostsFromLocal();
            return c;
        });
    }
}
