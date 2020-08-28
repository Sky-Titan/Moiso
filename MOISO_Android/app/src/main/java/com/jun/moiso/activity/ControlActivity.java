package com.jun.moiso.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.jun.moiso.R;
import com.jun.moiso.adapter.ViewPagerAdapter;
import com.jun.moiso.fragment.KeyboardFragment;
import com.jun.moiso.fragment.MouseFragment;
import com.jun.moiso.interfaces.SocketCallback;
import com.jun.moiso.socket.SocketLibrary;

public class ControlActivity extends AppCompatActivity {


    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private MouseFragment mouseFragment;
    private KeyboardFragment keyboardFragment;

    public TabLayout tabLayout;

    private SocketLibrary socketLibrary;

    private static final String TAG = "ControlActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().from(this).inflate(R.layout.activity_control,null);
        setContentView(view);



        socketLibrary = SocketLibrary.getInstance();

        //종료 신호 수신 콜백
        SocketCallback waitCallback = new SocketCallback() {
            @Override
            public void callback(boolean result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "연결 종료", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        };
        socketLibrary.waitSocketClose(waitCallback);//종료 신호 수신 대기

        createFragment();
        createViewpager();
        settingTabLayout();
    }

    @Override
    protected void onDestroy() {

        //액티비티 finish 되면 자동으로 연결 종료
        SocketCallback disconnectCallback = new SocketCallback() {
            @Override
            public void callback(boolean result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "연결 종료", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        socketLibrary.disconnect(disconnectCallback);
        super.onDestroy();
    }

    //fragment 생성
    public void createFragment()
    {
        mouseFragment = new MouseFragment();
        keyboardFragment = new KeyboardFragment();
    }

    //viewpager 및 어댑터 생성
    public void createViewpager()
    {
        viewPager = (ViewPager2) findViewById(R.id.viewpager_control);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPagerAdapter.addFragment(mouseFragment);
        viewPagerAdapter.addFragment(keyboardFragment);

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setUserInputEnabled(false);
    }

    //tablayout - viewpager 연결
    public void settingTabLayout()
    {
        tabLayout = (TabLayout)findViewById(R.id.tablayout_control);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();

                switch (pos)
                {
                    case 0 :
                        viewPager.setCurrentItem(0);
                        break;
                    case 1 :
                        viewPager.setCurrentItem(1);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}