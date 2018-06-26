package com.example.jens.splittinit.model;

import java.util.ArrayList;
import java.util.List;

public class Group {


    private List<String> memberIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;


    public Group(List<String> memberIds, String name) {
        this.memberIds = memberIds;
        this.name = name;
    }

    public Group(){

    }
    public Group(String name){
        this.name = name;
        memberIds = new ArrayList<>();
    }


    public List<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<String> memberIds) {
        this.memberIds = memberIds;
    }
}
