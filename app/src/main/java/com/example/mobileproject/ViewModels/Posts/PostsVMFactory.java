package com.example.mobileproject.ViewModels.Posts;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobileproject.dataLayer.repositories.PostRepository;

public class PostsVMFactory implements ViewModelProvider.Factory {
    private final PostRepository pr;

    public PostsVMFactory(PostRepository repo) {
        this.pr = repo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PostsViewModel(pr);
    }
}
