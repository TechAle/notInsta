package com.example.mobileproject.dataLayer.sources;

public abstract class GeneralPostLocalSource {
    protected CallbackPosts c;
    public void setCallback(CallbackPosts c){
        this.c = c;
    }
    public abstract void retrievePosts();
    public abstract void retrieveNoSyncPosts();
    public abstract void createPost();
    public abstract void deletePosts();
}
