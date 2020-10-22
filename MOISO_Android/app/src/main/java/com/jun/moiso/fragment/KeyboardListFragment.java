package com.jun.moiso.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.jun.moiso.MyApplication;
import com.jun.moiso.R;
import com.jun.moiso.adapter.KeyboardListActivityAdapter;
import com.jun.moiso.adapter.KeyboardListFragmentAdapter;
import com.jun.moiso.database.KeyboardDB;
import com.jun.moiso.databinding.FragmentKeyboardListBinding;
import com.jun.moiso.interfaces.KeyboardList;
import com.jun.moiso.model.CustomKeyboard;
import com.jun.moiso.viewmodel.KeyboardListFragmentViewModel;

import java.util.Arrays;
import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class KeyboardListFragment extends Fragment implements KeyboardList {

    private static KeyboardListFragmentAdapter keyboardAdapter;
    FragmentKeyboardListBinding binding;

    private MyApplication myApplication;
    private KeyboardDB keyboardDB;


    private static KeyboardListFragmentViewModel viewModel;
    private static Context context;


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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_keyboard_list, container, false);
        v = binding.getRoot();

        viewModel = ViewModelProviders.of(this).get(KeyboardListFragmentViewModel.class);
        binding.setViewModel(viewModel);

        myApplication = (MyApplication)getActivity().getApplication();
        keyboardDB = KeyboardDB.getInstance(getContext());

        ImageButton addCustom_btn = v.findViewById(R.id.keyboardcustom_add_btn_keyboardlist_fragment);
        addCustom_btn.setOnClickListener( view -> {

                //키보드 커스텀 이름 이력 후 생성하는 액티비티
                View dialogView = getLayoutInflater().inflate(R.layout.create_dialog, null);
                final EditText editText = dialogView.findViewById(R.id.edittext_dialog);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(dialogView);
                builder.setTitle("커스텀 키보드 추가").setMessage("추가할 커스텀 키보드의 이름을 입력하세요.");
                builder.setPositiveButton("확인", (dialogInterface, i) ->{
                    new Thread(() -> viewModel.addItem(keyboardDB.insertCustom(editText.getText().toString(), myApplication.getUser_id()))).start();
                });

                builder.setNegativeButton("취소", null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();;
        });

        context = getContext();

        return v;
    }

    @Override
    public void onStart() {

        renewalList();
        super.onStart();
    }


    //viewmodel의 item list에 변경 생길 때마다 호출
    @BindingAdapter("items_fragment")
    public static void setItems(RecyclerView recyclerView, ObservableArrayList<CustomKeyboard> customKeyboards)
    {
        //Recyclerview 초기화
        if(recyclerView.getAdapter() == null)
        {
            //구분선 적용
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);

            //adapter 적용
            keyboardAdapter = new KeyboardListFragmentAdapter(context, viewModel);
            recyclerView.setAdapter(keyboardAdapter);
        }
        else
            keyboardAdapter = (KeyboardListFragmentAdapter)recyclerView.getAdapter();

        keyboardAdapter.setCustomKeyboardList(customKeyboards);//item list 적용
    }

    @Override
    public void renewalList()
    {
        Observable.create(e -> {

            e.onNext(keyboardDB.selectCustomOf(myApplication.getUser_id()));

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {

                    ObservableArrayList<CustomKeyboard> customKeyboards = (ObservableArrayList<CustomKeyboard>)o;

                    viewModel.setItem_list(customKeyboards);
                    if (keyboardAdapter != null)
                        keyboardAdapter.setCustomKeyboardList(viewModel.getItem_list());
                });
    }

}