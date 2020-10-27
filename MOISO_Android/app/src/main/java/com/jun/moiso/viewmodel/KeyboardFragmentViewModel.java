package com.jun.moiso.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.jun.moiso.database.MyDAO;
import com.jun.moiso.database.MyDatabase;
import com.jun.moiso.fragment.KeyboardFragment;
import com.jun.moiso.model.CustomButton;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KeyboardFragmentViewModel extends ViewModel {

    private MyDAO dao;
    private ExecutorService executorService;
    private int custom_id = 0;

    public KeyboardFragmentViewModel(@NonNull Application application, int custom_id)
    {
        dao = MyDatabase.getInstance(application).dao();

        this.custom_id = custom_id;

        executorService = Executors.newSingleThreadExecutor();
    }

    public void setCustom_id(int custom_id)
    {
        this.custom_id = custom_id;
    }

    public LiveData<List<CustomButton>> getCustomButtons()
    {
        return dao.selectButtonsIn(custom_id);
    }
}
