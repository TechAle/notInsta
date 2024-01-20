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
    private boolean Loading;
    private boolean firstLoading;
    private MutableLiveData<Result> posts;
    private MutableLiveData<Result> selectedPosts;
    public PostsViewModel(PostRepository repo) {
        this.repo = repo;
        this.page = 1;
        this.totalResults = 0;
        this.firstLoading = true;
    }

    //getters & setters
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
    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public void setCurrentResults(int currentResults) {
        this.currentResults = currentResults;
    }
    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
    public void setLoading(boolean loading) {
        Loading = loading;
    }
    public void setFirstLoading(boolean firstLoading) {
        this.firstLoading = firstLoading;
    }
    public int getCurrentResults() {
        return currentResults;
    }
    public int getTotalResults() {
        return totalResults;
    }
    public boolean isLoading() {
        return Loading;
    }
    public boolean isFirstLoading() {
        return firstLoading;
    }
}
