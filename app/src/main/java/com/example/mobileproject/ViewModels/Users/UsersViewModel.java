package com.example.mobileproject.ViewModels.Users;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataKt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobileproject.dataLayer.repositories.UserRepository;
import com.example.mobileproject.models.Users.Users;
import com.example.mobileproject.utils.Result;

public class UsersViewModel extends ViewModel {
    private final UserRepository repo;
    private int page;
    private int currentResults;
    private int totalResults;
    private boolean Loading;
    private boolean firstLoading;
    private MutableLiveData<Result> users;
    private MutableLiveData<Result> selectedUsers;
    private boolean authenticationError;

    public UsersViewModel(UserRepository repo) {
        this.repo = repo;
        this.page = 1;
        this.totalResults = 0;
        this.firstLoading = true;
        authenticationError = false;
    }

    //getters & setters
    public MutableLiveData<Result> getUsers(){
        if(users == null){
            users = repo.retrieveUsers();
        }
        return users;
    }

    public MutableLiveData<Result> getUserById(String tag){
        if(selectedUsers == null){
            selectedUsers = repo.retrieveUsers(tag);
        }
        return selectedUsers;
    }

    public MutableLiveData<Result> editUsername(String tag, String newUsername) {
        return repo.editUsername(tag, newUsername);
    }

    public MutableLiveData<Result> editPassword(String newPassword) {
        return repo.editPassword( newPassword);
    }

    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public void setCurrentResults(int currentResults) {
        this.currentResults = currentResults;
    }
    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
    public void setLoading(boolean loading) {
        Loading = loading;
    }
    public void setFirstLoading(boolean firstLoading) {
        this.firstLoading = firstLoading;
    }
    public int getCurrentResults() {
        return currentResults;
    }
    public int getTotalResults() {
        return totalResults;
    }
    public boolean isLoading() {
        return Loading;
    }
    public boolean isFirstLoading() {
        return firstLoading;
    }

    public MutableLiveData<Result> createUser(Users toCreate) {
        return repo.createUser(toCreate);
    }



    public MutableLiveData<Result> getUserMutableLiveData(
            String email, String password, boolean isUserRegistered) {
        if (users == null) {
            getUserData(email, password, isUserRegistered);
        }
        return users;
    }

    public MutableLiveData<Result> getGoogleUserMutableLiveData(String token) {
        if (users == null) {
            getUserData(token);
        }
        return users;
    }

    public MutableLiveData<Result> logout() {
        if (users == null) {
            users = repo.logout();
        } else {
            repo.logout();
        }

        return users;
    }

    public void getUser(String email, String password, boolean isUserRegistered) {
        repo.getUser(email, password, isUserRegistered);
    }

    public boolean isAuthenticationError() {
        return authenticationError;
    }

    public void setAuthenticationError(boolean authenticationError) {
        this.authenticationError = authenticationError;
    }

    private void getUserData(String email, String password, boolean isUserRegistered) {
        users = repo.getUser(email, password, isUserRegistered);
    }

    private void getUserData(String token) {
        users = repo.getGoogleUser(token);
    }

    public void sendPasswordReset(String email){
        repo.passwordReset(email);
    }


    public void signOut() {
        repo.signOut();
    }

    public void deleteAccount() {
        repo.deleteAccount();
    }


    public void changeImage(Uri selectedImageUri) {
        repo.changeImage(selectedImageUri);
    }
}
