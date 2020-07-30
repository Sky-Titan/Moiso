package com.jun.moiso.model;

public class KeyboardListItem {

    private String custom_name="";

    public KeyboardListItem() {
    }

    public KeyboardListItem(String custom_name) {
        this.custom_name = custom_name;
    }

    public String getCustom_name() {
        return custom_name;
    }

    public void setCustom_name(String custom_name) {
        this.custom_name = custom_name;
    }
}
