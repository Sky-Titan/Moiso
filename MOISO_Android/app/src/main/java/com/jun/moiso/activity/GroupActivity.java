package com.jun.moiso.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jun.moiso.R;
import com.jun.moiso.adapter.MemberAdapter;
import com.jun.moiso.databinding.ActivityGroupBinding;
import com.jun.moiso.interfaces.SocketCallback;
import com.jun.moiso.model.MemberListItem;
import com.jun.moiso.socket.SocketLibrary;
import com.jun.moiso.viewmodel.GroupViewModel;


public class GroupActivity extends AppCompatActivity {

    private static MemberAdapter memberAdapter;
    ActivityGroupBinding binding;

    private static GroupViewModel viewModel;
    private static Context context;

    private EditText ip_edittext, port_edittext;
    private String group_name,user_name;
    private SocketLibrary socketLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_group);
        viewModel = ViewModelProviders.of(this).get(GroupViewModel.class);

        binding.setViewModel(viewModel);

        socketLibrary = SocketLibrary.getInstance();

        Intent intent = getIntent();
        group_name = intent.getStringExtra("group_name");
        user_name = intent.getStringExtra("user_name");

        viewModel.setUser_name(user_name);
        viewModel.setGrop_name(group_name);
        viewModel.setSocketLibrary(socketLibrary);

        ip_edittext = (EditText) findViewById(R.id.ip_edittext_group);
        port_edittext = (EditText) findViewById(R.id.port_edittext_group);



        context = GroupActivity.this;
    }

    //viewmodel의 item list에 변경 생길 때마다 호출
    @BindingAdapter("items")
    public static void setItems(RecyclerView recyclerView, ObservableArrayList<MemberListItem> memberListItems)
    {
        //Recyclerview 초기화
        if(recyclerView.getAdapter() == null)
        {
            //구분선 적용
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);

            //adapter 적용
            memberAdapter = new MemberAdapter(context, viewModel);
            recyclerView.setAdapter(memberAdapter);
        }
        else
            memberAdapter = (MemberAdapter)recyclerView.getAdapter();

        memberAdapter.setMemberListItems(memberListItems);//item list 적용
    }

    //TODO : 멤버 추가 리스너
    public void addMemberOnclick(View v)
    {


    }

    //연결 버튼 클릭 리스너
    public void connectClick(final View v)
    {
        //PC 앱과 소켓 연결 작업 후 ControlActivity로 이동

        SocketCallback connectCallBack = new SocketCallback() {
            @Override
            public void callback(final boolean result) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(result)
                        {
                            Toast.makeText(getApplicationContext(), "연결 완료", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ControlActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "연결 실패", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        };


        //연결 시작
        socketLibrary.connect(ip_edittext.getText().toString(), Integer.parseInt(port_edittext.getText().toString()), group_name, user_name, connectCallBack);
    }


}