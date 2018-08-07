package com.example.jens.splittinit.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.jens.splittinit.R;
import com.example.jens.splittinit.listAdapters.GroupMemberList;
import com.example.jens.splittinit.listAdapters.LogList;
import com.example.jens.splittinit.model.Expense;
import com.example.jens.splittinit.model.Group;
import com.example.jens.splittinit.model.User;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupSelected extends AppCompatActivity {

    public TextView groupName, debt, userName;
    public CircleImageView groupImage, userImage;
    public ListView listOfMembers, log;
    public FloatingActionButton fab;
    public Button addMember;


    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference mStorageRef;
    private FirebaseAuth auth;
    private DataSnapshot myDataSnapshot;
    private User currentUser;

    public ArrayList<String> memberIds;
    public ArrayList<String> memberName;

    public ArrayList<Integer> profilePicture;

    public ArrayList<String> logEntry;

    public ArrayList<Group> groupArrayList;

    private int TAKE_IMAGE = 0;
    private int PICK_IMAGE_REQUEST = 1;


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

                ArrayList<Group> groups = new ArrayList<Group>();
                for(int i=0;i<dataSnapshot.child("groups").getChildrenCount();i++){
                    groups.add(dataSnapshot.child("groups").child(Integer.toString(i)).getValue(Group.class));
                }

                groupArrayList=groups;

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

        myRef.child("users").child("000").child("expenses").child("0").child("value").setValue("12");

        myRef.child("users").child("000").child("expenses").child("0").child("value").setValue("15");

        downloadImage(Integer.toString(getIntent().getIntExtra("groupID", 0)));




    }

    public void downloadImage(String s){
        StorageReference groupImg = mStorageRef.child("groupImages/" + s);
        Glide.with(getApplicationContext())
                .using(new FirebaseImageLoader())
                .load(groupImg)
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .into(groupImage);
    }

    public void changeGroupIcon(View view){

            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_Holo_Dialog_Alert));
            builder.setMessage("Take or select picture?")
                    .setCancelable(true)
                    .setPositiveButton("Take picture", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(takePictureIntent, TAKE_IMAGE);
                            }
                        }
                    })
                    .setNegativeButton("Select picture", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            updateViews();
    }


    private void uploadImage(Uri filePath) {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);

            progressDialog.setTitle("Uploading...");
            progressDialog.show();


            final StorageReference ref = mStorageRef.child("groupImages/" + getIntent().getIntExtra("groupID", 0));
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(GroupSelected.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(GroupSelected.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }

        downloadImage(Integer.toString(getIntent().getIntExtra("groupID", 0)));

    }

    private void updateViews() {



        //memberlist
        profilePicture = new ArrayList<>();
        memberName = new ArrayList<>();



        final Group group = myDataSnapshot.child("groups").child(Integer.toString(getIntent().getIntExtra("groupID", 0))).getValue(Group.class);

        String mail="";


        for(String s : group.getMembers()){
            for (int i = 0; i < myDataSnapshot.child("emailid").getChildrenCount(); i++) {
                if (myDataSnapshot.child("emailid").child(Integer.toString(i)).child("id").getValue(String.class).equals(s)) {
                    mail = myDataSnapshot.child("emailid").child(Integer.toString(i)).child("email").getValue(String.class);
                    memberName.add(mail);

                }
            }
        }



        Integer[] profileImageArray = new Integer[profilePicture.size()];
        profileImageArray = profilePicture.toArray(profileImageArray);

         //memberNameArray = new String[memberName.size()];
        final String[] memberNameArray = memberName.toArray(new String[memberName.size()]);


        final GroupMemberList adapter = new GroupMemberList(this, memberNameArray, profileImageArray){
            @Override
            public View getView(int position, View view, ViewGroup parent){
                LayoutInflater inflater = GroupSelected.this.getLayoutInflater();
                View rowView= inflater.inflate(R.layout.member_view_group_list, null, true);
                userName = rowView.findViewById(R.id.userName);

                userImage = rowView.findViewById(R.id.userImage);



                userName.setText(memberName.get(position));
                StorageReference groupImg = mStorageRef.child("profileImages/" + group.getMembers().get(position));
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
        listOfMembers.setAdapter(adapter);

        //logList

        logEntry = new ArrayList<>();

        for(int i=0;i<(int) myDataSnapshot.child("groups").child(Integer.toString(getIntent().getIntExtra("groupID", 0))).child("log").getChildrenCount();i++){
            String message = myDataSnapshot.child("groups").child(Integer.toString(getIntent().getIntExtra("groupID", 0))).child("log").child(Integer.toString(i)).getValue(String.class);
            logEntry.add(message);
        }

        logEntry.add("Jens paid 40€");
        logEntry.add("Timo owes Jens 10€");
        logEntry.add("Zabrina owes Jens 10€");
        logEntry.add("Marc owes Jens 10€");
        logEntry.add("Marc paid 420€");
        logEntry.add("Jens owes Marc 260€");

        /*logEntry.add("Fukka3 fucked Fukka4");
        logEntry.add("Fukka4 fucked Fukka5");
        logEntry.add("Fukka fucked Fukka2");
        logEntry.add("Fukka2 fucked Fukka3");
        logEntry.add("Fukka3 fucked Fukka4");
        logEntry.add("Fukka4 fucked Fukka5");*/

        String[] logEntryArray = new String[logEntry.size()];
        logEntryArray = logEntry.toArray(logEntryArray);

        LogList adapter2 = new LogList(this, logEntryArray);
        log.setAdapter(adapter2);
        int debt1=0;

        for(Expense e :currentUser.getExpenses()){
            for(String s :group.getMembers()){
                if(e.getFriendid().equals(s)){
                    debt1+=e.getValue();
                }
            }
        }

        debt.setText(Integer.toString(debt1));


    }

    @Override
    public void onStart() {
        super.onStart();

        //groupImage.setImageResource(R.drawable.check_split);

        myRef.child("users").child("000").child("expenses").child("0").child("value").setValue("12");

        myRef.child("users").child("000").child("expenses").child("0").child("value").setValue("15");


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupSelected.this, GroupExpenses.class);
                intent.putExtra("groupID", getIntent().getIntExtra("groupID", 0));
                startActivity(intent);
            }
        });



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


                groupName.setText(myDataSnapshot.child("groups").child(Integer.toString(getIntent().getIntExtra("groupID", 2))).child("name").getValue(String.class));
                updateViews();
                Log.d("login", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("login", "Failed to read value.", error.toException());
            }
        });



        //change groupname accordingly
        //groupName.setText(myDataSnapshot.child("groups").child(Integer.toString(getIntent().getIntExtra("groupID", 0))).child("name").getValue(String.class));



    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED && requestCode == TAKE_IMAGE) {
            Bundle extras = data.getExtras();
            Uri uri = data.getData();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            groupImage.setImageBitmap(imageBitmap);
            uploadImage(data.getData());

        }
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == PICK_IMAGE_REQUEST) {

                Uri uri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    groupImage.setImageBitmap(bitmap);
                    uploadImage(data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    public void addMember(View view) {
        final EditText taskEditText = new EditText(GroupSelected.this);
        AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(GroupSelected.this, R.style.Theme_Holo_Dialog_Alert))
                .setTitle("Who do you want to add?")
                .setMessage("Enter E-Mail")
                .setView(taskEditText)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());

                        String friendid = "";

                        memberIds = new ArrayList<String>();
                        for(int i =0;i<myDataSnapshot.child("groups").child(Integer.toString(getIntent().getIntExtra("groupID", 0))).getChildrenCount();i++){
                            memberIds.add(myDataSnapshot.child("groups").child(Integer.toString(getIntent().getIntExtra("groupID", 0))).child("members").child(Integer.toString(i)).getValue(String.class));

                        }



                        for (int i = 0; i < myDataSnapshot.child("emailid").getChildrenCount(); i++) {
                            if (myDataSnapshot.child("emailid").child(Integer.toString(i)).child("email").getValue(String.class).equals(task)) {
                                friendid = myDataSnapshot.child("emailid").child(Integer.toString(i)).child("id").getValue(String.class);
                            }
                        }

                        if (friendid.equals("")) {
                            Toast.makeText(GroupSelected.this, "not found",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            if(myDataSnapshot.child("groups").child(Integer.toString(getIntent().getIntExtra("groupID",0))).hasChild("members")){
                            Toast.makeText(GroupSelected.this,memberIds.get(0),
                                    Toast.LENGTH_SHORT).show();
                                for(int i=0;i<memberIds.size();i++){
                                    if(memberIds.get(i)!=null){
                                        if(memberIds.get(i).equals(friendid)){
                                            Toast.makeText(GroupSelected.this, "already a member",
                                                    Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }


                                }

                            }
                            //memberIds.add(friendid);
                            String memberId = "";
                            memberId=Long.toString(myDataSnapshot.child("groups").child(Integer.toString(getIntent().getIntExtra("groupID",0))).child("members").getChildrenCount());
                            myRef.child("groups").child(Integer.toString(getIntent().getIntExtra("groupID",0))).child("members").child(memberId).setValue(friendid);


                            User addedUser = myDataSnapshot.child("users").child(friendid).getValue(User.class);

                            if(addedUser.getGroups()!=null){
                                for(String grp :addedUser.getGroups()){
                                    if(grp.equals(Integer.toString(getIntent().getIntExtra("groupID",0)))){

                                        return;
                                    }
                                }
                                addedUser.getGroups().add(Integer.toString(getIntent().getIntExtra("groupID",0)));
                                myRef.child("users").child(friendid).setValue(addedUser);
                            }else{
                                ArrayList<String> gr = new ArrayList<>();


                                gr.add(Integer.toString(getIntent().getIntExtra("groupID",0)));
                                addedUser.setGroups(gr);
                                myRef.child("users").child(friendid).setValue(addedUser);
                                }




                        }





                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }


    private void initialize() {
        setContentView(R.layout.group_selected);

        groupImage = findViewById(R.id.groupImg);
        groupName = findViewById(R.id.groupName);
        debt = findViewById(R.id.debt);
        listOfMembers = findViewById(R.id.memberList);
        log = findViewById(R.id.log);
        fab = findViewById(R.id.fab);
        addMember = findViewById(R.id.addMember);


    }

}

