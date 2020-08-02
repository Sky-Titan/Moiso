package com.jun.moiso.model;

public class CustomButton {

    private int button_id;
    private int button_key;

    private String button_text;

    private float pos_x, pos_y;
    private int custom_id;

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

    public String getButton_text() {
        return button_text;
    }

    public void setButton_text(String button_text) {
        this.button_text = button_text;
    }

    public int getButton_id() {
        return button_id;
    }

    public void setButton_id(int button_id) {
        this.button_id = button_id;
    }

    public int getButton_key() {
        return button_key;
    }

    public void setButton_key(int key) {
        this.button_key = key;
    }

    public float getPos_x() {
        return pos_x;
    }

    public void setPos_x(float pos_x) {
        this.pos_x = pos_x;
    }

    public float getPos_y() {
        return pos_y;
    }

    public void setPos_y(float pos_y) {
        this.pos_y = pos_y;
    }

    public int getCustom_id() {
        return custom_id;
    }

    public void setCustom_id(int custom_id) {
        this.custom_id = custom_id;
    }
}
