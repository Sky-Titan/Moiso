package com.jun.moiso.viewmodel;

import androidx.databinding.ObservableArrayList;

import com.jun.moiso.item.GroupListItem;
import com.jun.moiso.item.MemberListItem;

public class MemberViewModel {

    private ObservableArrayList<MemberListItem> item_list = new ObservableArrayList<>();
    private int remove_position = 0;//삭제된 아이템 위치
    private int add_position = 0;//추가된 아이템 위치
    private boolean isRemove = false;//아이템 삭제 여부
    private boolean isAdd = false;//아이템 추가 여부
    private boolean isUpdate = false;//아이템 업데이트 여부

    public MemberViewModel() {

    }

    public int getRemove_position() {
        return remove_position;
    }

    public void setRemove_position(int remove_position) {
        this.remove_position = remove_position;
    }

    public int getAdd_position() {
        return add_position;
    }

    public void setAdd_position(int add_position) {
        this.add_position = add_position;
    }

    public boolean isRemove() {
        return isRemove;
    }

    public void setRemove(boolean remove) {
        isRemove = remove;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public void addItem(MemberListItem memberListItem)
    {
        item_list.add(memberListItem);
        isAdd = true;
        add_position = item_list.size()-1;
    }

    public void removeItem(int position)
    {
        item_list.remove(position);
        isRemove = true;
        remove_position = position;
    }

    public ObservableArrayList<MemberListItem> getItem_list() {
        return item_list;
    }

    public void setItem_list(ObservableArrayList<MemberListItem> item_list) {
        this.item_list = item_list;
    }
}
