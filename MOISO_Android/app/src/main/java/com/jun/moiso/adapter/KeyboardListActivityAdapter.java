package com.jun.moiso.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.jun.moiso.R;
import com.jun.moiso.activity.KeyboardCustomActivity;
import com.jun.moiso.activity.KeyboardListActivity;
import com.jun.moiso.database.KeyboardDB;
import com.jun.moiso.databinding.KeyboardlistItemBinding;

import com.jun.moiso.interfaces.KeyboardListAdapter;
import com.jun.moiso.model.CustomKeyboard;

import com.jun.moiso.viewmodel.KeyboardListActivityViewModel;


import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class KeyboardListActivityAdapter extends RecyclerView.Adapter<KeyboardListActivityAdapter.KeyboardViewHolder<KeyboardlistItemBinding>> implements KeyboardListAdapter {

    private static String TAG = "KeyboardListActivityAdapter";

    private KeyboardListActivityViewModel keyboardListActivityViewModel;

    private KeyboardListActivity activity;


    private int lastPosition = 0; //item list의 변경전 크기를 나타낸다.
    private KeyboardDB keyboardDB;
    private ObservableArrayList<CustomKeyboard> customKeyboards = new ObservableArrayList<>() ;
    private Context context;

    public KeyboardListActivityAdapter(Context context, KeyboardListActivityViewModel keyboardListActivityViewModel, KeyboardListActivity activity) {
        this.context = context;
        this.keyboardListActivityViewModel = keyboardListActivityViewModel;
        this.activity = activity;
        keyboardDB = KeyboardDB.getInstance(context);
    }

    @NonNull
    @Override
    public KeyboardViewHolder<KeyboardlistItemBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder");

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new KeyboardViewHolder<>(inflater.inflate(R.layout.keyboardlist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final KeyboardViewHolder<KeyboardlistItemBinding> holder, int position) {
        Log.i(TAG, "onBindViewHolder");

        holder.binding().setItem(customKeyboards.get(position));

        String custom_name = holder.binding().getItem().getCustom_name();
        int custom_id = holder.binding().getItem().getCustom_id();
        String owner_id = holder.binding().getItem().getOwner_id();

        holder.binding().getRoot().setOnClickListener(view ->  {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle(custom_name).setMessage("작업을 선택해주세요.");
                    builder.setNegativeButton("사용",(dialogInterface, i) -> {

                            Intent intent = new Intent();
                            intent.putExtra("custom_id", custom_id);
                            intent.putExtra("custom_name", custom_name);
                            intent.putExtra("owner_id", owner_id);
                            activity.setResult(Activity.RESULT_OK, intent);
                            activity.finish();
                    });

                    builder.setPositiveButton("버튼 편집", (dialogInterface, i) -> {

                            Intent intent = new Intent(context, KeyboardCustomActivity.class);
                            intent.putExtra("custom_id", custom_id);
                            intent.putExtra("custom_name", custom_name);
                            intent.putExtra("owner_id", owner_id);
                            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
            });



        //삭제 버튼 리스너
        ImageButton delete = holder.itemView.findViewById(R.id.keyboarddelete_btn_item);
        delete.setOnClickListener(view -> {

                //db에서 삭제 작업
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle(custom_name+" 삭제").setMessage("정말로 삭제하시겠습니까?");
                builder.setPositiveButton("확인", (dialogInterface, i) -> {
                        //삭제 처리
                        new Thread(() -> keyboardDB.deleteCustom(custom_id)).start();

                        //삭제 도중 중복 클릭 방지
                        delelteAnimation(holder.itemView, view, holder.getAdapterPosition());
                });
                builder.setNegativeButton("취소", null);

                AlertDialog dialog = builder.create();
                dialog.show();
        });

        createAnimation(holder.itemView,position);
    }


    //아이템 추가 애니메이션
    @Override
    public void createAnimation(View viewToAnimate, int position) {
        Log.i(TAG, "createAnimation");

        //새로 생성된 item에 한해서만 animation 실행
        if (position > lastPosition)
        {
            Log.d(TAG, "create Animation 실행");

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.alpha_create);
            animation.setInterpolator(new DecelerateInterpolator());
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    //아이템 삭제 애니메이션
    @Override
    public void delelteAnimation(final View viewToAnimate, final View delete_btn, final int position) {

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.alpha_remove);
        animation.setInterpolator(new DecelerateInterpolator());
        viewToAnimate.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //item list에서 제거
                lockClickable(viewToAnimate, delete_btn);
                keyboardListActivityViewModel.removeItem(position);
                lastPosition--;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                unlockClickable(viewToAnimate, delete_btn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    public void lockClickable(View item, View delete_btn)
    {
        item.setClickable(false);
        delete_btn.setClickable(false);
    }

    @Override
    public void unlockClickable(View item, View delete_btn)
    {
        item.setClickable(true);
        delete_btn.setClickable(true);
    }

    @Override
    public int getItemCount() {
        return customKeyboards.size();
    }


    //item list 변경 반영
    @Override
    public void setCustomKeyboardList(ObservableArrayList<CustomKeyboard>  customKeyboards) {
        //this.groupListItems = groupListItems;
        this.customKeyboards.clear();
        this.customKeyboards.addAll(customKeyboards);

        notifyItemChanged();
    }

    //아이템 변경 알림
    @Override
    public void notifyItemChanged()
    {
        //item이 추가 되었는지, 삭제 되었는지 파악
        if(keyboardListActivityViewModel.isAdd())
        {
            notifyItemInserted(keyboardListActivityViewModel.getAdd_position());
            keyboardListActivityViewModel.setAdd(false);
        }
        else if(keyboardListActivityViewModel.isRemove())
        {
            notifyItemRemoved(keyboardListActivityViewModel.getRemove_position());
            keyboardListActivityViewModel.setRemove(false);
        }
        else//내용 업데이트
        {
            notifyDataSetChanged();
            keyboardListActivityViewModel.setUpdate(false);
        }
    }

    class KeyboardViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder{
        private final T binding;

        public KeyboardViewHolder(final View v){
            super(v);
            this.binding = (T) DataBindingUtil.bind(v);

        }

        public T binding() {
            return binding;
        }
    }
}
