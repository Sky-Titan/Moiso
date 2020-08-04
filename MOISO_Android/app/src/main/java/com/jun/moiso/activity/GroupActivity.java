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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jun.moiso.R;
import com.jun.moiso.adapter.GroupAdapter;
import com.jun.moiso.databinding.ActivityGroupBinding;
import com.jun.moiso.interfaces.SocketCallback;
import com.jun.moiso.model.MemberListItem;
import com.jun.moiso.socket.SocketLibrary;
import com.jun.moiso.viewmodel.GroupViewModel;


public class GroupActivity extends AppCompatActivity {

    private static GroupAdapter groupAdapter;
    ActivityGroupBinding binding;

    private static GroupViewModel viewModel;
    private static Context context;

    private EditText ip_edittext, port_edittext;
    private String group_name,user_name;
    private SocketLibrary socketLibrary;

    //자동 저장
    private SharedPreferences sf;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_group);
        viewModel = ViewModelProviders.of(this).get(GroupViewModel.class);

        binding.setViewModel(viewModel);

        socketLibrary = SocketLibrary.getInstance();

        //저장된 IP 정보 가져오기
        sf =getSharedPreferences("IP_INFO", MODE_PRIVATE);
        editor = sf.edit();

        Intent intent = getIntent();
        group_name = intent.getStringExtra("group_name");
        user_name = intent.getStringExtra("user_name");

        viewModel.setUser_name(user_name);
        viewModel.setGrop_name(group_name);
        viewModel.setSocketLibrary(socketLibrary);

        //TODO : 나중에 그룹별로 서버에서 저장된 IP주소 불러와야함
        ip_edittext = (EditText) findViewById(R.id.ip_edittext_group);
        ip_edittext.setText(sf.getString("IP",""));
        ip_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
                editor.putString("IP", ip_edittext.getText().toString());
                editor.commit();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        port_edittext = (EditText) findViewById(R.id.port_edittext_group);
        port_edittext.setText(sf.getString("PORT","5001"));
        port_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editor.putString("PORT", port_edittext.getText().toString());
                editor.commit();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
            groupAdapter = new GroupAdapter(context, viewModel);
            recyclerView.setAdapter(groupAdapter);
        }
        else
            groupAdapter = (GroupAdapter)recyclerView.getAdapter();

        groupAdapter.setMemberListItems(memberListItems);//item list 적용
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