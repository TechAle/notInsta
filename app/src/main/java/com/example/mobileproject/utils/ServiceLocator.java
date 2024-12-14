package com.example.mobileproject.utils;

import android.content.Context;

import com.example.mobileproject.dataLayer.repositories.PostDataRepository;
import com.example.mobileproject.dataLayer.repositories.PostImageRepository;
import com.example.mobileproject.dataLayer.repositories.PostManager;
import com.example.mobileproject.dataLayer.repositories.UserRepository;
import com.example.mobileproject.dataLayer.sources.AdvertisementSource;
import com.example.mobileproject.dataLayer.sources.PostDataRemoteSource;
import com.example.mobileproject.dataLayer.sources.FirestoreUserRemoteSource;
import com.example.mobileproject.dataLayer.sources.PostImageLocalSource;
import com.example.mobileproject.dataLayer.sources.PostImageRemoteSource;
import com.example.mobileproject.dataLayer.sources.PostRoomDatabase;
import com.example.mobileproject.dataLayer.sources.PostDataLocalSource;
import com.example.mobileproject.dataLayer.sources.PostWorkerSource;
import com.example.mobileproject.service.StoreAPIService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ServiceLocator {
    private static volatile ServiceLocator INSTANCE = null;
    private ServiceLocator(){}

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

    public PostRoomDatabase getPostDao(Context c){
        return PostRoomDatabase.getInstance(c);
    }


    public PostManager getPostRepo(Context c){
        return new PostManager(
                new PostDataRepository(
                        new PostDataRemoteSource(),
                        new PostDataLocalSource(getPostDao(c))
                ),
                new PostImageRepository(
                        new PostImageRemoteSource(),
                        new PostImageLocalSource(c)
                ),
                new PostWorkerSource(c),
                new AdvertisementSource()

        );
    }

    public UserRepository getUserRepo(){
        return new UserRepository(new FirestoreUserRemoteSource());
    }

    public StoreAPIService getProductsApiService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.NEWS_API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(StoreAPIService.class);
    }

}
