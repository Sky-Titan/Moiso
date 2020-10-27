package com.jun.moiso.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.jun.moiso.model.CustomButton;
import com.jun.moiso.model.CustomKeyboard;

import java.util.List;

@Dao
public interface MyDAO {

    @Query("SELECT * FROM CUSTOMKEYBOARD")
    public LiveData<List<CustomKeyboard>> selectAllKeyboards();

    @Query("SELECT * FROM CUSTOMBUTTON")
    public LiveData<List<CustomButton>> selectAllButtons();

    @Query("SELECT * FROM CUSTOMBUTTON ORDER BY button_id DESC LIMIT 1")
    public CustomButton selectRecentButton();

    @Query("SELECT * FROM CustomKeyboard WHERE custom_id = :custom_id")
    public LiveData<CustomKeyboard> selectCustomKeyboard(int custom_id);

    @Query("SELECT * FROM CUSTOMBUTTON WHERE custom_id = :custom_id")
    public LiveData<List<CustomButton>> selectButtonsIn(int custom_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertCustomKeyboard(CustomKeyboard customKeyboard) throws IllegalStateException;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertCustomButton(CustomButton customButton) throws IllegalStateException;

    @Query("UPDATE CustomButton SET pos_x = :pos_x, pos_y = :pos_y WHERE button_id = :button_id")
    public void updateButtonPosition(int button_id, float pos_x, float pos_y);


    @Query("DELETE FROM CUSTOMKEYBOARD")
    public void deleteAllKeyboards();

    @Query("DELETE FROM CustomButton")
    public void deleteAllButtons();

    @Query("DELETE FROM CUSTOMKEYBOARD WHERE custom_id = :custom_id")
    public void deleteKeyboard(int custom_id);

    @Query("DELETE FROM CustomButton WHERE button_id = :button_id")
    public void deleteButton(int button_id);

}
