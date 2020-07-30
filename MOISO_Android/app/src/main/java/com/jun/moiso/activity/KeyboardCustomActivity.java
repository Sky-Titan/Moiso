package com.jun.moiso.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jun.moiso.R;

import java.util.ArrayList;

public class KeyboardCustomActivity extends AppCompatActivity {

    private View dragView,drag_btn;//현재 드래그 된 view
    private float dx,dy;

    private AutoCompleteTextView autoCompleteTextView;
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<Button> buttonArrayList = new ArrayList<>();

    private FloatingActionButton save_btn, delete_btn;
    private Animation create_animation, delete_animation;
    private ConstraintLayout parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent_view = (ConstraintLayout)getLayoutInflater().from(this).inflate(R.layout.activity_keyboard_custom,null);
        setContentView(parent_view);


        getAnimations();

        delete_btn = (FloatingActionButton)findViewById(R.id.delete_btn_keyboardcustom);

        //autocompletelist의 drop down 버튼 list 세팅
        settingList();

        //저장버튼
        save_btn = (FloatingActionButton)findViewById(R.id.save_btn_keyboardcustom);

        setChildViewDragListener(save_btn);

        //드래그앤 드롭
        setDragAndDrop(parent_view);

        setAutoCompleteTextView(parent_view);

    }

    public void getAnimations()
    {
        create_animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_create_for_del_btn);
        delete_animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_remove_for_del_btn);
    }

    public void setAutoCompleteTextView(final ViewGroup parent_layout)
    {
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.key_find_autocomplete_keyboardcustom);
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,list));
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text = ((TextView)view).getText().toString();

                //화면에 버튼 추가
                Button btn = new Button(getApplicationContext());
                btn.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                btn.setPadding(5,5,5,5);
                btn.setBackground(getDrawable(R.drawable.mouse_center_btn_layout));
                btn.setTag(text);
                btn.setText(text);
                btn.setX(0);
                btn.setY(autoCompleteTextView.getY()+autoCompleteTextView.getHeight() * 2);

                setChildViewDragListener(btn);
                buttonArrayList.add(btn);

                //부모 레이아웃에 추가
               parent_layout.addView(btn);
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

                    //삭제 버튼 나타남
                    if(dragView.getClass() == Button.class) {
                        delete_btn.setVisibility(View.VISIBLE);
                        delete_btn.startAnimation(create_animation);
                    }

                return true;
            }
        });
    }

    //activity 드래그앤드롭 설정
    public void setDragAndDrop(final ViewGroup parentView)
    {
        parentView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {

                switch (dragEvent.getAction())
                {
                    case DragEvent.ACTION_DRAG_LOCATION:
                        dx = dragEvent.getX();
                        dy = dragEvent.getY();

                        //autocompletetextview 위로 올라가지 못하게 바로 드래그앤 드롭 캔슬
                        if( autoCompleteTextView.getY() + autoCompleteTextView.getHeight() * 2 >= dy )
                            dragView.cancelDragAndDrop();

                        break;

                    case DragEvent.ACTION_DRAG_ENDED:

                        //버튼 삭제 처리
                        if(dragView.getClass() == Button.class && isInDelteBtn(dx,dy))
                        {
                            dragView.startAnimation(delete_animation);
                            parentView.removeView(dragView);
                        }
                        else {
                            //버튼 이동 처리
                            dragView.setX(dx - dragView.getWidth() / 2);
                            dragView.setY(dy - dragView.getHeight() / 2);
                        }

                        //삭제 버튼 사라짐
                        if(delete_btn.getVisibility() == View.VISIBLE)
                        {
                            delete_btn.startAnimation(delete_animation);
                            delete_btn.setVisibility(View.GONE);
                        }

                        break;
                    }


                return true;
            }
        });
    }

    public boolean isInDelteBtn(float dx, float dy)
    {
        if(delete_btn.getX() <= dx && dx <= delete_btn.getX() + delete_btn.getWidth() && delete_btn.getY() <= dy && dy <= delete_btn.getY() +delete_btn.getHeight())
            return true;
        return false;
    }

    public void settingList()
    {
        list.add("ESC");

        //F1 ~ F12
        for(int i = 1; i<=12;i++)
            list.add("F"+i);

        //숫자 1~9
        for(int i = 0; i<=9;i++)
            list.add("NUMBER"+i);

        list.add("Print Screen");
        list.add("Scroll Lock");
        list.add("Pause Break");

        list.add("Insert");
        list.add("Home");
        list.add("Page Up");
        list.add("Page Down");
        list.add("Delete");
        list.add("End");

        list.add("NumLock");
        list.add("/");
        list.add("*");
        list.add("-");
        list.add("+");
        //숫자패드 1~9
        for(int i = 0; i<=9;i++)
            list.add("NUMBER_PAD"+i);

        list.add("Alt");
        list.add("Ctrl");
        list.add("Shift");
        list.add("Enter");
        list.add("Space");
        list.add("Back Space");
        list.add("Tab");
        list.add("Caps Lock");

        list.add("`");
        list.add(";");
        list.add("'");
        list.add("[");
        list.add("]");
        list.add(",");
        list.add(".");
        list.add("=");
        list.add("\\");

        list.add("Window");

        list.add("↑");
        list.add("↓");
        list.add("←");
        list.add("→");


        //알파벳
        for(int i=65;i<=90;i++)
            list.add(""+(char)i);
    }



}