package com.example.mobileproject.service;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.dataLayer.repositories.PostResponseCallback;
import com.example.mobileproject.models.Post.Post;
import static com.example.mobileproject.utils.Constants.ELEMENTS_LAZY_LOADING;
import com.example.mobileproject.utils.ServiceLocator;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

public class SetupWorker extends ListenableWorker {

    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public SetupWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }
    //TODO: strategia quando viene fermato
    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        return CallbackToFutureAdapter.getFuture(completer -> {
            PostRepository pr = ServiceLocator.getInstance().getPostRepo(getApplicationContext());
            PostResponseCallback c = new PostResponseCallback() {
                final PostRepository ipr = pr;
                int successfulCalls = 0;
                boolean endedCalls = false;
                @Override
                public void onResponseOwnedPosts(com.example.mobileproject.utils.Result r) {
                    if (r.successful()){
                        successfulCalls++;
                        List<Post> p = ((com.example.mobileproject.utils.Result.PostResponseSuccess) r).getData().getPostList();
                        if(p.size() < ELEMENTS_LAZY_LOADING) endedCalls = true;
                        ipr.putPosts(p);
                    }
                    else completer.set(Result.failure());
                }
                @Override
                public void onResponseCreation(com.example.mobileproject.utils.Result r){
                    if (r.successful()){
                        if(endedCalls){
                            completer.set(Result.success());
                        } else {
                            ipr.retrievePostsbyAuthor("Boh", successfulCalls);
                        }
                    } else {
                        //TODO: gestione fallimento
                        //probabilmente è programmazione difensiva
                    }
                }
            };
            pr.setCallback(c);
            pr.retrievePostsbyAuthor("Boh", 0);
            return c;
        });
    }
}
