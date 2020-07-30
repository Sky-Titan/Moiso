package com.jun.moiso.adapter;

import android.content.Context;
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
import com.jun.moiso.databinding.KeyboardlistItemBinding;
import com.jun.moiso.model.KeyboardListItem;
import com.jun.moiso.viewmodel.KeyboardListViewModel;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class KeyboardAdapter extends RecyclerView.Adapter<KeyboardAdapter.KeyboardViewHolder<KeyboardlistItemBinding>> {

    private static String TAG = "KeyboardAdapter";

    private KeyboardListViewModel keyboardListViewModel;

    private int lastPosition = 0; //item list의 변경전 크기를 나타낸다.

    private ObservableArrayList<KeyboardListItem> keyboardListItems = new ObservableArrayList<>() ;
    private Context context;

    public KeyboardAdapter(Context context, KeyboardListViewModel keyboardListViewModel) {
        this.context = context;
        this.keyboardListViewModel = keyboardListViewModel;
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

        holder.binding().setItem(keyboardListItems.get(position));

        //그룹 삭제 버튼 리스너
        ImageButton delete = (ImageButton) holder.itemView.findViewById(R.id.keyboarddelete_btn_item);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO : 서버에서 삭제 작업

                //삭제 도중 중복 클릭 방지

                delelteAnimation(holder.itemView, view,holder.getAdapterPosition());

            }
        });

        createAnimation(holder.itemView,position);
    }


    //아이템 추가 애니메이션
    private void createAnimation(View viewToAnimate, int position) {
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
    private void delelteAnimation(final View viewToAnimate, final View delete_btn, final int position) {

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.alpha_remove);
        animation.setInterpolator(new DecelerateInterpolator());
        viewToAnimate.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //item list에서 제거
                lockClickable(viewToAnimate, delete_btn);
                keyboardListViewModel.removeItem(position);
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

    private void lockClickable(View item, View delete_btn)
    {
        item.setClickable(false);
        delete_btn.setClickable(false);
    }
    private void unlockClickable(View item, View delete_btn)
    {
        item.setClickable(true);
        delete_btn.setClickable(true);
    }

    @Override
    public int getItemCount() {
        return keyboardListItems.size();
    }

    public ObservableArrayList<KeyboardListItem> getGroupListItems() {
        return keyboardListItems;
    }

    //item list 변경 반영
    public void setKeyboardListItems(ObservableArrayList<KeyboardListItem>  keyboardListItems) {
        //this.groupListItems = groupListItems;
        this.keyboardListItems.clear();
        this.keyboardListItems.addAll(keyboardListItems);

        notifyItemChanged();
    }

    //아이템 변경 알림
    public void notifyItemChanged()
    {
        //item이 추가 되었는지, 삭제 되었는지 파악
        if(keyboardListViewModel.isAdd())
        {
            notifyItemInserted(keyboardListViewModel.getAdd_position());
            keyboardListViewModel.setAdd(false);
        }
        else if(keyboardListViewModel.isRemove())
        {
            notifyItemRemoved(keyboardListViewModel.getRemove_position());
            keyboardListViewModel.setRemove(false);
        }
        else//todo : 내용 업데이트
        {

        }
    }

    class KeyboardViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder{
        private final T binding;

        public KeyboardViewHolder(final View v){
            super(v);
            this.binding = (T) DataBindingUtil.bind(v);
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO : item 포지션에 따른 내용 삽입 수정
                    Intent intent = new Intent(context, KeyboardCustomActivity.class);
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);


                }
            });

        }

        public T binding() {
            return binding;
        }
    }
}
