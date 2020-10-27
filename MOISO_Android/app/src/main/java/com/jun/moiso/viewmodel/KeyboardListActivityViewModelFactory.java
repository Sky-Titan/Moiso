package com.jun.moiso.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class KeyboardListActivityViewModelFactory implements ViewModelProvider.Factory  {

    private Application application;

    public KeyboardListActivityViewModelFactory(Application application)
    {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(KeyboardListActivityViewModel.class))
            return (T)new KeyboardListActivityViewModel(application);
        return null;
    }
}
