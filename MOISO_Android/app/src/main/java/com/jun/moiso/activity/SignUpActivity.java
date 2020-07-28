package com.jun.moiso.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.jun.moiso.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    //회원가입 완료
    public void signupComplete(View v)
    {
        //TODO : 회원가입 처리
        finish();
    }

    //뒤로가기
    public void backSpace(View v)
    {
        finish();
    }

}