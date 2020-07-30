package com.jun.moiso.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.jun.moiso.R;
import com.jun.moiso.adapter.KeyboardAdapter;
import com.jun.moiso.databinding.ActivityKeyboardListBinding;
import com.jun.moiso.model.KeyboardListItem;
import com.jun.moiso.viewmodel.KeyboardListViewModel;

public class KeyboardListActivity extends AppCompatActivity {

    private static KeyboardAdapter keyboardAdapter;
    ActivityKeyboardListBinding binding;

    private static KeyboardListViewModel viewModel;
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_keyboard_list);
        viewModel = new KeyboardListViewModel();
        binding.setViewModel(viewModel);

        //TODO : 나중에 삭제
        plusOnclick();

        context = getApplicationContext();
    }

    //viewmodel의 item list에 변경 생길 때마다 호출
    @BindingAdapter("items")
    public static void setItems(RecyclerView recyclerView, ObservableArrayList<KeyboardListItem> keyboardListItems)
    {
        //Recyclerview 초기화
        if(recyclerView.getAdapter() == null)
        {
            //구분선 적용
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);

            //adapter 적용
            keyboardAdapter = new KeyboardAdapter(context, viewModel);
            recyclerView.setAdapter(keyboardAdapter);
        }
        else
            keyboardAdapter = (KeyboardAdapter)recyclerView.getAdapter();

        keyboardAdapter.setKeyboardListItems(keyboardListItems);//item list 적용
    }

    //TODO : 추가 테스트 후에 삭제
    public void plusOnclick()
    {
        viewModel.addItem(new KeyboardListItem("test"));
        viewModel.addItem(new KeyboardListItem("test"));
        viewModel.addItem(new KeyboardListItem("test"));


        ImageButton keyboardcustom_add_btn = (ImageButton) findViewById(R.id.keyboardcustom_add_btn_keyboardlist);
        keyboardcustom_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.addItem(new KeyboardListItem("test"));
            }
        });
    }
}