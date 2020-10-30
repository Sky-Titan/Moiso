package com.jun.moiso.model;

import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class CustomKeyboard {

    @PrimaryKey (autoGenerate = true)
    @NonNull
    public int custom_id;
    public String custom_name;

    public CustomKeyboard()
    {

    }

    public CustomKeyboard(int custom_id, String custom_name) {
        this.custom_id = custom_id;
        this.custom_name = custom_name;
    }


}
