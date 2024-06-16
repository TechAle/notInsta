package com.example.mobileproject.dataLayer.sources;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.mobileproject.models.Users.Users;

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
    public abstract void changeImage(Bitmap selectedImageUri);
    public abstract void getLoggedUser();
    public abstract void logout();
    public abstract void signUp(String email, String password);
    public abstract void signIn(String email, String password);
    public abstract void signInWithGoogle(String idToken);
    public abstract void passwordReset(String email);
    public abstract void deleteAccount();
    public abstract void signOut(); //questa o logout?
    public abstract boolean isLogged();
}
