package com.jun.moiso.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jun.moiso.R;
import com.jun.moiso.adapter.ViewPagerAdapter;
import com.jun.moiso.fragment.GroupManagementFragment;
import com.jun.moiso.fragment.ProfileFragment;
import com.jun.moiso.fragment.SettingFragment;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    private GroupManagementFragment groupManagementFragment;
    private ProfileFragment profileFragment;
    private SettingFragment settingFragment;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GroupManagementFragment groupManagementFragment = new GroupManagementFragment();
        ProfileFragment profileFragment = new ProfileFragment();
        SettingFragment settingFragment = new SettingFragment();


        viewPager = (ViewPager2) findViewById(R.id.fragment_container_viewpager);
        viewPager.setUserInputEnabled(false);//user 스크롤 막음

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),getLifecycle());
        viewPagerAdapter.addFragment(groupManagementFragment);
        viewPagerAdapter.addFragment(profileFragment);
        viewPagerAdapter.addFragment(settingFragment);

        viewPager.setAdapter(viewPagerAdapter);


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.navigation_group :
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_profile :
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_setting :
                        viewPager.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });


    }
}