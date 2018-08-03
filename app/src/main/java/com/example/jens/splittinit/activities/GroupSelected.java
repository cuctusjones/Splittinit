package com.example.jens.splittinit.activities;

import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jens.splittinit.R;

public class GroupSelected extends AppCompatActivity {

    public TextView groupName, debt;
    public ImageView groupImage;
    public ListView listOfMembers, log;


    @Override
    public void onStart() {
        super.onStart();

        initialize();




    }

    private void initialize() {
        setContentView(R.layout.group_intern_view);

        groupImage = findViewById(R.id.groupImage);
        groupName = findViewById(R.id.groupName);
        debt = findViewById(R.id.debt);
        listOfMembers = findViewById(R.id.memberList);
        log = findViewById(R.id.log);
    }

    @Override
    public void onResume(){
        super.onResume();

    }
}
