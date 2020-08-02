package com.jun.moiso.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jun.moiso.MyApplication;
import com.jun.moiso.R;
import com.jun.moiso.adapter.KeyboardAdapter;
import com.jun.moiso.database.KeyboardDB;
import com.jun.moiso.databinding.ActivityKeyboardListBinding;
import com.jun.moiso.model.CustomKeyboard;
import com.jun.moiso.viewmodel.KeyboardListViewModel;

public class KeyboardListActivity extends AppCompatActivity {

    private static KeyboardAdapter keyboardAdapter;
    ActivityKeyboardListBinding binding;

    private MyApplication myApplication;
    private KeyboardDB keyboardDB;

    private static KeyboardListActivity activity;
    private static KeyboardListViewModel viewModel;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_keyboard_list);

        viewModel = ViewModelProviders.of(this).get(KeyboardListViewModel.class);
        binding.setViewModel(viewModel);

        myApplication = (MyApplication)getApplication();
        keyboardDB = KeyboardDB.getInstance(this);

        viewModel.setItem_list(keyboardDB.selectCustomOf(myApplication.getUser_id()));

        context = KeyboardListActivity.this;
        activity = this;
    }

    //viewmodel의 item list에 변경 생길 때마다 호출
    @BindingAdapter("items")
    public static void setItems(RecyclerView recyclerView, ObservableArrayList<CustomKeyboard> customKeyboards)
    {
        //Recyclerview 초기화
        if(recyclerView.getAdapter() == null)
        {
            //구분선 적용
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);

            //adapter 적용
            keyboardAdapter = new KeyboardAdapter(context, viewModel, activity);
            recyclerView.setAdapter(keyboardAdapter);
        }
        else
            keyboardAdapter = (KeyboardAdapter)recyclerView.getAdapter();

        keyboardAdapter.setCustomKeyboardList(customKeyboards);//item list 적용
    }

    //커스텀 키보드 추가 리스너
    public void addCustomOnClikc(View v)
    {
        //키보드 커스텀 이름 이력 후 생성하는 액티비티
        View dialogView = getLayoutInflater().inflate(R.layout.create_dialog, null);
        final EditText editText = (EditText) dialogView.findViewById(R.id.edittext_dialog);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("커스텀 키보드 추가").setMessage("추가할 커스텀 키보드의 이름을 입력하세요.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //키보드 커스텀 생성 후 추가
                viewModel.addItem(keyboardDB.insertCustom(editText.getText().toString(), myApplication.getUser_id()));
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();;
    }
}