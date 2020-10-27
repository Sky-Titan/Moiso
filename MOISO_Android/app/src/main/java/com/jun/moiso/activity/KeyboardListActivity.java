package com.jun.moiso.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jun.moiso.R;
import com.jun.moiso.adapter.KeyboardListActivityAdapter;
import com.jun.moiso.viewmodel.KeyboardListActivityViewModel;
import com.jun.moiso.viewmodel.KeyboardListActivityViewModelFactory;

public class KeyboardListActivity extends AppCompatActivity {

    private static KeyboardListActivityAdapter adapter;

    private RecyclerView recyclerView;


    private static KeyboardListActivity activity;
    private static KeyboardListActivityViewModel viewModel;
    private static Context context;

    private static final String TAG = "KeyboardListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard_list);

        viewModel = new ViewModelProvider(this, new KeyboardListActivityViewModelFactory(getApplication())).get(KeyboardListActivityViewModel.class);

        context = KeyboardListActivity.this;
        activity = this;

        recyclerView = findViewById(R.id.recyclerview_keyboardlist_activity);
        adapter = new KeyboardListActivityAdapter(context, viewModel, activity);

        viewModel.getKeyboards().observe(this, customKeyboards -> {
            adapter.submitList(customKeyboards);
        });

        recyclerView.setAdapter(adapter);

        //구분선 적용
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

    }

    //커스텀 키보드 추가 리스너
    public void addCustomOnClick(View v)
    {
        //키보드 커스텀 이름 이력 후 생성하는 액티비티
        View dialogView = getLayoutInflater().inflate(R.layout.create_dialog, null);
        final EditText editText = dialogView.findViewById(R.id.edittext_dialog);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("커스텀 키보드 추가").setMessage("추가할 커스텀 키보드의 이름을 입력하세요.");
        builder.setPositiveButton("확인", (dialogInterface, i) ->{

            String custom_name = editText.getText().toString();

            viewModel.addKeyboard(custom_name);
        });

        builder.setNegativeButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();;
    }
}