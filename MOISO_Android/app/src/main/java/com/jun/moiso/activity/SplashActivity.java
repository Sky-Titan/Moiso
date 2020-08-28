package com.jun.moiso.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jun.moiso.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        removeActionBar();

        ImageView mosio_icon = (ImageView) findViewById(R.id.moiso_ic_splash);
        doAnimation(mosio_icon,R.anim.alpha_create);


    }

    //권한
    public void checkSelfPermission() {
        String temp = "";

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
        {
            temp += Manifest.permission.INTERNET + " ";
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            temp += Manifest.permission.ACCESS_NETWORK_STATE + " ";
        }

        if (TextUtils.isEmpty(temp) == false)
        { // 권한 요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "),1);
        }

    }



    //상,하단 바 제거
    public void removeActionBar()
    {
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }


    //애니메이션 실행
    public void doAnimation(View v, int anim_id)
    {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),anim_id);

        v.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                checkSelfPermission();
                callMainActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //로그인 액티비티 호출
    public void callMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}