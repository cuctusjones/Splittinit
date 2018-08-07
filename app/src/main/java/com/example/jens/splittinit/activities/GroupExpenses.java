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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.jens.splittinit.R;
import com.example.jens.splittinit.listAdapters.GroupExpensesList;
import com.example.jens.splittinit.model.Expense;
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

import static com.example.jens.splittinit.activities.DuoActivity.isInteger;

public class GroupExpenses extends AppCompatActivity {

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
    public CheckBox checkBox;
    public boolean[] checkBoxes;
    private ArrayList<Expense> currentUserExpenses;
    private ArrayList<ArrayList<Expense>> otherUserExpenses = new ArrayList<>();
    private ArrayList<String> otherUserIds = new ArrayList<>();
    private ArrayList<String> otherUserNames = new ArrayList<>();
    private Group group;


    public ArrayList<Integer> profilePicture;
    public ArrayList<String> memberName;

    @Override
    public void onStart() {
        super.onStart();


        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                for (int i = 0; i < checkBoxes.length; i++) {
                    if (checkBoxes[i]) {

                        otherUserIds.add(group.getMembers().get(i));

                        User user = myDataSnapshot.child("users").child(group.getMembers().get(i)).getValue(User.class);

                        otherUserExpenses.add(user.getExpenses());
                        otherUserNames.add(user.getEmail());
                    }
                }


                if (isValid()) {
                    int money1 = Integer.parseInt(amount.getText().toString());
                    int money=money1/group.getMembers().size();

                    int logid=0;
                    String name = currentUser.getEmail();
                    int index = name.indexOf("@");
                    name = name.substring(0,index);
                    logid=(int)myDataSnapshot.child("groups").child(Integer.toString(getIntent().getIntExtra("groupID", 0))).child("log").getChildrenCount();
                    myRef.child("groups").child(Integer.toString(getIntent().getIntExtra("groupID", 0))).child("log").child(Integer.toString(logid)).setValue(name+" paid " + money1);
                    logid++;
                    int i = 0;
                    for (String s : otherUserIds) {
                        //addNewExpense(s, auth.getCurrentUser().getUid(), money / otherUserIds.size(), otherUserExpenses.get(i));

                        name=otherUserNames.get(i);
                        index=name.indexOf("@");
                        name=name.substring(0,index);
                        myRef.child("groups").child(Integer.toString(getIntent().getIntExtra("groupID", 0))).child("log").child(Integer.toString(logid)).setValue(name+" owes " + money);
                        logid++;


                        boolean alreadyInList = false;
                        for (Expense e : otherUserExpenses.get(i)) {
                            if (e.getFriendid().equals(auth.getCurrentUser().getUid())) {
                                e.setValue(e.getValue() + money);


                                myRef.child("users").child(s).child("expenses").setValue(otherUserExpenses.get(i));
                                alreadyInList = true;
                            }




                        }
                        if (!alreadyInList) {
                            otherUserExpenses.get(i).add(new Expense(money, auth.getCurrentUser().getUid()));
                            myRef.child("users").child(s).child("expenses").setValue(otherUserExpenses.get(i));
                        }


                        //addNewExpense(auth.getCurrentUser().getUid(), s, (money / otherUserIds.size()) * (-1), currentUserExpenses);

                        alreadyInList = false;
                        for (Expense e : currentUserExpenses) {
                            if (e.getFriendid().equals(s)) {
                                e.setValue(e.getValue() + (money*(-1)));


                                myRef.child("users").child(auth.getCurrentUser().getUid()).child("expenses").setValue(currentUserExpenses);
                                alreadyInList = true;
                            }




                        }
                        if (!alreadyInList) {
                            currentUserExpenses.add(new Expense((money*(-1)), s));
                            myRef.child("users").child(auth.getCurrentUser().getUid()).child("expenses").setValue(currentUserExpenses);
                        }
                        i++;
                    }


                } else {
                    Toast.makeText(GroupExpenses.this, "invalid input try again!",
                            Toast.LENGTH_SHORT).show();
                }


                finish();
            }
        });


    }


    public void addNewExpense(String uid, String friendID, int money, ArrayList<Expense> userExpenses) {
        boolean alreadyInList = false;
        for (Expense e : userExpenses) {
            if (e.getFriendid().equals(friendID)) {
                e.setValue(e.getValue() + money);


                myRef.child("users").child(uid).child("expenses").setValue(userExpenses);
                alreadyInList = true;
            }

            if (!alreadyInList) {
                userExpenses.add(new Expense(money, friendID));
                myRef.child("users").child(uid).child("expenses").setValue(userExpenses);
            }


        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private boolean isValid() {
        if (title.getText().toString().equals("Titel")) {
            Toast.makeText(GroupExpenses.this, "enter title",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (description.getText().toString().equals("description")) {
            Toast.makeText(GroupExpenses.this, "enter description",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (!isInteger(amount.getText().toString()) && Integer.parseInt(amount.getText().toString()) > 0) {
            Toast.makeText(GroupExpenses.this, "enter valid amount",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
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


                group = myDataSnapshot.child("groups").child(Integer.toString(getIntent().getIntExtra("groupID", 0))).getValue(Group.class);
                if(currentUserExpenses==null){
                    currentUserExpenses = value.getExpenses();
                }



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

            checkBoxes = new boolean[memberName.size()];


            GroupExpensesList adapter = new GroupExpensesList(this, memberNameArray, profileImageArray) {
                @Override
                public View getView(final int position, View view, ViewGroup parent) {
                    LayoutInflater inflater = GroupExpenses.this.getLayoutInflater();
                    View rowView = inflater.inflate(R.layout.group_checkbox_list, null, true);
                    checkBox = rowView.findViewById(R.id.memberName);

                    CircleImageView profilePicture = rowView.findViewById(R.id.profileImage);


                    checkBox.setText(memberName.get(position));

                    StorageReference groupImg = mStorageRef.child("profileImages/" + group.getMembers().get(position));
                    Glide.with(getApplicationContext())
                            .using(new FirebaseImageLoader())
                            .load(groupImg)
                            .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                            .into(profilePicture);

                    checkBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!checkBoxes[position]) {
                                checkBoxes[position] = true;
                            } else {
                                checkBoxes[position] = false;
                            }


                        }
                    });
                    return rowView;
                }
            };
            memberList.setAdapter(adapter);


        }
    }
        private void initialize () {
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
