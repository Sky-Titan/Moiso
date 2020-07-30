package com.jun.moiso.adapter;

import android.content.Context;
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
import com.jun.moiso.databinding.MemberlistItemBinding;
import com.jun.moiso.model.MemberListItem;
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
    public void onBindViewHolder(@NonNull final MemberViewHolder<MemberlistItemBinding> holder,final int position) {
        holder.binding().setItem(memberListItems.get(position));

        //삭제 버튼 리스너
        ImageButton delete = (ImageButton) holder.itemView.findViewById(R.id.memberdelete_btn_item);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO : 서버에서 삭제 작업
                delelteAnimation(holder.itemView, view, holder.getAdapterPosition());
            }
        });

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

    //아이템 삭제 애니메이션
    private void delelteAnimation(final View viewToAnimate, final View delete_btn , final int position) {

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.alpha_remove);
        animation.setInterpolator(new DecelerateInterpolator());
        viewToAnimate.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //item list에서 제거
                lockClickable(viewToAnimate, delete_btn);
                memberViewModel.removeItem(position);
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

    public ObservableArrayList<MemberListItem> getMemberListItems() {
        return memberListItems;
    }

    public void setMemberListItems(ObservableArrayList<MemberListItem> memberListItems) {
        this.memberListItems.clear();
        this.memberListItems.addAll(memberListItems);

        notifyItemChanged();
    }

    //아이템 변경 알림
    public void notifyItemChanged()
    {
        //item이 추가 되었는지, 삭제 되었는지 파악
        if(memberViewModel.isAdd())
        {
            notifyItemInserted(memberViewModel.getAdd_position());
            memberViewModel.setAdd(false);
        }
        else if(memberViewModel.isRemove())
        {
            notifyItemRemoved(memberViewModel.getRemove_position());
            memberViewModel.setRemove(false);
        }
        else//todo : 내용 업데이트
        {

        }
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

        }

        public T binding() {
            return binding;
        }
    }
}
