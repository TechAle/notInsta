package com.example.mobileproject.service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.utils.ServiceLocator;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SyncWorker extends Worker{

    private final PostRepository pr;
    private List<Post> tmp = null;
    private CountDownLatch latch;
    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        pr = ServiceLocator.getInstance().getPostRepo(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        int i = 0;
        while(true){
            List<Post> tmp = pr.syncPostsFromRemote(i); //Chiamata sincrona, vedere la javadoc del metodo
            if(tmp == null){ //Errore
                return Result.failure();
            }
            if(tmp.size() == 0){ //Nulla da sincronizzare
                return Result.success();
            }
            i++;
            pr.loadPostsInLocal(tmp);
        }
    }
}
