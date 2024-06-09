package com.example.mobileproject.UI.Camera;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobileproject.dataLayer.repositories.PostRepository;

public final class ProcessedImageVMFactory implements ViewModelProvider.Factory {
    private final PostRepository pr;
    public ProcessedImageVMFactory(PostRepository pr) {
        this.pr = pr;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ProcessedImageViewModel(pr);
    }
}
