package com.example.mobileproject.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.utils.Result;

public class PostsViewModel extends ViewModel {
    private final PostRepository repo;
    private int page;
    private int currentResults;
    private int totalResults;
    private boolean isLoading;
    private boolean firstLoading;
    private MutableLiveData<Result> posts;
    private MutableLiveData<Result> selectedPosts;
    public PostsViewModel(PostRepository repo) {
        this.repo = repo;
        this.page = 1;
        this.totalResults = 0;
        this.firstLoading = true;
    }

    public MutableLiveData<Result> getPosts(){
        if(posts == null){
            posts = repo.retrievePosts();
        }
        return posts;
    }
    public MutableLiveData<Result> getSelectedPosts(String tag){
        if(selectedPosts == null){
            posts = repo.retrievePosts(tag);
        }
        return selectedPosts;
    }
}
