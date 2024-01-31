package com.example.mobileproject.dataLayer.sources;

import android.content.ContentResolver;
import android.net.Uri;

import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.models.Post.Post;

import java.util.Map;

public abstract class GeneralPostRemoteSource {

    protected CallbackPosts c;


    public abstract void retrievePostsSponsor(CallbackPosts c);

    public abstract void retrievePostByDocumentId(String tag, CallbackPosts c);
    public abstract void retrievePosts(CallbackPosts c);


    public abstract void retrieveUsers(CallbackUsers c);

    public abstract void retrievePostsWithTags(String[] tags, CallbackPosts c);

    public abstract void retrieveUserByDocumentId(String tag, CallbackUsers c);
    public abstract void editUsername(String tag, String newUsername, CallbackUsers c);

    public abstract void createDocument(String collectionName, Post post, CallbackPosts ci);

    public abstract void createDocument(String collectionName, Map<String, Object> documentFields, CallbackPosts ci);


    //TODO: Qua io non lo sto usando, qualcuno ne faccia qualcosa o cancellatelo
    public abstract void postPost();

    public abstract  void createImage(Uri imageUri, ContentResolver contentResolver, CallbackPosts postRepository);
}
