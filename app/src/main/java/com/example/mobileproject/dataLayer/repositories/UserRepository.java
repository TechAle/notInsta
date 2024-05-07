package com.example.mobileproject.dataLayer.repositories;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mobileproject.dataLayer.sources.CallbackUsers;
import com.example.mobileproject.dataLayer.sources.GeneralUserRemoteSource;
import com.example.mobileproject.models.Users.Users;
import com.example.mobileproject.models.Users.UsersResp;
import com.example.mobileproject.utils.Result;

import java.util.List;

public class UserRepository implements CallbackUsers {

    private final MutableLiveData<Result> users;

    private final GeneralUserRemoteSource rem;
//    private final GeneralUserLocalSource local;

    private final MutableLiveData<Result> ready;

    public UserRepository(GeneralUserRemoteSource rem){
        this.rem = rem;
        this.rem.setCallback(this);
        users = new MutableLiveData<>();
        ready = new MutableLiveData<>();
    }

    //assegnamento in callback
    public MutableLiveData<Result> retrieveUsers() {
        rem.retrieveUsers();
        return users;
    }

    //assegnamento in callback
    public MutableLiveData<Result> retrieveUsers(String tag) {
        rem.retrieveUserByDocumentId(tag);
        return users;
    }

    public MutableLiveData<Result> editUsername(String newUsername) {
        rem.editUsername(newUsername);
        return ready;
    }

    public MutableLiveData<Result> editPassword(String newPassword) {
        rem.editPassword(newPassword);
        return ready;
    }


    @Override
    public void onSuccess() {
        ready.postValue(new Result.UserEditSuccess());
    }


    @Override
    public void onSuccess(List<Users> res) {

        if (users.getValue() != null && users.getValue().successful()) { //Lazy Loading
            List<Users> l = ((Result.UserResponseSuccess) users.getValue()).getData().getUsersList();
            l.addAll(res);
            Result.UserResponseSuccess result = new Result.UserResponseSuccess(new UsersResp(l));
            users.postValue(result);
        } else {
            Result.UserResponseSuccess result = new Result.UserResponseSuccess(new UsersResp(res));
            users.postValue(result);
        }
    }

    @Override
    public void onFailure(Exception e) {
        Result.Error resultError = new Result.Error(e.getMessage());
        users.postValue(resultError);
    }

    @Override
    public void onUploadSuccess(String id) {
        Result.UserCreationSuccess result = new Result.UserCreationSuccess(id);
        ready.postValue(result);
    }


    public MutableLiveData<Result> createUser(Users toCreate) {
        rem.createUser(toCreate);
        return ready;
    }

    @Override
    public MutableLiveData<Result> getUser(String email, String password, boolean isUserRegistered) {
        if (isUserRegistered) {
            signIn(email, password);
        } else {
            signUp(email, password);
        }
        return users;
    }

    @Override
    public MutableLiveData<Result> getGoogleUser(String idToken) {
        signInWithGoogle(idToken);
        return users;
    }

    @Override
    public Users getLoggedUser() {
        return rem.getLoggedUser();
    }

    @Override
    public MutableLiveData<Result> logout() {
        rem.logout();
        return users;
    }

    @Override
    public void signUp(String email, String password) {
        rem.signUp(email, password);
    }

    @Override
    public void signIn(String email, String password) {
        rem.signIn(email, password);
    }

    @Override
    public void signInWithGoogle(String token) {
        rem.signInWithGoogle(token);
    }


    @Override
    public void onSuccessFromAuthentication(Users user) {
        if (user != null) {
            createUser(user);
        }
    }

    @Override
    public void onFailureFromAuthentication(String message) {
        Result.Error result = new Result.Error(message);
        users.postValue(result);
    }

    @Override
    public void onSuccessFromRemoteDatabase(Users user) {
        Result.UserResponseSuccessUser result = new Result.UserResponseSuccessUser(user);
        users.postValue(result);
    }

    @Override
    public void onFailureFromRemoteDatabase(String message) {
        Result.Error result = new Result.Error(message);
        users.postValue(result);
    }

    @Override
    public void onSuccessLogout() {

    }

    public void signOut() {
        rem.signOut();
    }

    public void deleteAccount() {
        rem.deleteAccount();
    }

    public void changeImage(Uri selectedImageUri) {
        rem.changeImage(selectedImageUri);
    }

    @Override
    public void passwordReset(String email) {
        rem.passwordReset(email);
    }
/*
    public LiveData<Users> getCurrentUser(){
        local.retrieveCurrentUser();
        return User;
    }

    public void onLocalSuccess(){
        this.
    }*/
}
