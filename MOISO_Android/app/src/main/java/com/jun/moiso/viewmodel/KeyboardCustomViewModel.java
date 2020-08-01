package com.jun.moiso.viewmodel;

import com.jun.moiso.model.CustomKeyboard;

public class KeyboardCustomViewModel {

    private CustomKeyboard customKeyboard;

    public KeyboardCustomViewModel()
    {

    }

    public KeyboardCustomViewModel(CustomKeyboard customKeyboard) {
        this.customKeyboard = customKeyboard;
    }

    public CustomKeyboard getCustomKeyboard() {
        return customKeyboard;
    }

    public void setCustomKeyboard(CustomKeyboard customKeyboard) {
        this.customKeyboard = customKeyboard;
    }
}
