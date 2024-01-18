package com.example.mobileproject.dataLayer.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.mobileproject.dataLayer.sources.GeneralPostRemoteSource;
import com.example.mobileproject.utils.Result;

public class PostRepository {

    private final MutableLiveData<Result> posts;

    private final GeneralPostRemoteSource rem;

    public PostRepository(GeneralPostRemoteSource rem){
        this.rem = rem;
        posts = new MutableLiveData<>();
    }

    public MutableLiveData<Result> retrievePosts(){
        rem.retrievePosts();
        return posts; //TODO: controllare qua, non c'è nessun assegnamento
    }
    public MutableLiveData<Result> retrievePosts(String tag){
        rem.retrievePosts(tag);
        return posts; //TODO: controllare qua, non c'è nessun assegnamento
    }
}
