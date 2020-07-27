package com.jun.moiso.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jun.moiso.R;
import com.jun.moiso.adapter.GroupAdapter;
import com.jun.moiso.databinding.FragmentGroupManagementBinding;
import com.jun.moiso.item.GroupListItem;
import com.jun.moiso.viewmodel.GroupViewModel;

public class GroupManagementFragment extends Fragment {

    private static View v;
    private static GroupViewModel groupViewModel;
    private FragmentGroupManagementBinding binding;
    private ImageButton groupadd_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_group_management, container, false);

        v = binding.getRoot();

        //TODO : 추가 테스트 후에 삭제
        plusOnclick();

        return v;
    }

    @BindingAdapter("items")
    public static void setItems(RecyclerView recyclerView,  ObservableArrayList<GroupListItem> groupListItems)
    {
        GroupAdapter groupAdapter;
        if(recyclerView.getAdapter() == null)
        {
            groupAdapter = new GroupAdapter(v.getContext(), groupViewModel);
            recyclerView.setAdapter(groupAdapter);
        }
        else
            groupAdapter = (GroupAdapter)recyclerView.getAdapter();

        groupAdapter.setGroupListItems(groupListItems);
        groupAdapter.notifyDataSetChanged();//데이터 변경알림
    }

    //TODO : 추가 테스트 후에 삭제
    public void plusOnclick()
    {
        groupViewModel = new GroupViewModel();
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