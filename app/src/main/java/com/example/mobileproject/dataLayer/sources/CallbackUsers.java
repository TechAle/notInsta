package com.example.mobileproject.dataLayer.sources;

import androidx.lifecycle.MutableLiveData;

import com.example.mobileproject.models.Users.Users;
import com.example.mobileproject.utils.Result;

import java.util.List;

/**
 * Interfaccia per le chiamate di ritorno.
 */

//Nel caso dovessero servire altre chiamate di ritorno, inseritele qua
public interface CallbackUsers{
    void onSuccess(List<Users> res);

    //TODO: Controllare qua, non tutte sono chiamate di ritorno
    MutableLiveData<Result> getUser(String email, String password, boolean isUserRegistered);
    MutableLiveData<Result> getGoogleUser(String idToken);
    MutableLiveData<Result> logout();
    void signUp(String email, String password);
    void signIn(String email, String password);
    void signInWithGoogle(String token);


    void onSuccessFromAuthentication(Users user);
    void onFailureFromAuthentication(String message);
    void onSuccessFromRemoteDatabase(Users user);
    void onFailureFromRemoteDatabase(String message);

    void onSuccessFromRemoteDatabase2(Users user);

    void onFailureFromRemoteDatabase2(String message);

    void onSuccessLogout();

    void passwordReset(String email);
    void onSuccess();
    void onUploadFailure(Exception e);
    void onUploadSuccess(String id);
}
