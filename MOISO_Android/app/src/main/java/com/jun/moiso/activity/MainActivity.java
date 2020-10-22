package com.jun.moiso.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jun.moiso.R;
import com.jun.moiso.adapter.ViewPagerAdapter;
import com.jun.moiso.fragment.ConnectFragment;
import com.jun.moiso.fragment.KeyboardListFragment;
import com.jun.moiso.fragment.SettingFragment;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    private ConnectFragment connectFragment;
    private KeyboardListFragment keyboardListFragment;
    private SettingFragment settingFragment;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFragments();
        setViewPager();
        setViewPagerAdapter();
        setBottomNavigationView();

    }

    //프래그먼트 생성
    private void setFragments()
    {
        connectFragment = new ConnectFragment();
        keyboardListFragment = new KeyboardListFragment();
        settingFragment = new SettingFragment();
    }

    //viewpager 생성
    private void setViewPager()
    {
        viewPager = findViewById(R.id.viewpager_main);
        viewPager.setUserInputEnabled(false);//user 스크롤 막음
    }

    //viewpager adapter 생성
    private void setViewPagerAdapter()
    {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),getLifecycle());
        viewPagerAdapter.addFragment(connectFragment);
        viewPagerAdapter.addFragment(keyboardListFragment);
        viewPagerAdapter.addFragment(settingFragment);

        viewPager.setAdapter(viewPagerAdapter);
    }

    //bottomnavigation view 생성 및 세팅
    private void setBottomNavigationView()
    {
        bottomNavigationView = findViewById(R.id.bottomnavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item ->  {

            switch (item.getItemId())
            {
                case R.id.navigation_connect:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_keyboard:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_setting :
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        });
    }
}