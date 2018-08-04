package com.example.jens.splittinit.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.jens.splittinit.R;
import com.example.jens.splittinit.listAdapters.GroupExpensesList;

import java.util.ArrayList;

public class GroupExpenses extends AppCompatActivity{

    public EditText titel, description, amount;
    public ImageView groupImage;
    public ListView memberList;
    public Button confirm;

    public ArrayList<Integer> profilePicture;
    public ArrayList<String> memberName;

    public void onCreate(Bundle savedInstanceState) {
        // call the super class onCreate to complete the creation of activity like
        // the view hierarchy
        super.onCreate(savedInstanceState);

        initialize();

        updateViews();


    }

    public void updateViews(){

        profilePicture = new ArrayList<>();
        memberName = new ArrayList<>();


        //just for testing
        memberName.add("Fukka1");
        memberName.add("Fukka2");
        memberName.add("Fukka3");
        memberName.add("Fukka4");
        memberName.add("Fukka5");

        profilePicture.add(R.drawable.common_google_signin_btn_icon_dark);
        profilePicture.add(R.drawable.common_google_signin_btn_icon_dark);
        profilePicture.add(R.drawable.common_google_signin_btn_icon_dark);
        profilePicture.add(R.drawable.common_google_signin_btn_icon_dark);
        profilePicture.add(R.drawable.common_google_signin_btn_icon_dark);
        //just for testing


        Integer [] profileImageArray = new Integer[profilePicture.size()];
        profileImageArray = profilePicture.toArray(profileImageArray);

        String[] memberNameArray = new String[memberName.size()];
        memberNameArray = memberName.toArray(memberNameArray);


        GroupExpensesList adapter = new GroupExpensesList(this, memberNameArray, profileImageArray);
        memberList.setAdapter(adapter);
    }

    private void initialize() {
        setContentView(R.layout.group_expenses);

        titel = findViewById(R.id.titel);
        description = findViewById(R.id.description);
        amount = findViewById(R.id.amount);
        groupImage = findViewById(R.id.groupImage);
        memberList = findViewById(R.id.memberList);
        confirm = findViewById(R.id.confirm);



    }
}
