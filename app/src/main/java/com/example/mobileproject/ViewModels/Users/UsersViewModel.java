package com.example.mobileproject.ViewModels.Users;

import androidx.lifecycle.LiveData;
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
    public UsersViewModel(UserRepository repo) {
        this.repo = repo;
        this.page = 1;
        this.totalResults = 0;
        this.firstLoading = true;
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
}
