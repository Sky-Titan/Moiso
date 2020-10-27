package com.jun.moiso.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.jun.moiso.R;
import com.jun.moiso.adapter.KeyboardListFragmentAdapter;

import com.jun.moiso.viewmodel.KeyboardListFragmentViewModel;
import com.jun.moiso.viewmodel.KeyboardListFragmentViewModelFactory;


public class KeyboardListFragment extends Fragment {

    private KeyboardListFragmentAdapter adapter;

    private RecyclerView recyclerView;

    private KeyboardListFragmentViewModel viewModel;

    private View v;
    private static final String TAG = "KeyboardListFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG, "onCreateView");

        v = inflater.inflate(R.layout.fragment_keyboard_list, container, false);

        viewModel = new ViewModelProvider(this, new KeyboardListFragmentViewModelFactory(getActivity().getApplication())).get(KeyboardListFragmentViewModel.class);

        recyclerView = v.findViewById(R.id.recyclerview_keyboardlist_fragment);
        adapter = new KeyboardListFragmentAdapter(getContext(), viewModel);

        viewModel.getKeyboards().observe(getViewLifecycleOwner(), customKeyboards -> {
            adapter.submitList(customKeyboards);
        });

        recyclerView.setAdapter(adapter);

        //구분선 적용
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        ImageButton addCustom_btn = v.findViewById(R.id.keyboardcustom_add_btn_keyboardlist_fragment);
        addCustom_btn.setOnClickListener( view -> {

                //키보드 커스텀 이름 이력 후 생성하는 액티비티
                View dialogView = getLayoutInflater().inflate(R.layout.create_dialog, null);
                final EditText editText = dialogView.findViewById(R.id.edittext_dialog);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(dialogView);
                builder.setTitle("커스텀 키보드 추가").setMessage("추가할 커스텀 키보드의 이름을 입력하세요.");
                builder.setPositiveButton("확인", (dialogInterface, i) ->{

                    String custom_name = editText.getText().toString();

                    viewModel.addKeyboard(custom_name);
                });

                builder.setNegativeButton("취소", null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();;
        });

        return v;
    }


}