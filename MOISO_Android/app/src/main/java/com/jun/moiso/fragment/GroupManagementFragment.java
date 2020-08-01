package com.jun.moiso.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jun.moiso.R;
import com.jun.moiso.adapter.GroupAdapter;
import com.jun.moiso.databinding.FragmentGroupManagementBinding;
import com.jun.moiso.model.GroupListItem;
import com.jun.moiso.viewmodel.GroupViewModel;

public class GroupManagementFragment extends Fragment {

    private static View v;
    private static GroupViewModel groupViewModel;
    private FragmentGroupManagementBinding binding;
    private ImageButton groupadd_btn;

    private static Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_group_management, container, false);

        v = binding.getRoot();

        groupViewModel = new GroupViewModel();
        //TODO : 추가 테스트 후에 삭제
        plusOnclick();
        context = getActivity();
        return v;
    }

    //viewmodel의 item list에 변경 생길 때마다 호출
    @BindingAdapter("items")
    public static void setItems(RecyclerView recyclerView,  ObservableArrayList<GroupListItem> groupListItems)
    {
        GroupAdapter groupAdapter;

        //Recyclerview 초기화
        if(recyclerView.getAdapter() == null)
        {
            //구분선 적용
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);

            //adapter 적용
            groupAdapter = new GroupAdapter(context, groupViewModel);
            recyclerView.setAdapter(groupAdapter);
        }
        else
            groupAdapter = (GroupAdapter)recyclerView.getAdapter();

        groupAdapter.setGroupListItems(groupListItems);//item list 적용
    }

    //TODO : 추가 테스트 후에 삭제
    public void plusOnclick()
    {
        groupViewModel.addItem(new GroupListItem("전세환"));
        groupViewModel.addItem(new GroupListItem("박준현"));
        groupViewModel.addItem(new GroupListItem("이준영"));

        binding.setViewModel(groupViewModel);

        groupadd_btn = (ImageButton) v.findViewById(R.id.group_add_btn_groupmanagement);
        groupadd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupViewModel.addItem(new GroupListItem("test"));
            }
        });
    }
}