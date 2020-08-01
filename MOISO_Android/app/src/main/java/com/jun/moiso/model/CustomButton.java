package com.jun.moiso.model;

public class CustomButton {

    private int button_id;
    private String key;
    private float pos_x, pos_y;
    private int custom_id;

    public CustomButton() {
    }

    public CustomButton(int button_id, String key, float pos_x, float pos_y, int custom_id) {
        this.button_id = button_id;
        this.key = key;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.custom_id = custom_id;
    }

    public int getButton_id() {
        return button_id;
    }

    public void setButton_id(int button_id) {
        this.button_id = button_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
