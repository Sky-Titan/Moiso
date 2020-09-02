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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.jun.moiso.MyApplication;
import com.jun.moiso.R;
import com.jun.moiso.adapter.KeyboardListActivityAdapter;
import com.jun.moiso.database.KeyboardDB;
import com.jun.moiso.databinding.ActivityKeyboardListBinding;
import com.jun.moiso.interfaces.KeyboardList;
import com.jun.moiso.model.CustomKeyboard;
import com.jun.moiso.viewmodel.KeyboardListActivityViewModel;

public class KeyboardListActivity extends AppCompatActivity implements KeyboardList {

    private static KeyboardListActivityAdapter keyboardAdapter;
    ActivityKeyboardListBinding binding;

    private MyApplication myApplication;
    private KeyboardDB keyboardDB;

    private static KeyboardListActivity activity;
    private static KeyboardListActivityViewModel viewModel;
    private static Context context;

    private static final String TAG = "KeyboardListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_keyboard_list);

        viewModel = ViewModelProviders.of(this).get(KeyboardListActivityViewModel.class);
        binding.setViewModel(viewModel);

        myApplication = (MyApplication)getApplication();
        keyboardDB = KeyboardDB.getInstance(this);

        context = KeyboardListActivity.this;
        activity = this;
    }

    @Override
    public void onStart() {

        Log.d(TAG, "onStart");
        renewalList();
        super.onStart();
    }

    //viewmodel의 item list에 변경 생길 때마다 호출
    @BindingAdapter("items_activity")
    public static void setItems(RecyclerView recyclerView, ObservableArrayList<CustomKeyboard> customKeyboards)
    {
        //Recyclerview 초기화
        if(recyclerView.getAdapter() == null)
        {
            //구분선 적용
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);

            //adapter 적용
            keyboardAdapter = new KeyboardListActivityAdapter(context, viewModel, activity);
            recyclerView.setAdapter(keyboardAdapter);
        }
        else
            keyboardAdapter = (KeyboardListActivityAdapter)recyclerView.getAdapter();

        keyboardAdapter.setCustomKeyboardList(customKeyboards);//item list 적용
    }

    @Override
    public void renewalList()
    {
        new AsyncTask<Void, Void, ObservableArrayList<CustomKeyboard>>(){
            @Override
            protected void onPostExecute(ObservableArrayList<CustomKeyboard> customKeyboards) {
                super.onPostExecute(customKeyboards);

                viewModel.setItem_list(customKeyboards);
                if(keyboardAdapter != null)
                    keyboardAdapter.setCustomKeyboardList(viewModel.getItem_list());

            }

            @Override
            protected ObservableArrayList<CustomKeyboard> doInBackground(Void... voids) {
                return keyboardDB.selectCustomOf(myApplication.getUser_id());
            }
        }.execute();

    }

    //커스텀 키보드 추가 리스너
    public void addCustomOnClick(View v)
    {
        //키보드 커스텀 이름 이력 후 생성하는 액티비티
        View dialogView = getLayoutInflater().inflate(R.layout.create_dialog, null);
        final EditText editText = (EditText) dialogView.findViewById(R.id.edittext_dialog);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("커스텀 키보드 추가").setMessage("추가할 커스텀 키보드의 이름을 입력하세요.");
        builder.setPositiveButton("확인", (dialogInterface, i) ->{
            new Thread(() -> viewModel.addItem(keyboardDB.insertCustom(editText.getText().toString(), myApplication.getUser_id()))).start();
        });

        builder.setNegativeButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();;
    }
}