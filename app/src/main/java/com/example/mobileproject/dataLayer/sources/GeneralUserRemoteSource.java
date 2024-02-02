package com.example.mobileproject.dataLayer.sources;

import com.example.mobileproject.models.Users.Users;

import java.util.Map;

public abstract class GeneralUserRemoteSource {
    protected CallbackUsers c;
    public abstract void retrieveUsers(CallbackUsers c);
    public abstract void retrieveUserByDocumentId(String tag, CallbackUsers c);
    public abstract void createUser(Users post, CallbackUsers ci);
    public abstract void editUsername(String tag, String newUsername, CallbackUsers c);
    protected abstract void createDocument(String collectionName, Map<String, Object> documentFields, CallbackInterface ci);
    public abstract Users getLoggedUser();
    public abstract void logout(CallbackUsers c);
    public abstract void signUp(String email, String password, CallbackUsers c);
    public abstract void signIn(String email, String password, CallbackUsers c);
    public abstract void signInWithGoogle(String idToken, CallbackUsers c);
    public abstract void passwordReset(String email, CallbackUsers c);
}
