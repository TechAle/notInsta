package com.example.mobileproject.ViewModels.Users;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.dataLayer.repositories.UserRepository;

public class UsersVMFactory implements ViewModelProvider.Factory {
    private final UserRepository pr;

    public UsersVMFactory(UserRepository repo) {
        this.pr = repo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new UsersViewModel(pr);
    }
}
