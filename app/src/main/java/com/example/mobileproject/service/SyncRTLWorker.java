package com.example.mobileproject.service;

import static com.example.mobileproject.utils.Constants.LAST_UPDATE_FIELD;
import static com.example.mobileproject.utils.Constants.SHARED_PREFERENCES_FILENAME;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mobileproject.dataLayer.repositories.PostManager;
import com.example.mobileproject.utils.DataStoreSingleton;
import com.example.mobileproject.utils.ServiceLocator;

import java.util.concurrent.ExecutionException;

/**
 * Classe per effettuare la sincronizzazione da sorgente remota (RTL -> Remote To Local)
 */
public final class SyncRTLWorker extends Worker {

    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public SyncRTLWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        PostManager pr = ServiceLocator.getInstance().getPostRepo(getApplicationContext());
        DataStoreSingleton sp = new DataStoreSingleton(getApplicationContext());
        //Note: if 0 then it's the first sync
        long last = sp.readLongData(SHARED_PREFERENCES_FILENAME, LAST_UPDATE_FIELD);
        try {
            sp.writeLongData(SHARED_PREFERENCES_FILENAME, LAST_UPDATE_FIELD, pr.syncPostsFromRemote(last));//TODO: come faccio a capire se ha finito??
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Result.success();
    }
}