package com.jun.moiso.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jun.moiso.MyApplication;
import com.jun.moiso.R;
import com.jun.moiso.database.KeyboardDB;
import com.jun.moiso.databinding.ActivityKeyboardCustomBinding;
import com.jun.moiso.model.CustomButton;
import com.jun.moiso.model.CustomKeyboard;
import com.jun.moiso.model.KeyButton;
import com.jun.moiso.viewmodel.KeyboardCustomViewModel;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class KeyboardCustomActivity extends AppCompatActivity {

    private View dragView;//현재 드래그 된 view
    private float dx,dy;



    private ActivityKeyboardCustomBinding binding;

    private KeyboardDB keyboardDB;

    private AutoCompleteTextView autoCompleteTextView;

    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<KeyButton> keyButtons = new ArrayList<>();

    private ArrayList<Button> buttonArrayList = new ArrayList<>();

    private FloatingActionButton delete_btn;
    private Animation delete_animation;
    private View parent_view;

    private CustomKeyboard customKeyboard;
    private KeyboardCustomViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_keyboard_custom);
        parent_view = binding.getRoot();

        keyboardDB = KeyboardDB.getInstance(this);

        Intent intent = getIntent();
        customKeyboard = new CustomKeyboard(intent.getIntExtra("custom_id",0), intent.getStringExtra("custom_name"), intent.getStringExtra("owner_id"));
        viewModel = ViewModelProviders.of(this).get(KeyboardCustomViewModel.class);
        viewModel.setCustomKeyboard(customKeyboard);

        binding.setViewModel(viewModel);

        getAnimations();

        //버튼들 불러오기
        getButtons(customKeyboard.getCustom_id(), (ViewGroup)parent_view);

        delete_btn = (FloatingActionButton)findViewById(R.id.delete_btn_keyboardcustom);
        setChildViewDragListener(delete_btn);

        //autocompletelist의 drop down 버튼 list 세팅

        MyApplication myApplication = (MyApplication) getApplication();
        list.addAll(myApplication.getList());
        keyButtons.addAll(myApplication.getKeyButtons());

        //settingList();


        //드래그앤 드롭
        setDragAndDrop((ViewGroup)parent_view);

        setAutoCompleteTextView((ViewGroup)parent_view);
    }


    //맨 처음 버튼들 불러오기
    private void getButtons(int custom_id, ViewGroup parent_layout)
    {
        ObservableArrayList<CustomButton> customButtons = keyboardDB.selectButtonsOf(custom_id);

        for(int i=0;i<customButtons.size();i++)
        {
            CustomButton customButton = customButtons.get(i);

            Button btn = new Button(getApplicationContext());
            btn.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            btn.setPadding(5,5,5,5);
            btn.setBackground(getDrawable(R.drawable.mouse_center_btn_layout));
            btn.setTag(customButton.getButton_id());
            btn.setText(customButton.getButton_text());
            btn.setX(customButton.getPos_x());
            btn.setY(customButton.getPos_y());

            setChildViewDragListener(btn);
            buttonArrayList.add(btn);

            //부모 레이아웃에 추가
            parent_layout.addView(btn);
        }
    }


    private void getAnimations()
    {
        delete_animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_remove_for_del_btn);
    }

    private void setAutoCompleteTextView(final ViewGroup parent_layout) {
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.key_find_autocomplete_keyboardcustom);
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list));
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String button_text = ((TextView) view).getText().toString();

                int button_key = findKeyCode(button_text);

                if(button_key != -1)
                    addButton(parent_layout, button_key, button_text);
            }
        });
    }

    private void addButton(ViewGroup parent_layout, int button_key, String button_text)
    {
        CustomButton customButton = keyboardDB.insertButton(button_key, button_text, 0, autoCompleteTextView.getY()+autoCompleteTextView.getHeight() * 2, customKeyboard.getCustom_id());
        //화면에 버튼 추가
        Button btn = new Button(getApplicationContext());
        btn.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btn.setPadding(5,5,5,5);
        btn.setBackground(getDrawable(R.drawable.mouse_center_btn_layout));
        btn.setTag(customButton.getButton_id()+"&"+button_key);
        btn.setText(button_text);
        btn.setX(customButton.getPos_x());
        btn.setY(customButton.getPos_y());

        setChildViewDragListener(btn);
        buttonArrayList.add(btn);

        //부모 레이아웃에 추가
        parent_layout.addView(btn);
    }

    //키코드 찾아오기
    private int findKeyCode(String button_text)
    {
        for(int i=0;i<keyButtons.size();i++)
        {
            if(keyButtons.get(i).getKey_name().equals(button_text))
                return keyButtons.get(i).getKey_code();
        }
        return -1;
    }

    //드래그할 자식뷰의 리스너 설정
    private void setChildViewDragListener(View childView)
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
    private void setDragAndDrop(final ViewGroup parentView)
    {
        parentView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {

                switch (dragEvent.getAction())
                {
                    case DragEvent.ACTION_DRAG_LOCATION:
                        dx = dragEvent.getX();
                        dy = dragEvent.getY();

                        //검색창 영역에 들어가면 드래그앤드롭 취소
                        if( isInAutoCompleteTextView() )
                            dragView.cancelDragAndDrop();

                        break;

                    case DragEvent.ACTION_DRAG_ENDED:

                        //버튼 이동
                        if(!isInAutoCompleteTextView())
                            moveButton();

                        //휴지통에 오면 버튼 삭제 처리
                        if(dragView.getClass() == Button.class && isInDelteBtn(dx,dy))
                            deleteButton(parentView);

                        break;
                }


                return true;
            }
        });
    }

    //검색창과 위치 겹치는지 확인
    private boolean isInAutoCompleteTextView()
    {
        if( autoCompleteTextView.getY() + autoCompleteTextView.getHeight() * 2 >= dy )
            return true;
        return false;
    }

    //버튼 이동 처리
    private void moveButton()
    {
        dragView.setX(dx - dragView.getWidth() / 2);
        dragView.setY(dy - dragView.getHeight() / 2);

        if(dragView.getClass() == Button.class)
        {
            StringTokenizer stringTokenizer = new StringTokenizer(dragView.getTag().toString(),"&");

            int button_id = Integer.parseInt(stringTokenizer.nextToken());

            keyboardDB.updateButtonPos(button_id, dragView.getX(), dragView.getY());
        }
    }

    //버튼 삭제 처리
    private void deleteButton(ViewGroup parentView)
    {
        StringTokenizer strtok = new StringTokenizer(dragView.getTag().toString(),"&");

        keyboardDB.deleteButton(Integer.parseInt(strtok.nextToken()));
        buttonArrayList.remove(dragView);

        delete_btn.setPressed(true);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.scale_up_for_del_btn);
        delete_btn.startAnimation(animation);
        dragView.startAnimation(delete_animation);

        parentView.removeView(dragView);

        delete_btn.setPressed(false);
    }

    private boolean isInDelteBtn(float dx, float dy)
    {
        if(delete_btn.getX() <= dx && dx <= delete_btn.getX() + delete_btn.getWidth() && delete_btn.getY() <= dy && dy <= delete_btn.getY() +delete_btn.getHeight())
            return true;
        return false;
    }




}