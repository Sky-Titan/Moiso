package com.jun.moiso.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jun.moiso.R;
import com.jun.moiso.activity.ControlActivity;

public class KeyboardFragment extends Fragment {

    private View v;

    public View dragView;//현재 드래그 된 view
    private float dx,dy;

    private static final String TAG = "KeyboardFragment";

    private FloatingActionButton callCustom_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_keyboard, container, false);

        callCustom_btn = (FloatingActionButton) v.findViewById(R.id.call_custom_fab_keyboardfragment);
        callCustom_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        setChildViewDragListener(callCustom_btn);

        setDragAndDrop((ViewGroup) v);

        return v;
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
                            Log.d(TAG, "MOVED");
                            break;

                        case DragEvent.ACTION_DRAG_ENDED:
                            Log.d(TAG, "ended");
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