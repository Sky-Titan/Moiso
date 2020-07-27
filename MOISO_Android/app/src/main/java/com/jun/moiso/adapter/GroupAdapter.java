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
import com.jun.moiso.databinding.GrouplistItemBinding;
import com.jun.moiso.item.GroupListItem;
import com.jun.moiso.viewmodel.GroupViewModel;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder<GrouplistItemBinding>> {

    private GroupViewModel groupViewModel;
    private int lastPosition = 0;
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

        }

        public T binding() {
            return binding;
        }
    }
}
