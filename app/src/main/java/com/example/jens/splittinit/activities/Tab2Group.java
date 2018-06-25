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
    private ArrayList<Integer> groupImg;
    private ArrayList<String> groupName;

    public ConstraintLayout constraintLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.tab2group, container, false);

        //initializing stuff
        initialize(rootView);
        //updateViews();


        return rootView;
    }

    private void updateViews() {

        groupName.add("test1");
        groupName.add("test1");

        groupImg.add(R.drawable.check_split);
        groupImg.add(R.drawable.common_google_signin_btn_icon_dark);

        Integer[] stockArr = new Integer[groupImg.size()];
        stockArr = groupImg.toArray(stockArr);

        String[] stockArr2 = new String[groupName.size()];
        stockArr2 = groupName.toArray(stockArr2);

        CustomList adapter = new CustomList(getActivity(), stockArr2, stockArr);
        list.setAdapter(adapter);
        }


    private void initialize(View v) {

        constraintLayout = (ConstraintLayout) v.getRootView().findViewById(R.id.constraintLayout);
        list = (ListView) v.getRootView().findViewById(R.id.list);



    }

}
