package com.example.mobileproject.dataLayer.sources;

public interface CallbackInterface {//TODO: ha ancora senso?
    void onSuccess();
    void onFailure(Exception e);
    void onUploadSuccess(String id);
}
