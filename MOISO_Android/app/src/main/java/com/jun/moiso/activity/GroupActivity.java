package com.jun.moiso.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.jun.moiso.R;
import com.jun.moiso.adapter.GroupAdapter;
import com.jun.moiso.adapter.MemberAdapter;
import com.jun.moiso.databinding.ActivityGroupBinding;
import com.jun.moiso.item.GroupListItem;
import com.jun.moiso.item.MemberListItem;
import com.jun.moiso.viewmodel.GroupViewModel;
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

        viewModel = new MemberViewModel();
        //TODO : 나중에 삭제
        plusOnclick();

        binding.setViewModel(viewModel);

        context = getApplicationContext();
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
        memberAdapter.notifyDataSetChanged();//데이터 변경알림!
    }

    //TODO : 추가 테스트 후에 삭제
    public void plusOnclick()
    {
        viewModel.addItem(new MemberListItem("전세환","전세환"));
        viewModel.addItem(new MemberListItem("박준현", "박준현"));
        viewModel.addItem(new MemberListItem("이준영", "이준영"));

        binding.setViewModel(viewModel);


        ImageButton memberadd_btn = (ImageButton) findViewById(R.id.member_add_btn_group);
        memberadd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.addItem(new MemberListItem("test", "test"));
            }
        });
    }
}