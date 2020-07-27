package com.jun.moiso.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.jun.moiso.R;
import com.jun.moiso.interfaces.Animator;

public class SplashActivity extends AppCompatActivity implements Animator {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        removeActionBar();

        TextView mosio_textview = (TextView) findViewById(R.id.moiso_text_splash);
        doAnimation(mosio_textview,R.anim.alpha_create);
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
                callSignInActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //로그인 액티비티 호출
    public void callSignInActivity()
    {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

}