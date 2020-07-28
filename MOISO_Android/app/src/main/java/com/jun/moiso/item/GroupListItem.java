package com.jun.moiso.item;

public class GroupListItem {

    private String group_name="";
    private String group_id="";
    private String group_leader="";


    public GroupListItem()
    {

    }
    public GroupListItem(String group_name)
    {
        this.group_name = group_name;
    }
    public GroupListItem(String group_name, String group_id)
    {
        this.group_name = group_name;
        this.group_id = group_id;
    }
    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }
}
