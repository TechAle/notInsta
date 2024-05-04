package com.example.mobileproject.dataLayer.sources;

import com.example.mobileproject.models.Post.Post;

import java.util.List;

public abstract class GeneralPostLocalSource {
    protected CallbackPosts c;
    public void setCallback(CallbackPosts c){
        this.c = c;
    }
    public abstract void retrievePosts();
    public abstract void retrieveNoSyncPosts();
    public abstract void insertPosts(List<Post> l);
    public abstract void insertPost(Post p);
    public abstract void deletePosts();
}
