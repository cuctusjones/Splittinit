package com.example.jens.splittinit.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jens.splittinit.R;
import com.example.jens.splittinit.model.Expense;
import com.example.jens.splittinit.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DuoActivity extends AppCompatActivity {

    public TextView email, name;
    public EditText oweMoney, description, amount;
    public RadioButton youOwe, himOwe;
    public ImageView profileImage;
    public Button confirm;
    private String selectedFriendEmail;
    private String selectedFriendName;
    private String selectedFriendId;
    DatabaseReference myRef;
    FirebaseDatabase database;
    private FirebaseAuth auth;
    private ArrayList<Expense> currentUserExpenses;
    private ArrayList<Expense> otherUserExpenses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        auth = FirebaseAuth.getInstance();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User value = dataSnapshot.child("users").child(auth.getCurrentUser().getUid()).getValue(User.class);
                User other = dataSnapshot.child("users").child(selectedFriendId).getValue(User.class);


                currentUserExpenses = value.getExpenses();
                otherUserExpenses = other.getExpenses();




                Log.d("login", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("login", "Failed to read value.", error.toException());
            }
        });


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                selectedFriendEmail = null;
            } else {
                selectedFriendEmail = extras.getString("selectedFriend");
                selectedFriendId = extras.getString("selectedFriendId");
            }
        } else {
            selectedFriendEmail = (String) savedInstanceState.getSerializable("selectedFriend");
            selectedFriendId = (String) savedInstanceState.getSerializable("selectedFriendId");
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

        oweMoney = (EditText) findViewById(R.id.titel);
        description = (EditText) findViewById(R.id.description);

        youOwe = (RadioButton) findViewById(R.id.youOwe);
        himOwe = (RadioButton) findViewById(R.id.himOwe);

        profileImage = (ImageView) findViewById(R.id.profileImage);

        confirm = (Button) findViewById(R.id.confirm);
        amount = (EditText) findViewById(R.id.amount);


        email.setText(selectedFriendEmail);
        if (null != selectedFriendEmail && selectedFriendEmail.length() > 0 )
        {
            int endIndex = selectedFriendEmail.lastIndexOf("@");
            if (endIndex != -1)
            {
                selectedFriendName = selectedFriendEmail.substring(0, endIndex); // not forgot to put check if(endIndex != -1)
            }
        }
        name.setText(selectedFriendName);






        // this code very nice i think
        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(isValid()){
                    if(youOwe.isSelected()){
                        for(Expense e : currentUserExpenses){
                            if(e.getFriendid().equals(selectedFriendId)){
                                e.setValue(e.getValue()+Integer.parseInt(amount.getText().toString()));
                                System.out.println(amount.getText().toString());

                                myRef.child("users").child(auth.getCurrentUser().getUid()).child("expenses").setValue(currentUserExpenses);

                                for(Expense ex : otherUserExpenses){
                                    if(ex.getFriendid().equals(auth.getCurrentUser().getUid())){
                                        ex.setValue(ex.getValue()+ (Integer.parseInt(amount.getText().toString())*-1));
                                        myRef.child("users").child(selectedFriendId).child("expenses").setValue(otherUserExpenses);
                                    }
                                }
                                finish();
                                return;

                            }
                        }





                        currentUserExpenses.add(new Expense(Integer.parseInt(amount.getText().toString()), selectedFriendId));
                        otherUserExpenses.add(new Expense(Integer.parseInt(amount.getText().toString()) * -1, auth.getCurrentUser().getUid()));

                        myRef.child("users").child(auth.getCurrentUser().getUid()).child("expenses").setValue(currentUserExpenses);
                        myRef.child("users").child(selectedFriendId).child("expenses").setValue(otherUserExpenses);

                    }else{

                        for(Expense e : currentUserExpenses){
                            if(e.getFriendid().equals(selectedFriendId)){
                                e.setValue(e.getValue()+(Integer.parseInt(amount.getText().toString())*(-1)));

                                myRef.child("users").child(auth.getCurrentUser().getUid()).child("expenses").setValue(currentUserExpenses);

                                for(Expense ex : otherUserExpenses){
                                    if(ex.getFriendid().equals(auth.getCurrentUser().getUid())){
                                        ex.setValue(ex.getValue()+ (Integer.parseInt(amount.getText().toString())));
                                        myRef.child("users").child(selectedFriendId).child("expenses").setValue(otherUserExpenses);
                                    }
                                }
                                finish();
                                return;

                            }
                        }




                        currentUserExpenses.add(new Expense(Integer.parseInt(amount.getText().toString()) * -1, selectedFriendId));
                        otherUserExpenses.add(new Expense(Integer.parseInt(amount.getText().toString()), auth.getCurrentUser().getUid()));

                        myRef.child("users").child(auth.getCurrentUser().getUid()).child("expenses").setValue(currentUserExpenses);
                        myRef.child("users").child(selectedFriendId).child("expenses").setValue(otherUserExpenses);
                    }




                }else{
                    Toast.makeText(DuoActivity.this, "invalid input try again!",
                            Toast.LENGTH_SHORT).show();
                }


                finish();
            }
        });


    }

    private boolean isValid() {
        if(name.getText().toString().equals("Titel")){
            Toast.makeText(DuoActivity.this, "enter title",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if(description.getText().toString().equals("description")) {
            Toast.makeText(DuoActivity.this, "enter description",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if(!isInteger(amount.getText().toString())){
            Toast.makeText(DuoActivity.this, "enter valid amount",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

}
