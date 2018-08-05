package com.example.jens.splittinit.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.jens.splittinit.R;
import com.example.jens.splittinit.listAdapters.GroupMemberList;

import java.util.ArrayList;

public class FriendList extends AppCompatActivity{

    public ListView friendlist;

    public ArrayList<Integer> profilePicture;
    public ArrayList<String> email;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        initialize();
        updateViews();
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

        //just for testing
        email.add("Fukka1");
        email.add("Fukka2");
        email.add("Fukka3");
        email.add("Fukka4");
        email.add("Fukka5");

        profilePicture.add(R.drawable.common_google_signin_btn_icon_dark);
        profilePicture.add(R.drawable.common_google_signin_btn_icon_dark);
        profilePicture.add(R.drawable.common_google_signin_btn_icon_dark);
        profilePicture.add(R.drawable.common_google_signin_btn_icon_dark);
        profilePicture.add(R.drawable.common_google_signin_btn_icon_dark);
        //just for testing


        Integer[] profileImageArray = new Integer[profilePicture.size()];
        profileImageArray = profilePicture.toArray(profileImageArray);

        String[] emailArray = new String[email.size()];
        emailArray = email.toArray(emailArray);


        GroupMemberList adapter = new GroupMemberList(this, emailArray, profileImageArray);
        friendlist.setAdapter(adapter);
    }

    public void initialize(){
        setContentView(R.layout.friend_list_view);

        friendlist = findViewById(R.id.friendlist);
    }
}
