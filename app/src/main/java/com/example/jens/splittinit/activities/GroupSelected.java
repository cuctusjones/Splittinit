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
import com.example.jens.splittinit.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // call the super class onCreate to complete the creation of activity like
        // the view hierarchy
        super.onCreate(savedInstanceState);

        initialize();

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

