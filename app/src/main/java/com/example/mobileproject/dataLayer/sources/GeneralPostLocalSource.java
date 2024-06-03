package com.example.mobileproject.dataLayer.sources;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.mobileproject.models.Post.Post;

import java.util.List;

public abstract class GeneralPostLocalSource {
    protected CallbackPosts c;
    public void setCallback(CallbackPosts c){
        this.c = c;
    }
    public abstract void retrievePosts(int page);
    public abstract void retrieveNoSyncPosts();
    public abstract void insertPosts(List<Post> l);
    public abstract void insertPost(Post p);

    public abstract void modifyId(String oldId, String newId);

    public abstract void deletePosts();

    public abstract Bitmap getImage(String id);

    public abstract Uri createImage(Bitmap bmp);
}
