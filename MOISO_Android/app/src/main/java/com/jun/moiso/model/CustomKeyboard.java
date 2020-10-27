package com.jun.moiso.model;

import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Entity;
import androidx.room.Ignore;
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

    public CustomKeyboard(String custom_name) {
        this.custom_name = custom_name;
    }
    @Ignore
    public static DiffUtil.ItemCallback<CustomKeyboard> DIFF_CALLBACK = new DiffUtil.ItemCallback<CustomKeyboard>() {
        @Override
        public boolean areItemsTheSame(@NonNull CustomKeyboard oldItem, @NonNull CustomKeyboard newItem) {
            return oldItem.custom_id == newItem.custom_id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull CustomKeyboard oldItem, @NonNull CustomKeyboard newItem) {
            return oldItem.custom_id == newItem.custom_id && oldItem.custom_name.equals(newItem.custom_name);
        }
    };

}
