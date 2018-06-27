package com.example.jens.splittinit.activities;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jens.splittinit.R;

public class GroupView extends AppCompatActivity {

    public TextView groupName;
    public ImageView groupImage;

    @Override
    public void onStart(){
        super.onStart();

        setContentView(R.layout.group_view);

        groupImage = (ImageView) findViewById(R.id.groupImage);
        groupName = (TextView) findViewById(R.id.groupName);


    }
}
