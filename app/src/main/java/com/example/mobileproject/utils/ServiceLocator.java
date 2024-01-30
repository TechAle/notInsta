package com.example.mobileproject.utils;

import android.app.Application;

import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.dataLayer.repositories.UserRepository;
import com.example.mobileproject.dataLayer.sources.FirestoreRemoteSource;

public class ServiceLocator {
    private static volatile ServiceLocator INSTANCE = null;

    private ServiceLocator(){}

    public static ServiceLocator getInstance(){
        if(INSTANCE == null){
            synchronized (ServiceLocator.class){
                if(INSTANCE == null){
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }

    //TODO: vedere la Application
//    public PostRepository getPostRepo(Application a){
    public PostRepository getPostRepo(){
        return new PostRepository(new FirestoreRemoteSource());
    }

    public UserRepository getUserRepo(){
        return new UserRepository(new FirestoreRemoteSource());
    }
}
//TODO: completarlo