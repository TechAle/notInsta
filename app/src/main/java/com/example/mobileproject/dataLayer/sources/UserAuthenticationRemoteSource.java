package com.example.mobileproject.dataLayer.sources;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserAuthenticationRemoteSource {
    private final FirebaseAuth auth;
    public UserAuthenticationRemoteSource(){
        auth = FirebaseAuth.getInstance();
    }
    public FirebaseUser getCurrentUser(){
        return auth.getCurrentUser();
    }
}
