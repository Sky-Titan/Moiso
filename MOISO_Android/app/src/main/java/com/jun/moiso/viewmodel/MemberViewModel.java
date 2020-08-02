package com.jun.moiso.viewmodel;

import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModel;

import com.jun.moiso.model.MemberListItem;

public class MemberViewModel extends ViewModel {

    private ObservableArrayList<MemberListItem> item_list = new ObservableArrayList<>();
    private int remove_position = 0;//삭제된 아이템 위치
    private int add_position = 0;//추가된 아이템 위치
    private boolean isRemove = false;//아이템 삭제 여부
    private boolean isAdd = false;//아이템 추가 여부
    private boolean isUpdate = false;//아이템 업데이트 여부


    private String user_name, grop_name;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getGrop_name() {
        return grop_name;
    }

    public void setGrop_name(String grop_name) {
        this.grop_name = grop_name;
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
