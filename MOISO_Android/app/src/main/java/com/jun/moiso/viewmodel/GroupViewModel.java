package com.jun.moiso.viewmodel;

import androidx.databinding.ObservableArrayList;

import com.jun.moiso.item.GroupListItem;


public class GroupViewModel {

    private ObservableArrayList<GroupListItem> item_list = new ObservableArrayList<>();

    public GroupViewModel()
    {

    }

    public void addItem(GroupListItem groupListItem)
    {
        item_list.add(groupListItem);
    }

    public void removeItem(int position)
    {
        item_list.remove(position);
    }
    public ObservableArrayList<GroupListItem> getItem_list() {
        return item_list;
    }

    public void setItem_list(ObservableArrayList<GroupListItem> item_list) {
        this.item_list = item_list;
    }
}
