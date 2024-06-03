package com.example.mobileproject.dataLayer.sources;

import com.example.mobileproject.models.Users.Users;

import java.util.List;

/**
 * Interfaccia per le chiamate di ritorno.
 */

//Nel caso dovessero servire altre chiamate di ritorno, inseritele qua
public interface CallbackUsers{
    void onSuccess(List<Users> res);
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