package com.jun.moiso.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.jun.moiso.database.MyDAO;
import com.jun.moiso.database.MyDatabase;
import com.jun.moiso.model.CustomKeyboard;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KeyboardListActivityViewModel extends ViewModel {

    private MyDAO dao;

    private ExecutorService executorService;

    public KeyboardListActivityViewModel(@NonNull Application application)
    {
        dao = MyDatabase.getInstance(application).dao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void deleteCustomKeyboard(int custom_id)
    {
        executorService.execute(() -> dao.deleteKeyboard(custom_id));
    }

    public LiveData<List<CustomKeyboard>> getKeyboards()
    {
        return dao.selectAllKeyboards();
    }

    public void addKeyboard(String custom_name)
    {
        executorService.execute(() -> dao.insertCustomKeyboard(new CustomKeyboard(custom_name)));
    }
}
