package com.jun.moiso.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.jun.moiso.R;
import com.jun.moiso.adapter.GroupManagementAdapter;
import com.jun.moiso.databinding.FragmentGroupManagementBinding;
import com.jun.moiso.model.GroupListItem;
import com.jun.moiso.viewmodel.GroupManagementViewModel;

public class GroupManagementFragment extends Fragment {

    private static View v;
    private static GroupManagementViewModel viewModel;
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

        viewModel = ViewModelProviders.of(this).get(GroupManagementViewModel.class);
        viewModel.addItem(new GroupListItem("돌"));//todo : 나중에 지우기
        binding.setViewModel(viewModel);

        context = getActivity();

        //그룹 추가 리스너
        groupAddOnclick();

        return v;
    }

    //viewmodel의 item list에 변경 생길 때마다 호출
    @BindingAdapter("items")
    public static void setItems(RecyclerView recyclerView,  ObservableArrayList<GroupListItem> groupListItems)
    {
        GroupManagementAdapter groupManagementAdapter;

        //Recyclerview 초기화
        if(recyclerView.getAdapter() == null)
        {
            //구분선 적용
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);

            //adapter 적용
            groupManagementAdapter = new GroupManagementAdapter(context, viewModel);
            recyclerView.setAdapter(groupManagementAdapter);
        }
        else
            groupManagementAdapter = (GroupManagementAdapter)recyclerView.getAdapter();

        groupManagementAdapter.setGroupListItems(groupListItems);//item list 적용
    }

    //그룹 추가 버튼 리스너
    public void groupAddOnclick()
    {

        groupadd_btn = (ImageButton)v.findViewById(R.id.group_add_btn_groupmanagement);
        groupadd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View dialog_view = getLayoutInflater().inflate(R.layout.create_dialog,null);

                final EditText editText = (EditText)dialog_view.findViewById(R.id.edittext_dialog);

                builder.setView(dialog_view);

                builder.setTitle("그룹 추가").setMessage("추가할 그룹의 이름을 입력해주세요.");
                builder.setNegativeButton("취소",null);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String group_name = editText.getText().toString();
                        //TODO : 그룹 추가 작업
                        viewModel.addItem(new GroupListItem(group_name));
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}