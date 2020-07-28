package com.jun.moiso.adapter;

import android.content.Context;
import android.content.Intent;
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

    private GroupViewModel groupViewModel;

    private int lastPosition = 0; //item list의 변경전 크기를 나타낸다.

    private ObservableArrayList<GroupListItem> groupListItems = new ObservableArrayList<>();
    private Context context;

    public GroupAdapter(Context context, GroupViewModel groupViewModel) {
        this.context = context;
        this.groupViewModel = groupViewModel;
    }

    @NonNull
    @Override
    public GroupViewHolder<GrouplistItemBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new GroupViewHolder<>(inflater.inflate(R.layout.grouplist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupViewHolder<GrouplistItemBinding> holder, final int position) {
        holder.binding().setItem(groupListItems.get(position));

        //그룹 삭제 버튼 리스너
        ImageButton delete = (ImageButton) holder.itemView.findViewById(R.id.groupdelete_btn_item);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO : 서버에서 삭제 작업
                delelteAnimation(holder.itemView,position);

            }
        });

        createAnimation(holder.itemView,position);
    }

    //아이템 추가 애니메이션
    private void createAnimation(View viewToAnimate, int position) {

        //새로 생성된 item에 한해서만 animation 실행
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.alpha_create);
            animation.setInterpolator(new DecelerateInterpolator());
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    //아이템 삭제 애니메이션
    private void delelteAnimation(View viewToAnimate, final int position) {

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.alpha_remove);
        animation.setInterpolator(new DecelerateInterpolator());
        viewToAnimate.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //item list에서 제거
                groupViewModel.removeItem(position);
                lastPosition--;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return groupListItems.size();
    }

    public ObservableArrayList<GroupListItem> getGroupListItems() {
        return groupListItems;
    }

    public void setGroupListItems(ObservableArrayList<GroupListItem> groupListItems) {
        this.groupListItems.clear();
        this.groupListItems.addAll(groupListItems);

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
