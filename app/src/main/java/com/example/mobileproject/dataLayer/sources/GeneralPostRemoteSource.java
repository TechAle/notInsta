package com.example.mobileproject.dataLayer.sources;

import android.content.ContentResolver;
import android.net.Uri;

import androidx.lifecycle.LifecycleOwner;

import com.example.mobileproject.dataLayer.repositories.UserRepository;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Users.Users;

import java.util.Map;

/**
 * Classe astratta per il datasource relativo ai posts
 */
public abstract class GeneralPostRemoteSource {

    protected CallbackPosts c;


    public abstract void retrievePostsSponsor(CallbackPosts c, LifecycleOwner ow);

    public abstract void retrievePostByDocumentId(String tag, CallbackPosts c);

    public abstract void retrievePosts(CallbackPosts c);

    public abstract void retrieveUsers(CallbackUsers c);

    public abstract void retrievePostsWithTags(String[] tags, CallbackPosts c);

    public abstract void retrieveUserByDocumentId(String tag, CallbackUsers c);

    public abstract void editUsername( String newUsername, CallbackUsers c);

    public abstract void editPassword(String newPassword, CallbackUsers c);

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

    public abstract void createImage(Uri imageUri, String document, ContentResolver contentResolver, CallbackInterface postRepository, String id);


    public abstract void signOut();

    public abstract void deleteAccount();

    public abstract void changeImage(Uri selectedImageUri);
    public void setCallback(CallbackPosts call){
        this.c = call;
    };
    public abstract void retrievePostsSponsor();
    public abstract void retrievePostByDocumentId(String tag);
    public abstract void retrievePosts();
    public abstract void retrievePostsWithTags(String[] tags);
    public abstract void createPosts(Post post);
    protected abstract void createDocument(String collectionName, Map<String, Object> documentFields, CallbackInterface ci);
    public abstract void createImage(Uri imageUri, String document, ContentResolver contentResolver, CallbackInterface postRepository, String id);
    public abstract void retrievePostsLL(int page);
    public abstract void retrievePostsSponsor(CallbackPosts c, LifecycleOwner ow);
    public void retrievePostsWithTagsLL(String[] tags, int page) {
    }
}