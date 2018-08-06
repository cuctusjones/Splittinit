package com.example.jens.splittinit.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.jens.splittinit.R;
import com.example.jens.splittinit.listAdapters.CustomList;
import com.example.jens.splittinit.model.User;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Tab2Group extends Fragment {

    public ListView list;
    public ArrayList<Integer> groupImg;
    public ArrayList<String> groupName,groupIds;

    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseAuth auth;
    private DataSnapshot myDataSnapshot;
    private StorageReference mStorageRef;


    public ConstraintLayout constraintLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.tab2group, container, false);

        //initializing stuff
        initialize(rootView);
        //updateViews();

        // Write a message to the database

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        auth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();


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


                Log.d("login", "Value is: " + value);


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("login", "Failed to read value.", error.toException());
            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                Intent intent = new Intent(getActivity(), GroupSelected.class);

                int id = Integer.parseInt(myDataSnapshot.child("users").child(auth.getCurrentUser().getUid()).child("groups").child(Integer.toString(position)).getValue(String.class));
                intent.putExtra("groupID", id);
                startActivity(intent);

            }
        });
        myRef.child("users").child("000").child("expenses").child("0").child("value").setValue("12");

        myRef.child("users").child("000").child("expenses").child("0").child("value").setValue("15");


    }

    private void updateViews() {

        groupImg = new ArrayList<Integer>();
        groupName = new ArrayList<String>();
        groupIds = new ArrayList<String>();

        if (myDataSnapshot != null) {
            for (int i = 0; i < myDataSnapshot.child("users").child(auth.getCurrentUser().getUid()).child("groups").getChildrenCount(); i++) {


                String grID = myDataSnapshot.child("users").child(auth.getCurrentUser().getUid()).child("groups").child(Integer.toString(i)).getValue(String.class);
                groupName.add(myDataSnapshot.child("groups").child(grID).child("name").getValue(String.class));
                groupIds.add(grID);
            }
        }





        Integer[] groupImgArray = new Integer[groupImg.size()];
        groupImgArray = groupImg.toArray(groupImgArray);


        String[] groupNameArray = new String[groupName.size()];
        groupNameArray = groupName.toArray(groupNameArray);

        CustomList adapter = new CustomList(getActivity(), groupNameArray, groupImgArray) {
            @Override
            public View getView(int position, View view, ViewGroup parent) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View rowView = inflater.inflate(R.layout.list_single, null, true);
                TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

                CircleImageView imageView = rowView.findViewById(R.id.img);
                txtTitle.setText(groupName.get(position));

                StorageReference groupImage = mStorageRef.child("groupImages/" + groupIds.get(position));
                Glide.with(getActivity())
                        .using(new FirebaseImageLoader())
                        .load(groupImage)
                        .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                        .into(imageView);
                return rowView;
            }
        };

        list.setAdapter(adapter);

        /*list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DuoActivity.class);
                startActivity(intent);
            }
        });*/

        }


    private void initialize(View v) {

        constraintLayout = (ConstraintLayout) v.getRootView().findViewById(R.id.constraintLayout);
        list = (ListView) v.getRootView().findViewById(R.id.list);

    }

    public static void setImgArray(){

    }

}
