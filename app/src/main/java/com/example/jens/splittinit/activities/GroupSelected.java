package com.example.jens.splittinit.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jens.splittinit.R;
import com.example.jens.splittinit.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GroupSelected extends AppCompatActivity {

    public TextView groupName, debt;
    public ImageView groupImage;
    public ListView listOfMembers, log;
    public FloatingActionButton fab;

    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseAuth auth;
    private DataSnapshot myDataSnapshot;


    @Override
    public void onStart() {
        super.onStart();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        auth = FirebaseAuth.getInstance();

        initialize();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //change groupname accordingly
        //groupName.setText(myDataSnapshot.child("groups").child(Integer.toString(getIntent().getIntExtra("groupID", 0))).child("name").getValue(String.class));

    }


    @Override
    public void onResume(){
        super.onResume();

    }

    private void initialize() {
        setContentView(R.layout.group_selected);

        groupImage = findViewById(R.id.groupImage);
        groupName = findViewById(R.id.groupName);
        debt = findViewById(R.id.debt);
        listOfMembers = findViewById(R.id.memberList);
        log = findViewById(R.id.log);
        fab = findViewById(R.id.fab);

    }

}
