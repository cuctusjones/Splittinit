package com.example.jens.splittinit.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.jens.splittinit.R;
import com.example.jens.splittinit.listAdapters.GroupMemberList;
import com.example.jens.splittinit.model.Group;
import com.example.jens.splittinit.model.User;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendList extends AppCompatActivity{

    public ListView friendlist;

    public ArrayList<Integer> profilePicture;
    public ArrayList<String> email,id;
    public CircleImageView userImage;
    public TextView userName;
    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference mStorageRef;
    private FirebaseAuth auth;
    private DataSnapshot myDataSnapshot;
    private User currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        initialize();

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

                ArrayList<Group> groups = new ArrayList<Group>();
                for(int i=0;i<dataSnapshot.child("groups").getChildrenCount();i++){
                    groups.add(dataSnapshot.child("groups").child(Integer.toString(i)).getValue(Group.class));
                }



                currentUser = value;
                myDataSnapshot = dataSnapshot;


                Log.d("login", "Value is: " + value);
                updateViews();
            }





            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("login", "Failed to read value.", error.toException());
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void updateViews(){
        //memberlist
        profilePicture = new ArrayList<>();
        email = new ArrayList<>();
        id = new ArrayList<>();

        final User user = myDataSnapshot.child("users").child(auth.getCurrentUser().getUid()).getValue(User.class);


        //just for testing

        //just for testing


        Integer[] profileImageArray = new Integer[profilePicture.size()];
        profileImageArray = profilePicture.toArray(profileImageArray);

        String[] emailArray = new String[email.size()];
        emailArray = email.toArray(emailArray);


        String mail ="";



        for(String s : currentUser.getFriends()){
            for (int i = 0; i < myDataSnapshot.child("emailid").getChildrenCount(); i++) {
                if (myDataSnapshot.child("emailid").child(Integer.toString(i)).child("id").getValue(String.class).equals(s)) {
                    mail = myDataSnapshot.child("emailid").child(Integer.toString(i)).child("email").getValue(String.class);
                    email.add(mail);

                }
            }
            id.add(s);
        }




        //memberNameArray = new String[memberName.size()];
        final String[] memberNameArray = new String[user.getFriends().size()];


        final GroupMemberList adapter = new GroupMemberList(this, memberNameArray, profileImageArray){
            @Override
            public View getView(int position, View view, ViewGroup parent){
                LayoutInflater inflater = FriendList.this.getLayoutInflater();
                View rowView= inflater.inflate(R.layout.member_view_group_list, null, true);
                userName = rowView.findViewById(R.id.userName);

                userImage = rowView.findViewById(R.id.userImage);



                userName.setText(email.get(position));
                StorageReference groupImg = mStorageRef.child("profileImages/" + user.getFriends().get(position));
                if(groupImg!=null){
                    Glide.with(getApplicationContext())
                            .using(new FirebaseImageLoader())
                            .load(groupImg)
                            .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                            .into(userImage);
                }
                return rowView;

            }
        };
        friendlist.setAdapter(adapter);
    }

    public void initialize(){
        setContentView(R.layout.friend_list_view);

        friendlist = findViewById(R.id.friendlist);
        userName = findViewById(R.id.userName);

        userImage = findViewById(R.id.userImage);

    }
}
