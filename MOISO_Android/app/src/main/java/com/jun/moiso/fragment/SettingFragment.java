package com.jun.moiso.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.jun.moiso.R;
import com.jun.moiso.activity.KeyboardListActivity;


public class SettingFragment extends Fragment {

    private View v;

    private NumberPicker mouse_sensitivity, mouse_wheel_sensitivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_setting, container, false);

        setNumberPickers();

        return v;
    }

    //마우스감도, 휠 감도 numberpicker 설정
    public void setNumberPickers()
    {
        mouse_sensitivity = (NumberPicker) v.findViewById(R.id.mouse_sensitivity_setting);
        mouse_wheel_sensitivity = (NumberPicker) v.findViewById(R.id.mousewheel_sensitivity_setting);

        mouse_sensitivity.setMinValue(1);
        mouse_sensitivity.setMaxValue(5);

        mouse_wheel_sensitivity.setMinValue(1);
        mouse_wheel_sensitivity.setMaxValue(5);
    }
}