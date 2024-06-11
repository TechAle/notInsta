package com.example.mobileproject.dataLayer.sources;

import androidx.lifecycle.MutableLiveData;

import com.example.mobileproject.dataLayer.sources.CallbackPosts;
import com.example.mobileproject.utils.Result;

public abstract class GeneralAdvSource {
    protected CallbackPosts c;
    public void setCallback(CallbackPosts call){
        this.c = call;
    };
    public abstract void getAdvPost();
}


