package com.example.mobileproject.dataLayer.sources;

import android.content.ContentResolver;
import android.net.Uri;

import com.example.mobileproject.models.Post.Post;

import java.util.List;

/**
 * Classe astratta per il datasource relativo ai posts
 */
public abstract class GeneralPostRemoteSource {

    protected CallbackPosts c;

    public void setCallback(CallbackPosts call){
        this.c = call;
    };
    public abstract void retrievePostsSponsor();

    /*    public abstract void retrievePostByDocumentId(String tag);
        public abstract void retrievePosts();
        public abstract void retrievePostsWithTags(String[] tags);*/
    public abstract void retrievePostsByAuthor(String idUser, int page); //Lazy loading
    public abstract void createPost(Post post);
    public abstract void createPosts(List<Post> p);
//    protected abstract void createDocument(String collectionName, Map<String, Object> documentFields, CallbackInterface ci);
    public abstract void createImage(Uri imageUri, String document, ContentResolver contentResolver, CallbackInterface postRepository, String id);
    public abstract void retrievePostsLL(int page);
    public abstract void retrievePostsWithTagsLL(String[] tags, int page);
/*    public abstract void retrievePostsForSync(Date d);*/
    public abstract void retrievePostsForSync(int page);
}