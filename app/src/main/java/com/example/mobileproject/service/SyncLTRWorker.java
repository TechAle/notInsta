package com.example.mobileproject.service;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.utils.ServiceLocator;

import java.util.List;

/**
 * Classe per effettuare la sincronizzazione da sorgente locale (LTR -> Local To Remote).
 * Carica un post alla volta
 */
public class SyncLTRWorker extends Worker {
    private final PostRepository pr;
    public SyncLTRWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        pr = ServiceLocator.getInstance().getPostRepo(context);
    }
    @NonNull
    @Override
    public Result doWork() {
        List<Post> pl = pr.syncPostsFromLocal();
        for(Post p : pl){
            //TODO: sistemare questa parte
            pr.createPost(p);
            pr.substitutePost(p, new Post());
        }
        return Result.success();
    }
}
