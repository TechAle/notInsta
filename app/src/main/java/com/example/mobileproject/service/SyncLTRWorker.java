package com.example.mobileproject.service;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mobileproject.dataLayer.repositories.PostManager;
import com.example.mobileproject.utils.ServiceLocator;

/**
 * Classe per effettuare la sincronizzazione da sorgente locale (LTR -> Local To Remote).
 * Carica un post alla volta
 */
public final class SyncLTRWorker extends Worker{

    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public SyncLTRWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        PostManager pr = ServiceLocator.getInstance().getPostRepo(getApplicationContext());
        if(pr.syncPostsFromLocal()){
            return Result.success();
        } else {
            return Result.failure();
        }
    }
}