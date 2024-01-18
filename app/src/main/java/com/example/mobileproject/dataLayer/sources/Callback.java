package com.example.mobileproject.dataLayer.sources;

public interface Callback {
    void onSuccess(TResult res);
    void onFailure(Exception e);
}
