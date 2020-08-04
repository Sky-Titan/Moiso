package com.jun.moiso.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    //자동 저장
    private SharedPreferences sf;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_setting, container, false);

        sf = getActivity().getSharedPreferences("SETTING", Context.MODE_PRIVATE);
        editor = sf.edit();

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

        mouse_sensitivity.setValue(sf.getInt("MOUSE_SENSITIVITY",1));
        mouse_wheel_sensitivity.setValue(sf.getInt("WHEEL_SENSITIVITY",1));

        mouse_sensitivity.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                editor.putInt("MOUSE_SENSITIVITY", i1);
                editor.commit();
            }
        });

        mouse_wheel_sensitivity.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                editor.putInt("WHEEL_SENSITIVITY", i1);
                editor.commit();
            }
        });

    }
}