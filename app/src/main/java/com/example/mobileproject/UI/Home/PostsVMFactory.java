package com.example.mobileproject.UI.Home;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.dataLayer.repositories.UserRepository;

public class PostsVMFactory implements ViewModelProvider.Factory {
    private final PostRepository pr;
    private final UserRepository ur;

    public PostsVMFactory(PostRepository pr, UserRepository ur) {
        this.pr = pr;
        this.ur = ur;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PostsViewModel(pr, ur);
    }
}
