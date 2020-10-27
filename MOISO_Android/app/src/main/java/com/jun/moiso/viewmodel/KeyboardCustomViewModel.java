package com.jun.moiso.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.jun.moiso.database.MyDAO;
import com.jun.moiso.database.MyDatabase;
import com.jun.moiso.model.CustomButton;
import com.jun.moiso.model.CustomKeyboard;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class KeyboardCustomViewModel extends ViewModel {


    private MyDAO dao;

    private int custom_id;
    private ExecutorService executorService;

    public KeyboardCustomViewModel(@NonNull Application application, int custom_id)
    {
        this.custom_id = custom_id;
        dao = MyDatabase.getInstance(application).dao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<CustomButton>> getCustomButtons()
    {
        return dao.selectButtonsIn(custom_id);
    }

    public void updateButtonPos(int button_id, float pos_x, float pos_y)
    {
        executorService.execute(() -> dao.updateButtonPosition(button_id, pos_x, pos_y));
    }

    public void insertButton(int button_key, String button_text, float pos_x, float pos_y)
    {
        executorService.execute(() -> dao.insertCustomButton(new CustomButton(button_key, button_text, pos_x, pos_y, custom_id)));
    }

    public CustomButton getRecentButton()
    {
        return dao.selectRecentButton();
    }

    public void deleteButton(int button_id)
    {
        executorService.execute(() -> dao.deleteButton(button_id));
    }


    public LiveData<CustomKeyboard> getCustomKeyboard() {
        return dao.selectCustomKeyboard(custom_id);
    }
}
