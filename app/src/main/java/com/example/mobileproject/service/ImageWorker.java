package com.example.mobileproject.service;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.dataLayer.repositories.PostResponseCallback;
import com.example.mobileproject.utils.ServiceLocator;
import com.google.common.util.concurrent.ListenableFuture;

//TODO: vedere se serve
public final class ImageWorker extends ListenableWorker {

    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public ImageWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        return CallbackToFutureAdapter.getFuture(completer -> {
            //TODO: prendere in input il parametro ID
            String s = getInputData().getString("id");
            PostRepository pr = ServiceLocator.getInstance().getPostRepo(getApplicationContext());
            PostResponseCallback c = new PostResponseCallback() {
                @Override
                public void onResponseCreation(com.example.mobileproject.utils.Result r) {
                    if(r.successful()){
                        completer.set(Result.success());
                    } else {
                        completer.set(Result.retry());
                    }
                }
            };
            pr.setCallback(c);
            //TODO: implementare
            pr.syncImage(getInputData().getString("id"));
            return c;
        });
    }
}
