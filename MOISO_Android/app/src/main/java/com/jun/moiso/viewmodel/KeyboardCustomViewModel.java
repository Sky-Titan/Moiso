package com.jun.moiso.viewmodel;

import androidx.lifecycle.ViewModel;

import com.jun.moiso.model.CustomKeyboard;

public class KeyboardCustomViewModel extends ViewModel {

    private CustomKeyboard customKeyboard;



    public CustomKeyboard getCustomKeyboard() {
        return customKeyboard;
    }

    public void setCustomKeyboard(CustomKeyboard customKeyboard) {
        this.customKeyboard = customKeyboard;
    }
}
