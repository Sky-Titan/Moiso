package com.jun.moiso.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;

import androidx.databinding.ObservableArrayList;

import com.jun.moiso.model.CustomButton;
import com.jun.moiso.model.CustomKeyboard;

import java.util.ArrayList;
import java.util.Observable;

public class KeyboardDB {

    private static KeyboardDB keyboardDB;
    private static SQLiteDatabase db;
    private static MyDBHelper myDBHelper;


    public static KeyboardDB getInstance(Context context)
    {
        if(keyboardDB == null)
            keyboardDB = new KeyboardDB(context);
        return keyboardDB;
    }

    private KeyboardDB(Context context)
    {
        myDBHelper = new MyDBHelper(context, "Keyboard.db", null, 1);
        db = myDBHelper.getWritableDatabase();

        db.execSQL("CREATE TABLE IF NOT EXISTS Custom (custom_id INTEGER PRIMARY KEY AUTOINCREMENT, custom_name TEXT, owner_id TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Button (button_id INTEGER PRIMARY KEY AUTOINCREMENT, button_key TEXT," +
                " pos_x FLOAT, pos_y FLOAT, custom_id INTEGER, FOREIGN KEY (custom_id) REFERENCES Custom(custom_id) )");
    }

    //커스텀 키보드 생성
    public CustomKeyboard insertCustom(String custom_name, String owner_id)
    {
        db.execSQL("INSERT INTO Custom (custom_name, owner_id) VALUES('"+custom_name+"','"+owner_id+"')");
        return selectRecentCustom();
    }

    //버튼 생성
    public CustomButton insertButton(String button_key, float pos_x, float pos_y, int custom_id)
    {
        db.execSQL("INSERT INTO Button (button_key, pos_x, pos_y, custom_id) VALUES('"+button_key+"', "+pos_x+", "+pos_y+", "+custom_id+")");
        return selectRecentButton();

    }

    //커스텀 키보드 삭제
    public void deleteCustom(int custom_id)
    {
        db.execSQL("DELETE FROM Custom WHERE custom_id = "+custom_id+"");
    }

    //버튼 삭제
    public void deleteButton(int button_id)
    {
        db.execSQL("DELETE FROM Button WHERE button_id = "+button_id+"");
    }

    //버튼 위치 수정
    public void updateButtonPos(int button_id, float pos_x, float pos_y)
    {
        db.execSQL("UPDATE Button SET pos_x ="+pos_x+", pos_y="+pos_y+" WHERE button_id = "+button_id+"");
    }

    //가장 최근에 추가된 커스텀키보드 row 불러옴
    public CustomKeyboard selectRecentCustom()
    {
        Cursor cursor = db.rawQuery("SELECT * FROM Custom ORDER BY custom_id DESC LIMIT 1", null);
        CustomKeyboard customKeyboard = new CustomKeyboard();

        while(cursor.moveToNext())
        {
            customKeyboard.setCustom_id(cursor.getInt(0));
            customKeyboard.setCustom_name(cursor.getString(1));
            customKeyboard.setOwner_id(cursor.getString(2));
        }
        return customKeyboard;
    }

    //가장 최근에 추가된 커스텀키보드 row 불러옴
    public CustomButton selectRecentButton()
    {
        Cursor cursor = db.rawQuery("SELECT * FROM Button ORDER BY button_id DESC LIMIT 1", null);
        CustomButton customButton = new CustomButton();

        while(cursor.moveToNext())
        {
            customButton.setButton_id(cursor.getInt(0));
            customButton.setKey(cursor.getString(1));
            customButton.setPos_x(cursor.getFloat(2));
            customButton.setPos_y(cursor.getFloat(3));
            customButton.setCustom_id(cursor.getInt(4));

        }
        return customButton;
    }

    //커스텀 키보드 불러오기
    public ObservableArrayList<CustomKeyboard> selectCustomOf(String owner_id)
    {
        ObservableArrayList<CustomKeyboard> customKeyboards = new ObservableArrayList<>();

        Cursor cursor = db.rawQuery("SELECT custom_id, custom_name FROM Custom WHERE owner_id = '"+owner_id+"'", null);

        while(cursor.moveToNext())
        {
            int custom_id = cursor.getInt(0);
            String custom_name = cursor.getString(1);

            CustomKeyboard customKeyboard = new CustomKeyboard(custom_id,custom_name, owner_id);
            customKeyboards.add(customKeyboard);
        }
        cursor.close();
        return customKeyboards;
    }

    //커스텀 id의 버튼들 불러오기
    public ObservableArrayList<CustomButton> selectButtonsOf(int custom_id){
        ObservableArrayList<CustomButton> buttons = new ObservableArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM Button WHERE custom_id = "+custom_id+"", null);

        while(cursor.moveToNext())
        {
            int button_id = cursor.getInt(0);
            String button_key = cursor.getString(1);
            float pos_x = cursor.getFloat(2);
            float pos_y = cursor.getFloat(3);

            CustomButton button = new CustomButton(button_id, button_key, pos_x, pos_y, custom_id);
            buttons.add(button);
        }
        cursor.close();
        return buttons;
    }

}
