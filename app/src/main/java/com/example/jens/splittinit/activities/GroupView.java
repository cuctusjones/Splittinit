package com.example.jens.splittinit.activities;

import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jens.splittinit.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupView extends AppCompatActivity {

    public TextView groupName;
    public CircleImageView groupImage;

    @Override
    public void onStart(){
        super.onStart();

        setContentView(R.layout.group_view);

        groupImage = findViewById(R.id.profileImage);
        groupName = (TextView) findViewById(R.id.groupName);


    }
}
