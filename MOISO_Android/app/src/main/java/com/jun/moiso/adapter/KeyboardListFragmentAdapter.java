package com.jun.moiso.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.ListAdapter;

import com.jun.moiso.R;
import com.jun.moiso.activity.KeyboardCustomActivity;
import com.jun.moiso.database.MyViewHolder;
import com.jun.moiso.databinding.KeyboardlistItemBinding;
import com.jun.moiso.model.CustomKeyboard;

import com.jun.moiso.viewmodel.KeyboardListFragmentViewModel;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class KeyboardListFragmentAdapter extends ListAdapter<CustomKeyboard, MyViewHolder<KeyboardlistItemBinding>> {

    private static String TAG = "KeyboardListFragmentAdapter";

    private KeyboardListFragmentViewModel viewModel;

    private Context context;

    public KeyboardListFragmentAdapter(Context context, KeyboardListFragmentViewModel viewModel) {
        super(CustomKeyboard.DIFF_CALLBACK);

        this.context = context;
        this.viewModel = viewModel;

    }

    @Override
    public int getItemCount() {
        return getCurrentList().size();
    }

    @NonNull
    @Override
    public MyViewHolder<KeyboardlistItemBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder");

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder<>(inflater.inflate(R.layout.keyboardlist_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder<KeyboardlistItemBinding> holder, int position) {
        Log.i(TAG, "onBindViewHolder");

        holder.binding().setItem(getCurrentList().get(position));

        final String custom_name = holder.binding().getItem().custom_name;
        final int custom_id = holder.binding().getItem().custom_id;


        holder.binding().getRoot().setOnClickListener(view -> {

                Intent intent = new Intent(context, KeyboardCustomActivity.class);
                intent.putExtra("custom_id", custom_id);
                intent.putExtra("custom_name", custom_name);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
        });


        //삭제 버튼 리스너
        ImageButton delete = holder.itemView.findViewById(R.id.keyboarddelete_btn_item);
        delete.setOnClickListener(view ->  {
                //db에서 삭제 작업
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle(custom_name+" 삭제").setMessage("정말로 삭제하시겠습니까?");
                builder.setPositiveButton("확인",  (dialogInterface, i) -> {
                    //삭제 처리
                    viewModel.deleteCustomKeyboard(custom_id);
                });
                builder.setNegativeButton("취소", null);

                AlertDialog dialog = builder.create();
                dialog.show();
        });

    }



}