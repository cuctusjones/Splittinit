package com.example.jens.splittinit.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jens.splittinit.R;
import com.example.jens.splittinit.model.Expense;
import com.example.jens.splittinit.model.Group;
import com.example.jens.splittinit.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Tab2Group extends Fragment {

    public ListView list;
    public ArrayList<Integer> groupImg;
    public ArrayList<String> groupName;

    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseAuth auth;
    private DataSnapshot myDataSnapshot;


    public ConstraintLayout constraintLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.tab2group, container, false);

        //initializing stuff
        initialize(rootView);
        //updateViews();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        auth = FirebaseAuth.getInstance();


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User value = dataSnapshot.child("users").child(auth.getCurrentUser().getUid()).getValue(User.class);
                myDataSnapshot = dataSnapshot;


                updateViews();


                Log.d("login", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("login", "Failed to read value.", error.toException());
            }
        });

        updateViews();


        return rootView;
    }

    @Override
    public void onStart() {

        super.onStart();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User value = myDataSnapshot.child("users").child(auth.getCurrentUser().getUid()).getValue(User.class);



                updateViews();


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

        groupImg = new ArrayList<Integer>();
        groupName = new ArrayList<String>();

        if(myDataSnapshot!= null) {
            for (int i = 0; i < myDataSnapshot.child("groups").getChildrenCount(); i++) {
                groupName.add(myDataSnapshot.child("groups").child(Integer.toString(i)).child("name").getValue(String.class));
            }
        }


        groupImg.add(R.drawable.check_split);
        groupImg.add(R.drawable.common_google_signin_btn_icon_dark);
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
