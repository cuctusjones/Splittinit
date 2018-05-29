package com.example.jens.splittinit.activities;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jens.splittinit.R;
import com.example.jens.splittinit.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;


public class Tab1Duo extends Fragment {

    private ImageView person1;
    private ImageView person2;

    private TextView person1txt;
    private TextView person2txt;
    private StorageReference mStorageRef;
    private FirebaseAuth auth;

    private ConstraintLayout constraintLayout;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ListView list;





    @Override
    public void onStart() {

        super.onStart();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User value = dataSnapshot.child("users").child(auth.getCurrentUser().getUid()).getValue(User.class);

                updateViews(value);


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.tab1duo, container, false);

        //initializing stuff
        initialize(rootView);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        auth = FirebaseAuth.getInstance();







        return rootView;
    }


    public ConstraintLayout getConstraintLayout(){
        return constraintLayout;
    }

    private void updateViews(User user) {

        String[] texts = {user.getExpenses().get(0).getFriendid() + " \n\nDEBT: " + user.getExpenses().get(0).getValue() +"€",
                user.getExpenses().get(1).getFriendid() + " \n\nDEBT: " + user.getExpenses().get(1).getValue() +"€"


        } ;
        Integer[] imageId = {
                R.drawable.common_google_signin_btn_icon_light_normal,
                R.drawable.check_split


        };


        CustomList adapter = new CustomList(getActivity(),texts,imageId);
        list.setAdapter(adapter);
        String text = user.getExpenses().get(0).getFriendid() + " \ndebt: " + user.getExpenses().get(0).getValue();
        //person2txt.setText(text);
    }



    private void initialize(View v) {

        //Imageviews
        /*person1 = (ImageView) v.getRootView().findViewById(R.id.person1);
        person2 = (ImageView) v.getRootView().findViewById(R.id.person2);

        person1txt = (TextView) v.getRootView().findViewById(R.id.person1txt);
        person2txt = (TextView) v.getRootView().findViewById(R.id.person2txt);*/

        constraintLayout = (ConstraintLayout) v.getRootView().findViewById(R.id.constraintLayout);

        list = (ListView) v.getRootView().findViewById(R.id.list);




    }
}
