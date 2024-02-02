package com.example.mobileproject.utils;

import com.example.mobileproject.models.Product;

import java.util.List;

/**
 * Interface to send data from Repositories to Activity/Fragment.
 */
public interface ResponseCallback {
    void onSuccess(List<Product> newsList, long lastUpdate);
    void onFailure(String errorMessage);
}
