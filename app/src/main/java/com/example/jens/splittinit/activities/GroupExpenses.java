package com.example.jens.splittinit.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.jens.splittinit.R;

public class GroupExpenses extends AppCompatActivity{

    public EditText titel, description, amount;
    public ImageView groupImage;
    public ListView memberList;
    public Button confirm;

    public void onCreate(Bundle savedInstanceState) {
        // call the super class onCreate to complete the creation of activity like
        // the view hierarchy
        super.onCreate(savedInstanceState);

        initialize();


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
