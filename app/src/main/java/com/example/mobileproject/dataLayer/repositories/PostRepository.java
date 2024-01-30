package com.example.mobileproject.dataLayer.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.mobileproject.dataLayer.sources.Callback;
import com.example.mobileproject.dataLayer.sources.GeneralPostRemoteSource;
import com.example.mobileproject.models.Post;
import com.example.mobileproject.models.PostResp;
import com.example.mobileproject.utils.Result;

import java.util.List;

public class PostRepository implements Callback {

    private final MutableLiveData<Result> posts;

    private final GeneralPostRemoteSource rem;

    public PostRepository(GeneralPostRemoteSource rem){
        this.rem = rem;
        posts = new MutableLiveData<>();
    }

    //assegnamento in callback
    public MutableLiveData<Result> retrievePosts(){
        rem.retrievePosts(this);
        return posts;
    }

    //assegnamento in callback
    public MutableLiveData<Result> retrievePosts(String tag){
        rem.retrievePostByDocumentId(tag, this);
        return posts;
    }

    @Override
    public void onSuccess(List<Post> res) {
        if (posts.getValue() != null && posts.getValue().successful()) { //Lazy Loading
            List<Post> l = ((Result.PostResponseSuccess)posts.getValue()).getData().getPostList();
            l.addAll(res);
            Result.PostResponseSuccess result = new Result.PostResponseSuccess(new PostResp(l));
            posts.postValue(result);
        } else {
            Result.PostResponseSuccess result = new Result.PostResponseSuccess(new PostResp(res));
            posts.postValue(result);
        }
    }
    @Override
    public void onFailure(Exception e) {
        Result.Error resultError = new Result.Error(e.getMessage());
        posts.postValue(resultError);
    }

    @Override
    public void onUploadSuccess() {
        //TODO: Usare o eliminare (dalla interfaccia Callback)
    }
}
