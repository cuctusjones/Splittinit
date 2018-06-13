package com.example.jens.splittinit.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.jens.splittinit.R;
import com.example.jens.splittinit.model.Expense;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.widget.TextView;

import java.util.ArrayList;

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
    NavigationView navigationView;
    View headerView;
    TextView revNameField;
    TextView revEmailField;

    private FloatingActionButton fab;


    private DrawerLayout mDrawerLayout;
    private User currentUser;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


   @Override
    protected void onStart(){

       super.onStart();

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

       String name="nothing worked";
       String email ="nothing worked";


       firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
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



       }else {
           name= "provider shit dont work";
           email = "provider shit dont work";
       }








       revEmailField.setText(email);

       revNameField.setText(name);












    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
        navigationView.setNavigationItemSelectedListener(
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
                });



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



        //User user = new User(auth.getCurrentUser().getUid(),auth.getCurrentUser().getDisplayName(),null,auth.getCurrentUser().getEmail(),auth.getCurrentUser().getPhotoUrl());

        /*user.getFriends().add("jensfischerx@gmail.com");
        user.getFriends().add("timo.gerhard1337@googlemail.com");
        user.getExpenses().add(new Expense(42,"timo.gerhard1337@googlemail.com"));
        user.getExpenses().add(new Expense(5115,"jensfischerx@gmail.com"));*/

        ArrayList<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(42,"timo.gerhard1337@googlemail.com"));
        expenses.add(new Expense(1234,"jensfischerx@googlemail.com"));


        myRef.child("users").child(auth.getCurrentUser().getUid()).child("expenses").setValue(expenses);




        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        fab = findViewById(R.id.fab);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);

        revEmailField = (TextView) headerView.findViewById(R.id.email_field);

        revNameField = (TextView) headerView.findViewById(R.id.name_field);







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

        if(id == R.id.action_logout){
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