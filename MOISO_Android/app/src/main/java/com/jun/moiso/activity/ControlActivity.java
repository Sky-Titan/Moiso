package com.jun.moiso.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.jun.moiso.R;
import com.jun.moiso.adapter.ViewPagerAdapter;
import com.jun.moiso.fragment.KeyboardFragment;
import com.jun.moiso.fragment.MouseFragment;

public class ControlActivity extends AppCompatActivity {


    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private MouseFragment mouseFragment;
    private KeyboardFragment keyboardFragment;

    public TabLayout tabLayout;
    public Button controlAuthority_btn;

    public float call_custom_move_limit;

    private static final String TAG = "ControlActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().from(this).inflate(R.layout.activity_control,null);
        setContentView(view);

        controlAuthority_btn = (Button) findViewById(R.id.control_authority_btn_control);


        createFragment();
        createViewpager();
        settingTabLayout();
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