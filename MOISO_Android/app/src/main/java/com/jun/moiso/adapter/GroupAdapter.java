package com.jun.moiso.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.jun.moiso.R;
import com.jun.moiso.databinding.GrouplistItemBinding;
import com.jun.moiso.item.GroupListItem;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder<GrouplistItemBinding>> {

    private ObservableArrayList<GroupListItem> groupListItems = new ObservableArrayList<>();

    @NonNull
    @Override
    public GroupViewHolder<GrouplistItemBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new GroupViewHolder<>(inflater.inflate(R.layout.grouplist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder<GrouplistItemBinding> holder, int position) {
        holder.binding().setItem(groupListItems.get(position));
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
        notifyDataSetChanged();//중요!!
    }

    class GroupViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder{
        private final T binding;

        public GroupViewHolder(View v){
            super(v);
            this.binding = (T) DataBindingUtil.bind(v);
        }

        public T binding() {
            return binding;
        }
    }
}
