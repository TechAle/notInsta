package com.example.mobileproject.service;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.utils.Constants;
import com.example.mobileproject.utils.DataStoreSingleton;
import com.example.mobileproject.utils.ServiceLocator;

import java.util.List;

public class SetupWorker extends Worker {

    private final PostRepository pr;
    public SetupWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        pr = ServiceLocator.getInstance().getPostRepo(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        int i = 0;
        while(true){
            List<Post> tmp = pr.retrieveUserPostSynchronously(i); //Chiamata sincrona, vedere la javadoc del metodo
            if(tmp == null){ //Errore
                return Result.failure();
            }
            if(tmp.size() == 0){ //Nulla da sincronizzare
                DataStoreSingleton.getInstance(getApplicationContext()).writeLongData(Constants.LAST_UPDATE, System.currentTimeMillis());
                return Result.success();
            }
            i++;
            pr.loadPostsInLocal(tmp);
        }
    }
}
