package com.example.mobileproject.dataLayer.sources;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.mobileproject.models.Post.Post;

import java.io.File;
import java.util.List;
import java.util.concurrent.Future;

public abstract class GeneralPostLocalSource {

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
    public abstract Bitmap getImage(Uri id);
    public abstract Uri createImage(Bitmap bmp, String id);
    public abstract void modifyImage(String id, Uri bmp);
    public abstract void deleteImages();
    public abstract File createEmptyImageFile();
    public abstract File createEmptyImageFile(String id);
    public abstract Uri renameImage(Uri img, String id);
}