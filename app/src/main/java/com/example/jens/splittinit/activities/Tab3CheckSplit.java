package com.example.jens.splittinit.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jens.splittinit.R;

public class Tab3CheckSplit extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.tab3checksplit, container, false);

        //initializing stuff
        initialize(rootView);


        return rootView;
    }


    private void initialize(View v) {

    }

}
