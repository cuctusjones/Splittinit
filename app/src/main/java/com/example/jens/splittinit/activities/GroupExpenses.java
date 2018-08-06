package com.example.jens.splittinit.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.jens.splittinit.R;
import com.example.jens.splittinit.listAdapters.GroupExpensesList;
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

public class GroupExpenses extends AppCompatActivity{

    public EditText title, description, amount;
    public CircleImageView groupImage;
    public ListView memberList;
    public Button confirm;
    public TextView groupName;

    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference mStorageRef;
    private FirebaseAuth auth;
    private DataSnapshot myDataSnapshot;
    private User currentUser;

    public ArrayList<Integer> profilePicture;
    public ArrayList<String> memberName;

    @Override
    public void onStart(){
        super.onStart();

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // call the super class onCreate to complete the creation of activity like
        // the view hierarchy
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

    public void updateViews() {

        profilePicture = new ArrayList<>();
        memberName = new ArrayList<>();

        final Group group = myDataSnapshot.child("groups").child(Integer.toString(getIntent().getIntExtra("groupID", 0))).getValue(Group.class);

        String mail = "";


        for (String s : group.getMembers()) {
            for (int i = 0; i < myDataSnapshot.child("emailid").getChildrenCount(); i++) {
                if (myDataSnapshot.child("emailid").child(Integer.toString(i)).child("id").getValue(String.class).equals(s)) {
                    mail = myDataSnapshot.child("emailid").child(Integer.toString(i)).child("email").getValue(String.class);
                    memberName.add(mail);
                }
            }
        }

        StorageReference groupImg = mStorageRef.child("groupImages/" + getIntent().getIntExtra("groupID", 0));
        Glide.with(getApplicationContext())
                .using(new FirebaseImageLoader())
                .load(groupImg)
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .into(groupImage);

        groupName.setText(group.getName());




        Integer[] profileImageArray = new Integer[profilePicture.size()];
        profileImageArray = profilePicture.toArray(profileImageArray);

        String[] memberNameArray = new String[memberName.size()];
        memberNameArray = memberName.toArray(memberNameArray);


        GroupExpensesList adapter = new GroupExpensesList(this, memberNameArray, profileImageArray) {
            @Override
            public View getView(int position, View view, ViewGroup parent) {
                LayoutInflater inflater = GroupExpenses.this.getLayoutInflater();
                View rowView = inflater.inflate(R.layout.group_checkbox_list, null, true);
                CheckBox membrName = rowView.findViewById(R.id.memberName);

                CircleImageView profilePicture = rowView.findViewById(R.id.profileImage);


                membrName.setText(memberName.get(position));

                StorageReference groupImg = mStorageRef.child("profileImages/" + group.getMembers().get(position));
                Glide.with(getApplicationContext())
                        .using(new FirebaseImageLoader())
                        .load(groupImg)
                        .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                        .into(profilePicture);
                return rowView;
            }
        };
        memberList.setAdapter(adapter);

    }

    private void initialize() {
        setContentView(R.layout.group_expenses);

        title = findViewById(R.id.titel);
        description = findViewById(R.id.description);
        amount = findViewById(R.id.amount);
        groupImage = findViewById(R.id.profileImage);
        memberList = findViewById(R.id.memberList);
        confirm = findViewById(R.id.confirm);
        groupName = findViewById(R.id.groupName);



    }
}
