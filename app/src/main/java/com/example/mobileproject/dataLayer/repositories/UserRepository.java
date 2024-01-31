package com.example.mobileproject.dataLayer.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.mobileproject.dataLayer.sources.CallbackPosts;
import com.example.mobileproject.dataLayer.sources.CallbackUsers;
import com.example.mobileproject.dataLayer.sources.GeneralPostRemoteSource;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Users.Users;
import com.example.mobileproject.models.Users.UsersResp;
import com.example.mobileproject.utils.Result;

import java.util.List;

public class UserRepository implements CallbackUsers {

    private final MutableLiveData<Result> users;

    private final GeneralPostRemoteSource rem;

    private final MutableLiveData<Result> ready;

    public UserRepository(GeneralPostRemoteSource rem){
        this.rem = rem;
        users = new MutableLiveData<>();
        ready = new MutableLiveData<>();
    }

    //assegnamento in callback
    public MutableLiveData<Result> retrieveUsers(){
        rem.retrieveUsers(this);
        return users;
    }

    //assegnamento in callback
    public MutableLiveData<Result> retrieveUsers(String tag){
        rem.retrieveUserByDocumentId(tag, this);
        return users;
    }

    public MutableLiveData<Result> editUsername(String tag, String newUsername) {
        rem.editUsername(tag, newUsername, this);
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
    public void onUploadSuccess() {
        //TODO: Usare o eliminare (dalla interfaccia Callback)
    }
}
