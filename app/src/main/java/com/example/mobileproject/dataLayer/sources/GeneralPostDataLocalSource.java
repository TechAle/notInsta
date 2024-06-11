package com.example.mobileproject.dataLayer.sources;

import android.net.Uri;

import com.example.mobileproject.models.Post.Post;

import java.util.List;
import java.util.concurrent.Future;

public abstract class GeneralPostDataLocalSource {

    protected CallbackPosts c;

    public void setCallback(CallbackPosts c){
        this.c = c;
    }
    public abstract void retrievePosts(int page);
    public abstract Future<List<Post>> retrieveNoSyncPosts();
    public abstract void insertPosts(List<Post> l);
    public abstract void insertPost(Post p);
    public abstract Future<List<String>> retrieveIDsWithNoImage();
    public abstract void modifyId(String oldId, String newId);
    public abstract void updatePost(Post p);
    public abstract void deletePosts();
    public abstract void modifyImage(String id, Uri bmp);
}