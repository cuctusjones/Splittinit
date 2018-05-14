package com.example.jens.splittinit.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jens.splittinit.R;


public class Tab1Duo extends Fragment {

    private ImageView person1;
    private ImageView person2;

    private TextView person1txt;
    private TextView person2txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.tab1duo, container, false);

        //initializing stuff
        initialize(rootView);


        return rootView;
    }


    private void initialize(View v) {

        //Imageviews
        person1 = (ImageView) v.getRootView().findViewById(R.id.person1);
        person2 = (ImageView) v.getRootView().findViewById(R.id.person2);

        person1txt = (TextView) v.getRootView().findViewById(R.id.person1txt);
        person2txt = (TextView) v.getRootView().findViewById(R.id.person2txt);

    }
}
