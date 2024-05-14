package com.example.mobileproject.dataLayer.sources;
import com.example.mobileproject.models.Post.Post;

import androidx.annotation.NonNull;

import java.util.List;

public class PostLocalSource extends GeneralPostLocalSource{

    private final PostDao d;

    public PostLocalSource(@NonNull PostRoomDatabase db){
        this.d = db.postDao();
    }


    @Override
    public void insertPosts(List<Post> l){
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            d.insertAll(l);
        });
    }

    @Override
    public void insertPost(Post p){
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            d.insertPost(p);
        });
    }
    @Override
    public void retrievePosts() {
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Post> res = d.getUserPosts();
            c.onSuccessO(res);
        });
    }

    @Override
    public void retrieveNoSyncPosts() {
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Post> res = d.getNoSyncPosts();
            c.onSuccessSyncLocal(res);
        });
    }

    @Override
    public void deletePosts() {
        PostRoomDatabase.databaseWriteExecutor.execute(d::deleteAll);
    }
}
