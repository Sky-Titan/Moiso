package com.jun.moiso.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jun.moiso.MyApplication;
import com.jun.moiso.R;

import com.jun.moiso.model.CustomButton;
import com.jun.moiso.model.CustomKeyboard;
import com.jun.moiso.model.KeyButton;
import com.jun.moiso.viewmodel.KeyboardCustomViewModel;
import com.jun.moiso.viewmodel.KeyboardCustomViewModelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class KeyboardCustomActivity extends AppCompatActivity {

    private View dragView;//현재 드래그 된 view
    private float dx,dy;


    //버튼 검색창
    private AutoCompleteTextView autoCompleteTextView;

    private ArrayList<String> list = new ArrayList<>();
    private HashMap<String, Integer> keyButtons = new HashMap<>();

    private HashMap<Integer ,Button> buttons = new HashMap<>();

    private FloatingActionButton delete_btn;
    private Animation delete_animation;
    private View parent_view;

    private KeyboardCustomViewModel viewModel;

    private static final String TAG = "KeyboardCustomActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().from(this).inflate(R.layout.activity_keyboard_custom,null);

        setContentView(view);

        Intent intent = getIntent();

        parent_view = view;

        viewModel = new ViewModelProvider(this, new KeyboardCustomViewModelFactory(getApplication(), intent.getIntExtra("custom_id",0))).get(KeyboardCustomViewModel.class);

        TextView custom_name_tv = findViewById(R.id.custom_name_text);
        custom_name_tv.setText(intent.getStringExtra("custom_name"));

        getAnimations();

        //버튼들 불러오기
        getButtons((ViewGroup)parent_view);

        delete_btn = findViewById(R.id.delete_btn_keyboardcustom);
        setChildViewDragListener(delete_btn);

        //autocompletelist의 drop down 버튼 list 세팅

        MyApplication myApplication = (MyApplication) getApplication();
        list.addAll(myApplication.getList());

        keyButtons = myApplication.getKeyButtons();

        //settingList();


        //드래그앤 드롭
        setDragAndDrop((ViewGroup)parent_view);

        setAutoCompleteTextView((ViewGroup)parent_view);
    }


    //맨 처음 버튼들 불러오기
    private void getButtons(ViewGroup parent_layout)
    {
        Observable.create(
            e -> {
                System.out.println(Thread.currentThread().getName());
                e.onNext(viewModel.getCustomButtons());
            }
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    LiveData<List<CustomButton>> custom_btn_live = (LiveData<List<CustomButton>>)o;

                    if(!custom_btn_live.hasObservers()) {
                        custom_btn_live.observe(this, customButtons -> {
                            for (int i = 0; i < customButtons.size(); i++) {
                                CustomButton customButton = customButtons.get(i);

                                Button btn = new Button(getApplicationContext());
                                btn.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                btn.setPadding(5, 5, 5, 5);
                                btn.setBackground(getDrawable(R.drawable.mouse_center_btn_layout));
                                btn.setTag(customButton.button_id);
                                btn.setText(customButton.button_text);
                                btn.setX(customButton.pos_x);
                                btn.setY(customButton.pos_y);


                                Log.d(TAG, "옵저버 추가");
                                setChildViewDragListener(btn);

                                if (!buttons.containsKey(customButton.button_id)) {
                                    buttons.putIfAbsent(customButton.button_id, btn);
                                    //부모 레이아웃에 추가
                                    parent_layout.addView(btn);
                                }

                            }
                            ((LiveData<List<CustomButton>>) o).removeObservers(this);
                        });
                    }
                });

    }


    private void getAnimations()
    {
        delete_animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_remove_for_del_btn);
    }

    private void setAutoCompleteTextView(final ViewGroup parent_layout) {

        autoCompleteTextView = findViewById(R.id.key_find_autocomplete_keyboardcustom);
        autoCompleteTextView.setAdapter(new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list));

        autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {

            String button_text = ((TextView) view).getText().toString();

            int button_key = findKeyCode(button_text);

            if(button_key != -1)
                addButton(parent_layout, button_key, button_text);
        });
    }

    private void addButton(ViewGroup parent_layout, int button_key, String button_text)
    {
        viewModel.insertButton(button_key, button_text, 0, autoCompleteTextView.getY() + autoCompleteTextView.getHeight() * 2);
        Observable.create(
            e -> e.onNext(viewModel.getRecentButton())
        ).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    CustomButton customButton = (CustomButton)o;
                    //화면에 버튼 추가
                    Button btn = new Button(getApplicationContext());
                    btn.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    btn.setPadding(5,5,5,5);
                    btn.setBackground(getDrawable(R.drawable.mouse_center_btn_layout));
                    btn.setTag(customButton.button_id);
                    btn.setText(customButton.button_text);
                    btn.setX(customButton.pos_x);
                    btn.setY(customButton.pos_y);

                    Log.d(TAG, customButton.button_text);

                    setChildViewDragListener(btn);

                    if(!buttons.containsKey(customButton.button_id))
                    {
                        buttons.putIfAbsent(customButton.button_id, btn);

                        //부모 레이아웃에 추가
                        parent_layout.addView(btn);
                    }
                });
    }

    //키코드 찾아오기
    private int findKeyCode(String button_text)
    {
        return keyButtons.get(button_text);
    }

    //드래그할 자식뷰의 리스너 설정
    private void setChildViewDragListener(View childView)
    {
        childView.setOnLongClickListener(view ->  {

            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDragAndDrop(null, shadowBuilder,null,View.DRAG_FLAG_GLOBAL);
            dragView = view;

            return true;
        });
    }

    //activity 드래그앤드롭 설정
    private void setDragAndDrop(final ViewGroup parentView)
    {
        parentView.setOnDragListener((view, dragEvent) -> {

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

            viewModel.updateButtonPos(button_id, dragView.getX(), dragView.getY());
        }
    }

    //버튼 삭제 처리
    private void deleteButton(ViewGroup parentView)
    {
        viewModel.deleteButton((int)(dragView.getTag()));

        buttons.remove(dragView.getTag());

        delete_btn.setPressed(true);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_up_for_del_btn);
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