package com.jun.moiso.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;

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

    private View dragView;//현재 드래그 된 view
    private float dx,dy;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().from(this).inflate(R.layout.activity_control,null);
        setContentView(view);

        FloatingActionButton controlAuthority_btn = (FloatingActionButton) findViewById(R.id.control_authority_btn_control);

        createFragment();
        createViewpager();
        settingTabLayout();

        setDragAndDrop(view);
        setChildViewDragListener(controlAuthority_btn);


        controlAuthority_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO : 제어권한 가져오기
            }
        });




    }

    //드래그할 자식뷰의 리스너 설정
    public void setChildViewDragListener(View childView)
    {
        childView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDragAndDrop(null,shadowBuilder,null,View.DRAG_FLAG_GLOBAL);
                dragView = view;
                return true;
            }
        });
    }

    //activity 드래그앤드롭 설정
    public void setDragAndDrop(View parentView)
    {
        parentView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                switch (dragEvent.getAction()) {

                    case DragEvent.ACTION_DRAG_LOCATION:
                        dx = dragEvent.getX();
                        dy = dragEvent.getY();
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        dragView.setX(dx - dragView.getWidth()/2);
                        dragView.setY(dy - dragView.getHeight()/2);
                        break;
                }
                return true;
            }
        });
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