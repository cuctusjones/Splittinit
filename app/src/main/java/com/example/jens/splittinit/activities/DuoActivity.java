package com.example.jens.splittinit.activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.jens.splittinit.R;

public class DuoActivity extends AppCompatActivity {

    public TextView email, name;
    public EditText oweMoney, description;
    public RadioButton youOwe, himOwe;
    public ImageView profileImage;
    public Button confirm;
    private String selectedFriend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                selectedFriend= null;
            } else {
                selectedFriend= extras.getString("selectedFriend");
            }
        } else {
            selectedFriend= (String) savedInstanceState.getSerializable("selectedFriend");
        }


    }


    @Override
    protected void onStart() {

        super.onStart();

        setContentView(R.layout.duo_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        email = (TextView) findViewById(R.id.email);
        name = (TextView) findViewById(R.id.name);

        oweMoney = (EditText) findViewById(R.id.oweMoney);
        description = (EditText) findViewById(R.id.description);

        youOwe = (RadioButton) findViewById(R.id.youOwe);
        himOwe = (RadioButton) findViewById(R.id.himOwe);

        profileImage = (ImageView) findViewById(R.id.profile_image);

        confirm = (Button) findViewById(R.id.confirm);

        email.setText(selectedFriend);




        //close activity on button pressed
        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {









                finish();
            }
        });



    }

    @Override
    protected void onResume(){
        super.onResume();
    }

}
