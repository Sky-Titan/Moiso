package com.jun.moiso.model;


public class KeyButton {


    private String key_name;
    private int key_code;

    public KeyButton() {
    }

    public KeyButton(String key_name, int key_code) {
        this.key_name = key_name;
        this.key_code = key_code;
    }

    public String getKey_name() {
        return key_name;
    }

    public void setKey_name(String key_name) {
        this.key_name = key_name;
    }

    public int getKey_code() {
        return key_code;
    }

    public void setKey_code(int key_code) {
        this.key_code = key_code;
    }
}
