package com.jun.moiso.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.jun.moiso.model.CustomButton;
import com.jun.moiso.model.CustomKeyboard;


@Database(version = 1, entities = {CustomButton.class, CustomKeyboard.class})
public abstract class MyDatabase extends RoomDatabase {

    public abstract MyDAO dao();

    private static MyDatabase instance;

    private static final String TAG = "MyDatabase";

    public synchronized static MyDatabase getInstance(Context context)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context, MyDatabase.class, "olic.db").build();
        }
        return instance;
    }


}
