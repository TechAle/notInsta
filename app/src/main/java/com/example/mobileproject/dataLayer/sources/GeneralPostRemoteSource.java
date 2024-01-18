package com.example.mobileproject.dataLayer.sources;

import androidx.lifecycle.MutableLiveData;

import com.example.mobileproject.utils.Result;

public abstract class GeneralPostRemoteSource {

    protected Callback c;
    public abstract MutableLiveData<Result> retrievePosts(String tag);
    public abstract MutableLiveData<Result> retrievePosts();

    //Qua io non lo sto usando, qualcuno ne faccia qualcosa o cancellatelo
    public abstract void postPost();
}
