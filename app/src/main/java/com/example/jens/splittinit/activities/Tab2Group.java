package com.example.jens.splittinit.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jens.splittinit.R;
import com.example.jens.splittinit.model.Expense;
import com.example.jens.splittinit.model.User;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Tab2Group extends Fragment {

    public ListView list;
    public ArrayList<Integer> groupImg;
    public ArrayList<String> groupName;


    public ConstraintLayout constraintLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.tab2group, container, false);

        //initializing stuff
        initialize(rootView);
        //updateViews();

        updateViews();


        return rootView;
    }

    private void updateViews() {

        groupImg = new ArrayList<Integer>();
        groupName = new ArrayList<String>();
        groupName.add("test1");
        groupName.add("test2");

        groupImg.add(R.drawable.check_split);
        groupImg.add(R.drawable.common_google_signin_btn_icon_dark);

        Integer[] groupImgArray = new Integer[groupImg.size()];
        groupImgArray = groupImg.toArray(groupImgArray);


        String[] groupNameArray = new String[groupName.size()];
        groupNameArray = groupName.toArray(groupNameArray);

        GroupList adapter = new GroupList(getActivity(), groupNameArray, groupImgArray);
        list.setAdapter(adapter);
        }


    private void initialize(View v) {

        constraintLayout = (ConstraintLayout) v.getRootView().findViewById(R.id.constraintLayout);
        list = (ListView) v.getRootView().findViewById(R.id.list);

    }

    public static void setImgArray(){

    }

}
