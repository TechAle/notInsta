package com.example.mobileproject.dataLayer.sources;

import android.content.Context;

import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.mobileproject.service.ImageWorker;
import com.example.mobileproject.service.SyncLTRWorker;
import com.example.mobileproject.service.SyncRTLWorker;

public final class PostWorkerSource extends GeneralWorkerSource{
    private final WorkManager wm;

    public PostWorkerSource(Context c){
        wm = WorkManager.getInstance(c);
    }

    /**
     * Lancia il worker per la sincronizzazione dei post da remoto
     * */
    @Override
    public void enqueueRemoteRead(){
        Constraints c = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest syncWorkRequest =
                new OneTimeWorkRequest.Builder(SyncRTLWorker.class)
                        .setConstraints(c)
                        .build();
        wm.enqueueUniqueWork("RemoteReader", ExistingWorkPolicy.KEEP, syncWorkRequest);
    }

    /**
     * Lancia il worker per la sincronizzazione dei post da locale
     * */
    @Override
    public void enqueueRemoteWrite(){
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest syncWorkRequest =
                new OneTimeWorkRequest.Builder(SyncLTRWorker.class)
                        .setConstraints(constraints)
                        .build();
        wm.enqueueUniqueWork("LazyWriter", ExistingWorkPolicy.KEEP, syncWorkRequest);
    }

    /**
     * Lancia il worker per la sincronizzazione delle immagini
     * */
    @Override
    public void startFinishingDownload(){
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest syncWorkRequest =
                new OneTimeWorkRequest.Builder(ImageWorker.class)
                        .setConstraints(constraints)
                        .build();
        wm.enqueueUniqueWork("ImageDownloader", ExistingWorkPolicy.KEEP, syncWorkRequest);
    }
}
