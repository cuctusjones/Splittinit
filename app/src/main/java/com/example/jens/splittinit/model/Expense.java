package com.example.jens.splittinit.model;

public class Expense {

    int value;
    String friendid;

    public Expense(int value, String friendid) {
        this.value = value;
        this.friendid = friendid;
    }

    public Expense (){

    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getFriendid() {
        return friendid;
    }

    public void setFriendid(String friendid) {
        this.friendid = friendid;
    }
}
