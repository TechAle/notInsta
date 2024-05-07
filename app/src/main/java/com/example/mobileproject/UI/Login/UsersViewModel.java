package com.example.mobileproject.UI.Login;

import android.net.Uri;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.dataLayer.repositories.UserRepository;
import com.example.mobileproject.models.Users.Users;
import com.example.mobileproject.utils.Result;

//TODO: Usata in più parti, ho il sospetto che vada divisa (non era un ViewModel per activity?)
public class UsersViewModel extends ViewModel {
    private final UserRepository repoU;
    private final PostRepository repoP;
    private MutableLiveData<Result> users;
    private MutableLiveData<Result> selectedUsers;
    private boolean authenticationError;
    public UsersViewModel(UserRepository repoU, PostRepository repoP) {
        this.repoU = repoU;
        this.repoP = repoP;
        authenticationError = false;
    }
    //getters & setters
    public MutableLiveData<Result> getUsers(){
        if(users == null){
            users = repoU.retrieveUsers();
        }
        return users;
    }
    public MutableLiveData<Result> getUserById(String tag){
        if(selectedUsers == null){
            selectedUsers = repoU.retrieveUsers(tag);
        }
        return selectedUsers;
    }
    public MutableLiveData<Result> editUsername( String newUsername) {
        return repoU.editUsername( newUsername);
    }
    public MutableLiveData<Result> editPassword(String newPassword) {
        return repoU.editPassword( newPassword);
    }
    public MutableLiveData<Result> createUser(Users toCreate) {
        return repoU.createUser(toCreate);
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
            users = repoU.logout();
        } else {
            repoU.logout();
        }
        return users;
    }
    public void getUser(String email, String password, boolean isUserRegistered) {
        repoU.getUser(email, password, isUserRegistered);
    }
    public boolean isAuthenticationError() {
        return authenticationError;
    }
    public void setAuthenticationError(boolean authenticationError) {
        this.authenticationError = authenticationError;
    }
    private void getUserData(String email, String password, boolean isUserRegistered) {
        users = repoU.getUser(email, password, isUserRegistered);
    }
    private void getUserData(String token) {
        users = repoU.getGoogleUser(token);
    }
    public void sendPasswordReset(String email){
        repoU.passwordReset(email);
    }
    public void signOut() {
        repoU.signOut();
    }
    public void deleteAccount() {
        repoU.deleteAccount();
    }
    public void changeImage(Uri selectedImageUri) {
        repoU.changeImage(selectedImageUri);
    }
}
