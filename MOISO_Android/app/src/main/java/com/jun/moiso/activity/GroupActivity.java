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
import android.widget.ImageButton;

import com.jun.moiso.R;
import com.jun.moiso.adapter.MemberAdapter;
import com.jun.moiso.databinding.ActivityGroupBinding;
import com.jun.moiso.model.MemberListItem;
import com.jun.moiso.viewmodel.MemberViewModel;

public class GroupActivity extends AppCompatActivity {

    private static MemberAdapter memberAdapter;
    ActivityGroupBinding binding;

    private static MemberViewModel viewModel;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_group);
        viewModel = ViewModelProviders.of(this).get(MemberViewModel.class);

        binding.setViewModel(viewModel);

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

    //멤버 추가 리스너
    public void addMemberOnclick(View v)
    {


    }

    //연결 버튼 클릭 리스너
    public void connectClick(View v)
    {
        //TODO : PC 앱과 소켓 연결 작업 후 ControlActivity로 이동
        Intent intent = new Intent(this, ControlActivity.class);
        startActivity(intent);
    }
}