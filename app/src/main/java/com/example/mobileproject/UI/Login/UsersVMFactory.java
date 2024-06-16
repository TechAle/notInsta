package com.example.mobileproject.UI.Login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobileproject.dataLayer.repositories.PostManager;
import com.example.mobileproject.dataLayer.repositories.UserRepository;

public final class UsersVMFactory implements ViewModelProvider.Factory {
    private final UserRepository ur;
    private final PostManager pr;

    public UsersVMFactory(UserRepository repo, PostManager pr) {
        this.ur = repo;
        this.pr = pr;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new UsersViewModel(ur, pr);
    }
}
