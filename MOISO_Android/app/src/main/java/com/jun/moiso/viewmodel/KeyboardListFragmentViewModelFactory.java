package com.jun.moiso.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class KeyboardListFragmentViewModelFactory implements ViewModelProvider.Factory{

    private Application application;

    public KeyboardListFragmentViewModelFactory(Application application)
    {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(KeyboardListFragmentViewModel.class))
            return (T)new KeyboardListFragmentViewModel(application);
        return null;
    }
}
