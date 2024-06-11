package com.example.mobileproject.dataLayer.repositories;

import com.example.mobileproject.utils.Result;

public interface PostResponseCallback {
    default void onResponseGlobalPost(Result r){};
    default void onResponseFoundPosts(Result r){};
    default void onResponseOwnedPosts(Result r){};
    default void onResponseAdvPost(Result r){};
    default void onResponseCreation(Result r){};
}
