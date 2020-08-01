package com.jun.moiso.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;

import com.jun.moiso.model.CustomButton;
import com.jun.moiso.model.CustomKeyboard;

import java.util.ArrayList;

public class KeyboardDB {

    private static KeyboardDB keyboardDB;
    private static SQLiteDatabase db;
    private static MyDBHelper myDBHelper;


    public KeyboardDB getInstance(Context context)
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
                " pos_x FLOAT, pos_y FLOAT, custom_id TEXT, FOREIGN KEY (custom_id) REFERENCES Custom(custom_id) )");
    }

    //커스텀 키보드 생성
    public void insertCustom(String custom_name, String owner_id)
    {
        db.execSQL("INSERT INTO Custom (custom_name, owner_id) VALUES('"+custom_name+"','"+owner_id+"')");
    }

    //버튼 생성
    public void insertButton(String button_key, float pos_x, float pos_y, String custom_id)
    {
        db.execSQL("INSERT INTO Button (button_key, pos_x, pos_y, custom_id) VALUES('"+button_key+"', "+pos_x+", "+pos_y+", '"+custom_id+"')");
    }

    //커스텀 키보드 삭제
    public void deleteCustom(String custom_id)
    {
        db.execSQL("DELETE FROM Custom WHERE custom_id = "+custom_id+"");
    }

    //버튼 삭제
    public void deleteButton(String button_id)
    {
        db.execSQL("DELETE FROM Button WHERE button_id = "+button_id+"");
    }

    //버튼 위치 수정
    public void updateButtonPos(String button_id, float pos_x, float pos_y)
    {
        db.execSQL("UPDATE Button SET pos_x ="+pos_x+", pos_y="+pos_y+" WHERE button_id = "+button_id+"");
    }

    //커스텀 키보드 불러오기
    public ArrayList<CustomKeyboard> selectCustomOf(String owner_id)
    {
        ArrayList<CustomKeyboard> customKeyboards = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT custom_id, custom_name FROM Custom WHERE owner_id = '"+owner_id+"'", null);

        while(cursor.moveToNext())
        {
            String custom_id = cursor.getString(0);
            String custom_name = cursor.getString(1);

            CustomKeyboard customKeyboard = new CustomKeyboard(custom_id,owner_id,custom_name);
            customKeyboards.add(customKeyboard);
        }
        cursor.close();
        return customKeyboards;
    }

    //커스텀 id의 버튼들 불러오기
    public ArrayList<CustomButton> selectButtonsOf(String custom_id){
        ArrayList<CustomButton> buttons = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM Button WHERE custom_id = "+custom_id+"", null);

        while(cursor.moveToNext())
        {
            String button_id = cursor.getString(0);
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
