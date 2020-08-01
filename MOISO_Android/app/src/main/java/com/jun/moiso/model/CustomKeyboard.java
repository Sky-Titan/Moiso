package com.jun.moiso.model;

import android.widget.Button;

import java.util.ArrayList;

public class CustomKeyboard {

    private String custom_id;
    private String custom_name;
    private String owner_id;
    private ArrayList<Button> buttons = new ArrayList<>();

    public CustomKeyboard()
    {

    }

    public CustomKeyboard(String custom_id, String custom_name, String owner_id) {
        this.custom_id = custom_id;
        this.custom_name = custom_name;
        this.owner_id = owner_id;
    }

    public String getCustom_id() {
        return custom_id;
    }

    public void setCustom_id(String custom_id) {
        this.custom_id = custom_id;
    }

    public String getCustom_name() {
        return custom_name;
    }

    public void setCustom_name(String custom_name) {
        this.custom_name = custom_name;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public void addButton(Button button)
    {
        buttons.add(button);
    }

    public Button getButton(int index)
    {
        return buttons.get(index);
    }
}
