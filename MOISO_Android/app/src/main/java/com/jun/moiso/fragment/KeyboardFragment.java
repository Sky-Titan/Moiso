package com.jun.moiso.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

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
import com.jun.moiso.model.CustomButton;
import com.jun.moiso.socket.SocketLibrary;
import com.jun.moiso.viewmodel.KeyboardFragmentViewModel;
import com.jun.moiso.viewmodel.KeyboardFragmentViewModelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class KeyboardFragment extends Fragment {

    /*
    키보드 신호 전송 프래그먼트
     */

    private View v;

    public View dragView;//현재 드래그 된 view
    private float dx,dy;


    private static final String TAG = "KeyboardFragment";


    private FloatingActionButton callCustom_btn;
    public float call_custom_move_limit;
    private ArrayList<Button> buttonArrayList = new ArrayList<>();

    private SocketLibrary socketLibrary;

    private ArrayList<String> list = new ArrayList<>();
    private HashMap<String, Integer> keyButtons = new HashMap<>();

    private KeyboardFragmentViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_keyboard, container, false);

        viewModel = new ViewModelProvider(this, new KeyboardFragmentViewModelFactory(getActivity().getApplication(), 0)).get(KeyboardFragmentViewModel.class);

        //커스텀 불러오기 버튼
        callCustom_btn = v.findViewById(R.id.call_custom_fab_keyboardfragment);
        callCustom_btn.setOnClickListener(view -> {

            Intent intent = new Intent(getContext(), KeyboardListActivity.class);
            startActivityForResult(intent, 0);
        });
        setChildViewDragListener(callCustom_btn);

        setDragAndDrop((ViewGroup) v);

        calculateCustomMoveLimit();

        MyApplication myApplication = (MyApplication)getActivity().getApplication();
        list.addAll(myApplication.getList());
        keyButtons = myApplication.getKeyButtons();

        socketLibrary = SocketLibrary.getInstance();

        return v;
    }

    //call custom버튼이 이동할 수 있는 한계 계산
    private void calculateCustomMoveLimit()
    {
        call_custom_move_limit = ((ControlActivity)getActivity()).tabLayout.getHeight();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //키보드 리스트 띄운 후 선택해서 키보드 커스텀 가져옴
        if(requestCode == 0)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                int custom_id = data.getIntExtra("custom_id", 0);
                Log.d(TAG, "custom_id : "+custom_id);
                viewModel.setCustom_id(custom_id);
                getButtons((ViewGroup) v);
            }
        }
    }



    //맨 처음 버튼들 불러오기
    private void getButtons(ViewGroup parent_layout)
    {
        parent_layout.removeAllViews();
        parent_layout.addView(callCustom_btn);
        buttonArrayList.clear();

        Observable.create(
                e -> e.onNext(viewModel.getCustomButtons())
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {

                    LiveData<List<CustomButton>> list = (LiveData<List<CustomButton>>)  o;
                    list.observe(getViewLifecycleOwner(), customButtons -> {
                        for(int i=0;i<customButtons.size();i++) {
                            CustomButton customButton = customButtons.get(i);

                            Button btn = new Button(getContext());
                            btn.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            btn.setPadding(5, 5, 5, 5);
                            btn.setBackground(getActivity().getDrawable(R.drawable.mouse_center_btn_layout));
                            btn.setTag(customButton.button_id);
                            btn.setText(customButton.button_text);
                            btn.setX(customButton.pos_x);
                            btn.setY(customButton.pos_y);

                            btn.setOnTouchListener((view, motionEvent) -> {

                                int key_code = findKeyCode(btn.getText().toString());
                                String motion = "";

                                switch (motionEvent.getAction()) {
                                    case MotionEvent.ACTION_UP:
                                        motion = "RELEASE";
                                        btn.setPressed(false);
                                        //송신 결과 리턴
                                        socketLibrary.sendKeyboardEvent(key_code, motion);
                                        Log.i(TAG, "Keyboard " + key_code + " " + motion + " Send Complete");
                                        break;

                                    case MotionEvent.ACTION_DOWN:
                                        motion = "PRESS";
                                        btn.setPressed(true);
                                        //송신 결과 리턴
                                        socketLibrary.sendKeyboardEvent(key_code, motion);
                                        Log.i(TAG, "Keyboard " + key_code + " " + motion + " Send Complete");
                                        break;
                                }

                                return true;
                            });

                            buttonArrayList.add(btn);

                            //부모 레이아웃에 추가
                            parent_layout.addView(btn);
                        }
                        list.removeObservers(this);
                    });


                });
    }

    private int findKeyCode(String text)
    {
        return keyButtons.get(text);
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
        });
    }

    //드래그할 자식뷰의 리스너 설정
    private void setChildViewDragListener(View childView)
    {
        childView.setOnLongClickListener(view -> {

            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDragAndDrop(null,shadowBuilder,null, View.DRAG_FLAG_GLOBAL);
            dragView = view;

            return true;
        });
    }

    //버튼 이동 처리
    private void moveButton()
    {
        dragView.setX(dx - dragView.getWidth() / 2);
        dragView.setY(dy - dragView.getHeight() / 2);
    }






}