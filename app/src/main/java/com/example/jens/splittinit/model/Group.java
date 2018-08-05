package com.example.jens.splittinit.model;

import java.util.ArrayList;
import java.util.List;

public class Group {


    private ArrayList<String> members;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;


    public Group(ArrayList<String> members, String name) {
        this.members = members;
        this.name = name;
    }

    public Group(){

    }
    public Group(String name){
        this.name = name;
        members = new ArrayList<>();
    }


    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }
}
