package com.jun.moiso.model;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class CustomButton {

    @PrimaryKey(autoGenerate = true)
    public int button_id;

    public int button_key;

    public String button_text;

    public float pos_x, pos_y;

    @ForeignKey(entity = CustomKeyboard.class, parentColumns = "custom_id", childColumns = "custom_id", onDelete = ForeignKey.CASCADE)
    public int custom_id;

    public CustomButton() {
    }

    public CustomButton(int button_id, int button_key, String button_text , float pos_x, float pos_y, int custom_id) {
        this.button_id = button_id;
        this.button_key = button_key;
        this.button_text = button_text;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.custom_id = custom_id;
    }

    public CustomButton(int button_key, String button_text , float pos_x, float pos_y, int custom_id) {
        this.button_key = button_key;
        this.button_text = button_text;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.custom_id = custom_id;
    }

    @Ignore
    public static DiffUtil.ItemCallback<CustomButton> DIFF_CALLBACK = new DiffUtil.ItemCallback<CustomButton>() {
        @Override
        public boolean areItemsTheSame(@NonNull CustomButton oldItem, @NonNull CustomButton newItem) {

            return oldItem.button_id == newItem.button_id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull CustomButton oldItem, @NonNull CustomButton newItem) {
            return oldItem.button_id == newItem.button_id && oldItem.button_key == newItem.button_key
                    && oldItem.button_text.equals(newItem.button_text) && oldItem.pos_x == newItem.pos_x && oldItem.pos_y == newItem.pos_y;
        }
    };
}
