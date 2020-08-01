package com.jun.moiso;

import android.app.Application;

public class MyApplication extends Application {

    //TODO : 현재 로그인 된 USER ID
    private String user_id="test";

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
