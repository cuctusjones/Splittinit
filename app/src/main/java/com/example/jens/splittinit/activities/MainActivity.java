package com.example.jens.splittinit.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.jens.splittinit.R;
import com.example.jens.splittinit.model.Expense;
import com.example.jens.splittinit.model.Group;
import com.example.jens.splittinit.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private FirebaseAuth auth;
    private FirebaseAnalytics mFirebaseAnalytics;
    private StorageReference mStorageRef;
    DatabaseReference myRef;
    FirebaseDatabase database;
    FirebaseUser firebaseUser;
    public NavigationView navigationView;
    View headerView;
    TextView revNameField;
    TextView revEmailField;

    private DataSnapshot myDataSnapshot;

    public FloatingActionButton fab;

    public MenuItem owing, getting, add_friend, add_group;
    public CircleImageView profileImage;


    private int TAKE_IMAGE =0;
    private int PICK_IMAGE_REQUEST =1;



    private DrawerLayout mDrawerLayout;
    private User currentUser;

    private String m_Text = "Enter email of friend";

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public AlertDialog myDialog;


    @Override
    protected void onStart() {

        super.onStart();

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

        String name = "nothing worked";
        String email = "nothing worked";


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            for (UserInfo profile : firebaseUser.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                name = profile.getDisplayName();

                //name_field.setText(name);
                email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();
            }


        } else {
            name = "provider shit dont work";
            email = "provider shit dont work";
        }


        revEmailField.setText(email);

        revNameField.setText(name);


    }

    @Override
    public void onResume() {
        super.onResume();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User value = dataSnapshot.child("users").child(auth.getCurrentUser().getUid()).getValue(User.class);

                currentUser = value;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_friend = (MenuItem) findViewById(R.id.add_friend);
        add_group = (MenuItem) findViewById(R.id.add_group);
        owing = (MenuItem) findViewById(R.id.owing);
        getting = (MenuItem) findViewById(R.id.getting);

        //add_friend.setEnabled(false);
        //add_group.setEnabled(false);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        /*navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();


                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });*/



        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        // Write a message to the database

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();




        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        fab = findViewById(R.id.fab);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);

        profileImage = (CircleImageView) headerView.findViewById(R.id.profile_image);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.Theme_Holo_Dialog_Alert));
                builder.setMessage("Take or select picture?")
                        .setCancelable(false)
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
            }
        });

        revEmailField = (TextView) headerView.findViewById(R.id.email_field);


        revNameField = (TextView) headerView.findViewById(R.id.name_field);
        revNameField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText taskEditText = new EditText(MainActivity.this);
                AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.Theme_Holo_Dialog_Alert))
                        .setTitle("Enter Name")
                        .setMessage("What is your name?")
                        .setView(taskEditText)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                revNameField.setText(task);
                                myRef.child("users").child(auth.getCurrentUser().getUid()).child("name").setValue(task);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                mDrawerLayout.closeDrawers();
            }
        });


        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 2 | position == 1) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == TAKE_IMAGE && resultCode == RESULT_OK){
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    profileImage.setImageBitmap(imageBitmap);

                }

                if (requestCode == PICK_IMAGE_REQUEST){
                    if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

                        Uri uri = data.getData();

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            // Log.d(TAG, String.valueOf(bitmap));

                            profileImage.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    //method to show dialog when adding a friend
    public void addFriend(MenuItem item){
        final EditText taskEditText = new EditText(MainActivity.this);
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Add a new friend")
                .setMessage("Type the email of your friend")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = taskEditText.getText().toString();

                                String friendid="";

                                for(int i =0;i<myDataSnapshot.child("emailid").getChildrenCount();i++){
                                    if(myDataSnapshot.child("emailid").child(Integer.toString(i)).child("email").getValue(String.class).equals(task)){
                                        friendid = myDataSnapshot.child("emailid").child(Integer.toString(i)).child("id").getValue(String.class);
                                    }
                                }

                                if(friendid.equals("")){
                                    Toast.makeText(MainActivity.this, "not found",
                                            Toast.LENGTH_SHORT).show();
                                }else{
                                    User currentUser = myDataSnapshot.child("users").child(auth.getCurrentUser().getUid()).getValue(User.class);
                                    User otherUser = myDataSnapshot.child("users").child(friendid).getValue(User.class);

                                    ArrayList<String> myfriends = currentUser.getFriends();
                                    ArrayList<String> hisfriends = otherUser.getFriends();

                                    for(String s : myfriends){
                                        if(s.equals(friendid)){
                                            Toast.makeText(MainActivity.this, "you're already friends",
                                                    Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }


                                    myfriends.add(friendid);
                                    hisfriends.add(auth.getCurrentUser().getUid());
                                    myRef.child("users").child(auth.getCurrentUser().getUid()).child("friends").setValue(myfriends);
                                    myRef.child("users").child(friendid).child("friends").setValue(hisfriends);
                                    Toast.makeText(MainActivity.this, "you're friends now wuhuu!",
                                            Toast.LENGTH_SHORT).show();
                                }












                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    //method to show dialog when adding a friend
    public void addGroup(MenuItem item) {
        final EditText taskEditText = new EditText(MainActivity.this);
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Add a new group")
                .setMessage("How you wanna name your group?")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());

                        Group newGroup = new Group(task);
                        int id = (int) myDataSnapshot.child("groups").getChildrenCount();
                        myRef.child("groups").child(Integer.toString(id)).setValue(newGroup);


                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    Tab1Duo tab1 = new Tab1Duo();
                    return tab1;
                case 1:
                    Tab2Group tab2 = new Tab2Group();
                    return tab2;
                case 2:
                    Tab3CheckSplit tab3 = new Tab3CheckSplit();
                    return tab3;
                default:
                    return null;
            }
        }


        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "PERSONAL";
                case 1:
                    return "GROUP";
                case 2:
                    return "CHECKSPLIT";
            }
            return null;
        }


    }


}