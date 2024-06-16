package com.example.mobileproject.dataLayer.sources;

public abstract class GeneralAdvSource {
    protected CallbackPosts c;
    public void setCallback(CallbackPosts call){
        this.c = call;
    };
    public abstract void getAdvPost();
}


