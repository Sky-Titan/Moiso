package com.jun.moiso.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.jun.moiso.R;
import com.jun.moiso.databinding.MemberlistItemBinding;
import com.jun.moiso.item.MemberListItem;
import com.jun.moiso.viewmodel.MemberViewModel;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder<MemberlistItemBinding>> {

    private MemberViewModel memberViewModel;
    private int lastPosition = 0;
    private ObservableArrayList<MemberListItem> memberListItems = new ObservableArrayList<>();
    private Context context;

    public MemberAdapter(Context context, MemberViewModel memberViewModel) {
        this.memberViewModel = memberViewModel;
        this.context = context;
    }

    @NonNull
    @Override
    public MemberViewHolder<MemberlistItemBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MemberViewHolder<>(inflater.inflate(R.layout.memberlist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder<MemberlistItemBinding> holder, int position) {
        holder.binding().setItem(memberListItems.get(position));


        createAnimation(holder.itemView,position);
    }

    //아이템 추가 애니메이션
    private void createAnimation(View viewToAnimate, int position) {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.alpha_create);
            animation.setInterpolator(new DecelerateInterpolator());
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public ObservableArrayList<MemberListItem> getMemberListItems() {
        return memberListItems;
    }

    public void setMemberListItems(ObservableArrayList<MemberListItem> memberListItems) {
        this.memberListItems.clear();
        this.memberListItems.addAll(memberListItems);
    }

    @Override
    public int getItemCount() {
        return memberListItems.size();
    }

    class MemberViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder{
        private final T binding;

        public MemberViewHolder(final View v){
            super(v);
            this.binding = (T) DataBindingUtil.bind(v);
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO : item 포지션에 따른 내용 삽입 수정

                }
            });

        }

        public T binding() {
            return binding;
        }
    }
}
