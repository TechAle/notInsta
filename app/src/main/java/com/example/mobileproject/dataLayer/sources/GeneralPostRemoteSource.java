package com.example.mobileproject.dataLayer.sources;

import android.content.ContentResolver;
import android.net.Uri;

import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Users.Users;

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

    public abstract void createPosts(Post post, CallbackPosts ci);

    public abstract void createUser(Users post, CallbackUsers ci);

    public abstract void createDocument(String collectionName, Map<String, Object> documentFields, CallbackInterface ci);

    public abstract Users getLoggedUser();

    public abstract void logout(CallbackUsers c);

    public abstract void signUp(String email, String password, CallbackUsers c);

    public abstract void signIn(String email, String password, CallbackUsers c);

    public abstract void signInWithGoogle(String idToken, CallbackUsers c);

    public abstract void passwordReset(String email, CallbackUsers c);

    //TODO: Qua io non lo sto usando, qualcuno ne faccia qualcosa o cancellatelo
    public abstract void postPost();

    public abstract  void createImage(Uri imageUri, String document, ContentResolver contentResolver, CallbackInterface postRepository, String id);
}
