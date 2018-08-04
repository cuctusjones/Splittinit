package com.example.jens.splittinit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jens.splittinit.R;
import com.example.jens.splittinit.listAdapters.GroupExpensesList;
import com.example.jens.splittinit.listAdapters.GroupMemberList;
import com.example.jens.splittinit.listAdapters.LogList;
import com.example.jens.splittinit.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GroupSelected extends AppCompatActivity {

    public TextView groupName, debt;
    public ImageView groupImage;
    public ListView listOfMembers, log;
    public FloatingActionButton fab;

    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference mStorageRef;
    private FirebaseAuth auth;
    private DataSnapshot myDataSnapshot;
    private User currentUser;

    public ArrayList<String> memberName;
    public ArrayList<Integer> profilePicture;

    public ArrayList<String> logEntry;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // call the super class onCreate to complete the creation of activity like
        // the view hierarchy
        super.onCreate(savedInstanceState);

        initialize();

        updateViews();

        // Write a message to the database

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();





        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                /*if(myDataSnapshot.child("users").child(auth.getCurrentUser().getUid())==null) {
                    User user = new User(auth.getCurrentUser().getUid(), auth.getCurrentUser().getDisplayName(), null, auth.getCurrentUser().getEmail(), auth.getCurrentUser().getPhotoUrl());
                    myRef.child("users").child(auth.getCurrentUser().getUid()).setValue(user);
                }*/

                User value = dataSnapshot.child("users").child(auth.getCurrentUser().getUid()).getValue(User.class);

                currentUser = value;
                myDataSnapshot=dataSnapshot;

                Log.d("login", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("login", "Failed to read value.", error.toException());
            }
        });
    }

    private void updateViews() {

        //memberlist
        profilePicture = new ArrayList<>();
        memberName = new ArrayList<>();

        //just for testing
        memberName.add("Fukka1");
        memberName.add("Fukka2");
        memberName.add("Fukka3");
        memberName.add("Fukka4");
        memberName.add("Fukka5");

        profilePicture.add(R.drawable.common_google_signin_btn_icon_dark);
        profilePicture.add(R.drawable.common_google_signin_btn_icon_dark);
        profilePicture.add(R.drawable.common_google_signin_btn_icon_dark);
        profilePicture.add(R.drawable.common_google_signin_btn_icon_dark);
        profilePicture.add(R.drawable.common_google_signin_btn_icon_dark);
        //just for testing


        Integer [] profileImageArray = new Integer[profilePicture.size()];
        profileImageArray = profilePicture.toArray(profileImageArray);

        String[] memberNameArray = new String[memberName.size()];
        memberNameArray = memberName.toArray(memberNameArray);


        GroupMemberList adapter = new GroupMemberList(this, memberNameArray, profileImageArray);
        listOfMembers.setAdapter(adapter);

        //logList

        logEntry = new ArrayList<>();

        logEntry.add("Fukka fucked Fukka2");
        logEntry.add("Fukka2 fucked Fukka3");
        logEntry.add("Fukka3 fucked Fukka4");
        logEntry.add("Fukka4 fucked Fukka5");
        logEntry.add("Fukka fucked Fukka2");
        logEntry.add("Fukka2 fucked Fukka3");
        logEntry.add("Fukka3 fucked Fukka4");
        logEntry.add("Fukka4 fucked Fukka5");
        logEntry.add("Fukka fucked Fukka2");
        logEntry.add("Fukka2 fucked Fukka3");
        logEntry.add("Fukka3 fucked Fukka4");
        logEntry.add("Fukka4 fucked Fukka5");

        String[] logEntryArray = new String[logEntry.size()];
        logEntryArray = logEntry.toArray(logEntryArray);

        LogList adapter2 = new LogList(this, logEntryArray);
        log.setAdapter(adapter2);

    }

    @Override
        public void onStart () {
            super.onStart();


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(GroupSelected.this, GroupExpenses.class));
                }
            });

            //TODO: Jens change mal den Gruppenname und das Icon von der Gruppen, ty
            //change groupname accordingly
            //groupName.setText(myDataSnapshot.child("groups").child(Integer.toString(getIntent().getIntExtra("groupID", 0))).child("name").getValue(String.class));

        }


        private void initialize () {
            setContentView(R.layout.group_selected);

            groupImage = findViewById(R.id.groupImage);
            groupName = findViewById(R.id.groupName);
            debt = findViewById(R.id.debt);
            listOfMembers = findViewById(R.id.memberList);
            log = findViewById(R.id.log);
            fab = findViewById(R.id.fab);

        }

    }

