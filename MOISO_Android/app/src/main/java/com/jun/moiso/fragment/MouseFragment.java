package com.jun.moiso.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jun.moiso.MyApplication;
import com.jun.moiso.R;

import com.jun.moiso.socket.SocketLibrary;

public class MouseFragment extends Fragment implements View.OnTouchListener {

    private LinearLayout pad_layout_mouse;
    private Button left_btn, wheel_btn, right_btn;
    private Button wheel_up_btn, wheel_bar_btn, wheel_down_btn;

    private int first_wheel_Y,current_wheel_Y,wheel_value_Y = 0;//휠 용
    private int first_X,first_Y,current_X,current_Y,value_X=0,value_Y=0;//마우스 커서용

    private static final String TAG = "MouseFragment";

    private SocketLibrary socketLibrary;
    private MyApplication myApplication;

    private View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_mouse, container, false);

        socketLibrary = SocketLibrary.getInstance();
        myApplication = (MyApplication) getActivity().getApplication();

        pad_layout_mouse = (LinearLayout) v.findViewById(R.id.pad_layout_mouse);
        pad_layout_mouse.setOnTouchListener(this);

        left_btn = (Button) v.findViewById(R.id.left_button_mouse);
        wheel_btn = (Button) v.findViewById(R.id.wheel_button_mouse);
        right_btn = (Button) v.findViewById(R.id.right_button_mouse);
        left_btn.setOnTouchListener(this);
        wheel_btn.setOnTouchListener(this);
        right_btn.setOnTouchListener(this);

        wheel_up_btn = (Button) v.findViewById(R.id.wheel_up_mouse);
        wheel_bar_btn = (Button) v.findViewById(R.id.wheel_bar_mouse);
        wheel_down_btn = (Button) v.findViewById(R.id.wheel_down_mouse);
        wheel_up_btn.setOnTouchListener(this);
        wheel_bar_btn.setOnTouchListener(this);
        wheel_down_btn.setOnTouchListener(this);

        return v;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int eventaction = motionEvent.getAction();

        //드래그
        if(view == pad_layout_mouse)
        {
            //DOWN -> MOVE -> UP
            switch (eventaction)
            {
                case MotionEvent.ACTION_UP://터치 뗄 때
                    break;
                case MotionEvent.ACTION_MOVE://터치 드래그할때
                    current_X = (int)motionEvent.getX();
                    current_Y = (int)motionEvent.getY();
                    //Log.d(TAG,"current_x : "+ current_X+" "+"current_y : "+current_Y);

                    value_X = current_X - first_X;
                    value_Y = current_Y - first_Y;

                    first_X = current_X;
                    first_Y = current_Y;

                    socketLibrary.sendMouseDragEvent(value_X,value_Y, myApplication.getMouse_sensitivity());

                    break;
                case MotionEvent.ACTION_DOWN://터치 했을 때 좌표
                    first_X = (int) motionEvent.getX();//첫 터치한 X 좌표
                    first_Y = (int) motionEvent.getY();//첫 터치한 Y 좌표
                   // Log.d(TAG,"first_x : "+ first_X+" "+"first_y : "+first_Y);

                    break;
            }
        }
        else if(view == left_btn || view == wheel_btn || view == right_btn)//마우스 버튼
        {
            String direction = "";
            String movement = "";

            if(view == left_btn)
                direction = "LEFT";
            else if(view == wheel_btn)
                direction = "WHEEL";
            else if(view == right_btn)
                direction = "RIGHT";

            if(eventaction == MotionEvent.ACTION_UP)//RELEASE
            {
                ((Button)view).setPressed(false);
                movement = "RELEASE";
                socketLibrary.sendMouseButtonEvent(direction, movement);
            }
            else if(eventaction == MotionEvent.ACTION_DOWN)//PRESS
            {
                ((Button)view).setPressed(true);
                movement = "PRESS";
                socketLibrary.sendMouseButtonEvent(direction, movement);
            }
        }
        else//마우스 휠
        {
            String direction = "";
            String number = "";

            if(view == wheel_up_btn)
            {
                direction = "UP";
                number = myApplication.getMouse_sensitivity()+"";
                socketLibrary.sendMouseWheelEvent(direction, number);
            }
            else if(view == wheel_down_btn)
            {
                direction = "DOWN";
                number = myApplication.getMouse_sensitivity()+"";
                socketLibrary.sendMouseWheelEvent(direction, number);
            }
            else//휠 바
            {
                direction = "BAR";

                //DOWN -> MOVE -> UP
                switch (eventaction)
                {
                    case MotionEvent.ACTION_UP://터치 뗄 때
                        break;
                    case MotionEvent.ACTION_MOVE://터치 드래그할때

                        current_wheel_Y = (int)motionEvent.getY();
                        Log.d(TAG,"current_wheel_y : "+current_wheel_Y);

                        wheel_value_Y = (current_wheel_Y - first_wheel_Y);

                        first_wheel_Y = current_wheel_Y;

                        number = wheel_value_Y+"";
                        socketLibrary.sendMouseWheelEvent(direction, number);

                        break;
                    case MotionEvent.ACTION_DOWN://터치 했을 때 좌표
                        first_wheel_Y = (int) motionEvent.getY();//첫 터치한 Y 좌표
                        Log.d(TAG,"first_wheel_y : "+first_wheel_Y);

                        break;
                }
            }
        }
        return true;
    }
}