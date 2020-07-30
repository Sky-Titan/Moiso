package com.jun.moiso.model;

public class MemberListItem {

    private String memeber_nickname, member_id;
    private boolean isLeader = false;

    public MemberListItem() {
    }

    public MemberListItem(String member_id) {
        this.member_id = member_id;
    }

    public MemberListItem(String member_id, String memeber_nickname) {
        this.memeber_nickname = memeber_nickname;
        this.member_id = member_id;
    }

    public MemberListItem(String memeber_nickname, String member_id, boolean isLeader) {
        this.memeber_nickname = memeber_nickname;
        this.member_id = member_id;
        this.isLeader = isLeader;
    }

    public String getMemeber_nickname() {
        return memeber_nickname;
    }

    public void setMemeber_nickname(String memeber_nickname) {
        this.memeber_nickname = memeber_nickname;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public void setLeader(boolean leader) {
        isLeader = leader;
    }
}
