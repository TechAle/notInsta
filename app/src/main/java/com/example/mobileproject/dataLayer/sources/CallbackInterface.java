package com.example.mobileproject.dataLayer.sources;

public interface CallbackInterface {//TODO: ha ancora senso?
    void onSuccess();
    void onUploadFailure(Exception e);
    void onUploadSuccess(String id);
}
