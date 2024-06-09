package com.example.mobileproject.service;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.dataLayer.repositories.PostResponseCallback;
import com.example.mobileproject.utils.ServiceLocator;
import com.google.common.util.concurrent.ListenableFuture;

//TODO: modificare
public final class ImageWorker extends Worker {

    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public ImageWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        PostRepository pr = ServiceLocator.getInstance().getPostRepo(getApplicationContext());
        if (pr.syncImages()) return Result.success();
        else return Result.failure();
    }
}
