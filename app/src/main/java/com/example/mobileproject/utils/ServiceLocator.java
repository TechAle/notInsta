package com.example.mobileproject.utils;

import android.app.Application;

import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.dataLayer.repositories.UserRepository;
import com.example.mobileproject.dataLayer.sources.FirestoreRemoteSource;
import com.example.mobileproject.service.StoreAPIService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceLocator {
    private static volatile ServiceLocator INSTANCE = null;

    private ServiceLocator() {
    }

    public static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            synchronized (ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }

    //TODO: vedere la Application
//    public PostRepository getPostRepo(Application a){
    public PostRepository getPostRepo(Application app) {
        return new PostRepository(new FirestoreRemoteSource(app));
    }

    public UserRepository getUserRepo(Application app) {
        return new UserRepository(new FirestoreRemoteSource(app));
    }

    public StoreAPIService getProductsApiService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.NEWS_API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(StoreAPIService.class);
    }

}
