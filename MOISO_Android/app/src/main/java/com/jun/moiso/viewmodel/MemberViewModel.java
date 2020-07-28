package com.jun.moiso.viewmodel;

import androidx.databinding.ObservableArrayList;

import com.jun.moiso.item.GroupListItem;
import com.jun.moiso.item.MemberListItem;

public class MemberViewModel {

    private ObservableArrayList<MemberListItem> item_list = new ObservableArrayList<>();

    public MemberViewModel() {

    }

    public void addItem(MemberListItem memberListItem)
    {
        item_list.add(memberListItem);
    }

    public void removeItem(int position)
    {
        item_list.remove(position);
    }

    public ObservableArrayList<MemberListItem> getItem_list() {
        return item_list;
    }

    public void setItem_list(ObservableArrayList<MemberListItem> item_list) {
        this.item_list = item_list;
    }
}
