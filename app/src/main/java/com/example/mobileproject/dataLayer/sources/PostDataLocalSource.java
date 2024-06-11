package com.example.mobileproject.dataLayer.sources;

import android.net.Uri;

import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.utils.Constants;
import com.example.mobileproject.utils.DBConverter;
import com.google.common.util.concurrent.SettableFuture;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.Future;

public final class PostDataLocalSource extends GeneralPostDataLocalSource{

    private final PostDao d;

    public PostDataLocalSource(@NonNull PostRoomDatabase db){
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
            c.onLocalSaveSuccess();
        });
    }
    @Override
    public void retrievePosts(int page) {
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Post> res = d.getUserPosts(page* Constants.ELEMENTS_LAZY_LOADING);
            c.onSuccessO(res);
        });
    }

    @Override
    public Future<List<Post>> retrieveNoSyncPosts() {
        SettableFuture<List<Post>> f = SettableFuture.create();
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Post> res = d.getNoSyncPosts();
            f.set(res);
        });
        return f;
    }
    @Override
    public Future<List<String>> retrieveIDsWithNoImage(){
        SettableFuture<List<String>> f = SettableFuture.create();
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<String> res = d.getIDsWithNoImage();
            f.set(res);
        });
        return f;
    }
    @Override
    public void modifyId(String oldId, String newId){
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
           Post p = d.getPost(oldId);
           d.deletePost(p);
           p.setId(newId);
           d.insertPost(p);
        });
    }
    @Override
    public void modifyImage(String id, Uri img){
        PostRoomDatabase.databaseWriteExecutor.execute(() -> d.updateImage(id, DBConverter.fromUri(img)));
    }

    @Override
    public void updatePost(Post p){
        PostRoomDatabase.databaseWriteExecutor.execute(() -> d.updatePost(p));
    }
    @Override
    public void deletePosts() {
        PostRoomDatabase.databaseWriteExecutor.execute(d::deleteAll);
    }
}