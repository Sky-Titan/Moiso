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
import com.jun.moiso.activity.GroupActivity;
import com.jun.moiso.databinding.GrouplistItemBinding;
import com.jun.moiso.item.GroupListItem;
import com.jun.moiso.viewmodel.GroupViewModel;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder<GrouplistItemBinding>> {

    private static String TAG = "GroupAdapter";

    private GroupViewModel groupViewModel;

    private int lastPosition = 0; //item list의 변경전 크기를 나타낸다.

    private ObservableArrayList<GroupListItem> groupListItems = new ObservableArrayList<>() ;
    private Context context;

    public GroupAdapter(Context context, GroupViewModel groupViewModel) {
        this.context = context;
        this.groupViewModel = groupViewModel;
    }

    @NonNull
    @Override
    public GroupViewHolder<GrouplistItemBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder");

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new GroupViewHolder<>(inflater.inflate(R.layout.grouplist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupViewHolder<GrouplistItemBinding> holder, int position) {
        Log.i(TAG, "onBindViewHolder");

        holder.binding().setItem(groupListItems.get(position));

        //그룹 삭제 버튼 리스너
        ImageButton delete = (ImageButton) holder.itemView.findViewById(R.id.groupdelete_btn_item);
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
                groupViewModel.removeItem(position);
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
        return groupListItems.size();
    }

    public ObservableArrayList<GroupListItem> getGroupListItems() {
        return groupListItems;
    }

    //item list 변경 반영
    public void setGroupListItems(ObservableArrayList<GroupListItem> groupListItems) {
        //this.groupListItems = groupListItems;
        this.groupListItems.clear();
        this.groupListItems.addAll(groupListItems);

        notifyItemChanged();
    }

    //아이템 변경 알림
    public void notifyItemChanged()
    {
        //item이 추가 되었는지, 삭제 되었는지 파악
        if(groupViewModel.isAdd())
        {
            notifyItemInserted(groupViewModel.getAdd_position());
            groupViewModel.setAdd(false);
        }
        else if(groupViewModel.isRemove())
        {
            notifyItemRemoved(groupViewModel.getRemove_position());
            groupViewModel.setRemove(false);
        }
        else//todo : 내용 업데이트
        {

        }
    }

    class GroupViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder{
        private final T binding;

        public GroupViewHolder(final View v){
            super(v);
            this.binding = (T) DataBindingUtil.bind(v);
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO : item 포지션에 따른 내용 삽입 수정
                    Intent intent = new Intent(context, GroupActivity.class);
                    context.startActivity(intent);

                }
            });

        }

        public T binding() {
            return binding;
        }
    }
}
