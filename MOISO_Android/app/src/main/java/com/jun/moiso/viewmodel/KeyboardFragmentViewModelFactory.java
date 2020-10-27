package com.jun.moiso.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.jun.moiso.fragment.KeyboardFragment;

public class KeyboardFragmentViewModelFactory implements ViewModelProvider.Factory {

    private Application application;
    private int custom_id;

    public KeyboardFragmentViewModelFactory(Application application, int custom_id)
    {
        this.application = application;
        this.custom_id = custom_id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(KeyboardFragmentViewModel.class))
            return (T)new KeyboardFragmentViewModel(application, custom_id);
        return null;
    }
}
