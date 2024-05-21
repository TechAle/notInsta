package com.example.mobileproject.dataLayer.sources;

import android.net.Uri;

import com.example.mobileproject.models.Users.Users;

import java.util.Map;

public abstract class GeneralUserRemoteSource {
    protected CallbackUsers c;

    public void setCallback(CallbackUsers call){
        this.c = call;
    };
    public abstract void retrieveUsers();
    public abstract void retrieveUserByDocumentId(String tag);
    public abstract void createUser(Users post);
    public abstract void editUsername(String newUsername);
    public abstract void editPassword(String newPassword);
    public abstract void changeImage(Uri selectedImageUri);
    //protected abstract void createDocument(String collectionName, Map<String, Object> documentFields, CallbackInterface ci);
    public abstract void getLoggedUser();
    public abstract void logout();
    public abstract void signUp(String email, String password);
    public abstract void signIn(String email, String password);
    public abstract void signInWithGoogle(String idToken);
    public abstract void passwordReset(String email);
    public abstract void deleteAccount();
    public abstract void signOut(); //questa o logout?
}
