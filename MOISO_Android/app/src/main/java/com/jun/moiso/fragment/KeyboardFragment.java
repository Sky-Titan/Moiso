package com.jun.moiso.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jun.moiso.MyApplication;
import com.jun.moiso.R;
import com.jun.moiso.activity.ControlActivity;
import com.jun.moiso.activity.KeyboardListActivity;
import com.jun.moiso.database.KeyboardDB;
import com.jun.moiso.model.CustomButton;
import com.jun.moiso.model.CustomKeyboard;
import com.jun.moiso.model.KeyButton;
import com.jun.moiso.socket.SocketLibrary;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class KeyboardFragment extends Fragment {

    private View v;

    public View dragView;//현재 드래그 된 view
    private float dx,dy;

    private KeyboardDB keyboardDB;

    private static final String TAG = "KeyboardFragment";
    private CustomKeyboard customKeyboard;

    private FloatingActionButton callCustom_btn;
    public float call_custom_move_limit;
    private ArrayList<Button> buttonArrayList = new ArrayList<>();

    private SocketLibrary socketLibrary;

    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<KeyButton> keyButtons = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_keyboard, container, false);

        keyboardDB = KeyboardDB.getInstance(getContext());

        //커스텀 불러오기 버튼
        callCustom_btn = (FloatingActionButton) v.findViewById(R.id.call_custom_fab_keyboardfragment);
        callCustom_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), KeyboardListActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        setChildViewDragListener(callCustom_btn);

        setDragAndDrop((ViewGroup) v);

        calculateCustomMoveLimit();

        MyApplication myApplication = (MyApplication)getActivity().getApplication();
        list.addAll(myApplication.getList());
        keyButtons.addAll(myApplication.getKeyButtons());

        socketLibrary = SocketLibrary.getInstance();

        return v;
    }

    //call custom버튼이 이동할 수 있는 한계 계산
    private void calculateCustomMoveLimit()
    {
        call_custom_move_limit = ((ControlActivity)getActivity()).controlAuthority_btn.getHeight() + ((ControlActivity)getActivity()).tabLayout.getHeight();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                customKeyboard = new CustomKeyboard(data.getIntExtra("custom_id",0), data.getStringExtra("custom_name"), data.getStringExtra("owner_id"));
                getButtons(customKeyboard.getCustom_id(), (ViewGroup) v);
            }
        }
    }

    @Override
    public void onResume() {
        if(customKeyboard!=null)
            getButtons(customKeyboard.getCustom_id(), (ViewGroup) v);
        super.onResume();

    }

    //맨 처음 버튼들 불러오기
    private void getButtons(int custom_id, ViewGroup parent_layout)
    {
        parent_layout.removeAllViews();
        parent_layout.addView(callCustom_btn);
        buttonArrayList.clear();

        ObservableArrayList<CustomButton> customButtons = keyboardDB.selectButtonsOf(custom_id);

        for(int i=0;i<customButtons.size();i++)
        {
            final CustomButton customButton = customButtons.get(i);

            Button btn = new Button(getContext());
            btn.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            btn.setPadding(5,5,5,5);
            btn.setBackground(getActivity().getDrawable(R.drawable.mouse_center_btn_layout));
            btn.setTag(customButton.getButton_id()+"&"+customButton.getButton_key());
            btn.setText(customButton.getButton_text());
            btn.setX(customButton.getPos_x());
            btn.setY(customButton.getPos_y());

            btn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    Button btn = (Button)view;

                    int key_code = parseKeyCode(btn.getTag().toString());
                    String motion = "";

                    switch (motionEvent.getAction())
                    {
                        case MotionEvent.ACTION_UP :
                            motion = "RELEASE";
                            btn.setPressed(false);
                            //송신 결과 리턴
                            socketLibrary.sendKeyboardEvent(key_code, motion);
                            Log.i(TAG,"Keyboard "+key_code+" "+motion+" Send Complete");
                            break;

                            case MotionEvent.ACTION_DOWN :
                                motion = "PRESS";
                                btn.setPressed(true);
                                //송신 결과 리턴
                                socketLibrary.sendKeyboardEvent(key_code, motion);
                                Log.i(TAG,"Keyboard "+key_code+" "+motion+" Send Complete");
                                break;
                    }



                    return true;
                }
            });

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            buttonArrayList.add(btn);

            //부모 레이아웃에 추가
            parent_layout.addView(btn);
        }
    }

    private int parseKeyCode(String tag)
    {
        StringTokenizer strtok = new StringTokenizer(tag,"&");
        strtok.nextToken();
        return Integer.parseInt(strtok.nextToken());
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

                            Log.d(TAG,call_custom_move_limit+"");

                            if(dy <= call_custom_move_limit)
                                dragView.cancelDragAndDrop();
                            break;

                        case DragEvent.ACTION_DRAG_ENDED:
                            if(dy > call_custom_move_limit)
                                moveButton();
                            break;
                    }

                return true;
            }
        });
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

    //버튼 이동 처리
    private void moveButton()
    {
        dragView.setX(dx - dragView.getWidth() / 2);
        dragView.setY(dy - dragView.getHeight() / 2);
    }
}