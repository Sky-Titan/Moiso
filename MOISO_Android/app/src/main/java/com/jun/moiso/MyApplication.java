package com.jun.moiso;

import android.app.Application;

import com.jun.moiso.model.KeyButton;

import java.util.ArrayList;

public class MyApplication extends Application {

    //TODO : 현재 로그인 된 USER ID
    private String user_id="test";
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<KeyButton> keyButtons = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        settingList();
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public ArrayList<KeyButton> getKeyButtons() {
        return keyButtons;
    }

    public void setKeyButtons(ArrayList<KeyButton> keyButtons) {
        this.keyButtons = keyButtons;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    private void settingList()
    {
        list.add("ESC");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 27));

        //F1 ~ F12
        for(int i = 1; i<=12;i++)
        {
            list.add("F"+i);
            keyButtons.add(new KeyButton(list.get(list.size()-1), i+111));
        }

        //숫자 1~9
        for(int i = 0; i<=9;i++)
        {
            list.add("NUMBER"+i);
            keyButtons.add(new KeyButton(list.get(list.size()-1), i+48));
        }

        list.add("Print Screen");
        keyButtons.add(new KeyButton(list.get(list.size()-1),154));

        list.add("Scroll Lock");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 145));

        list.add("Pause");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 19));

        list.add("Insert");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 155));

        list.add("Home");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 36));

        list.add("Page Up");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 33));

        list.add("Page Down");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 34));

        list.add("Delete");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 127));

        list.add("End");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 35));

        list.add("NumLock");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 144));

        list.add("/");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 47));

        list.add("*");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 106));

        list.add("-");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 109));

        list.add("+");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 107));

        //숫자패드 1~9
        for(int i = 0; i<=9;i++)
        {
            list.add("NUMBER_PAD"+i);
            keyButtons.add(new KeyButton("NUMBER_PAD"+i, 96));
        }

        list.add("Alt");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 18));

        list.add("Ctrl");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 17));

        list.add("Shift");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 16));

        list.add("Enter");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 10));

        list.add("Space");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 32));

        list.add("Back Space");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 8));

        list.add("Tab");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 9));

        list.add("Caps Lock");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 20));

        list.add("`");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 192));

        list.add(";");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 59));

        list.add("'");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 222));

        list.add("[");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 91));

        list.add("]");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 93));

        list.add(",");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 44));

        list.add(".");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 46));

        list.add("=");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 61));

        list.add("\\");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 92));

        list.add("Windows");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 524));

        list.add("UP");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 38));

        list.add("DOWN");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 40));

        list.add("LEFT");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 37));

        list.add("RIGHT");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 39));


        list.add("한영");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 21));

        list.add("한자");
        keyButtons.add(new KeyButton(list.get(list.size()-1), 25));

        //알파벳
        for(int i=65;i<=90;i++)
        {
            list.add(""+(char)i);
            keyButtons.add(new KeyButton(""+(char)i, i));
        }
    }
}
