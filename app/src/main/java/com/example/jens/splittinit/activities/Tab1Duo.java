package com.example.jens.splittinit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.jens.splittinit.R;
import com.example.jens.splittinit.model.Expense;
import com.example.jens.splittinit.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class Tab1Duo extends Fragment {


    private StorageReference mStorageRef;
    private FirebaseAuth auth;

    private ConstraintLayout constraintLayout;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ListView list;

    private ArrayList<String> currentFriendsIds;
    private ArrayList<String> currentFriendsEmails;

    private DataSnapshot myDataSnapshot;





    @Override
    public void onStart() {

        super.onStart();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User value = dataSnapshot.child("users").child(auth.getCurrentUser().getUid()).getValue(User.class);

                currentFriendsIds = value.getFriends();
                currentFriendsEmails = new ArrayList<>();

                for(String id : currentFriendsIds){

                    currentFriendsEmails.add(dataSnapshot.child("users").child(id).child("email").getValue(String.class));
                }
                updateViews(value);



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
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            onResume();
        }
    }

    @Override
    public void onResume(){


        super.onResume();

        if (!getUserVisibleHint())
        {
            return;
        }

        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] test = new String[]{"1", "2", "3", "4"};


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Pick a friend");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice);
                for(String name :currentFriendsEmails){
                    arrayAdapter.add(name);
                }


                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //String strName = arrayAdapter.getItem(which);

                        Intent intent = new Intent(getActivity(),DuoActivity.class);
                        intent.putExtra("selectedFriend",arrayAdapter.getItem(which));
                        intent.putExtra("selectedFriendId",currentFriendsIds.get(which));
                        startActivity(intent);
                        dialog.dismiss();
                        /*AlertDialog.Builder builderInner = new AlertDialog.Builder(getActivity());
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your Selected Item is");
                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();*/
                    }
                });
                builder.show();
            }
            });


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User value = dataSnapshot.child("users").child(auth.getCurrentUser().getUid()).getValue(User.class);

                currentFriendsIds = value.getFriends();
                updateViews(value);



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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.tab1duo, container, false);

        //initializing stuff
        initialize(rootView);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        auth = FirebaseAuth.getInstance();


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User value = dataSnapshot.child("users").child(auth.getCurrentUser().getUid()).getValue(User.class);
                myDataSnapshot = dataSnapshot;

                updateViews(value);


                Log.d("login", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("login", "Failed to read value.", error.toException());
            }
        });
        list.setLongClickable(true);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                Log.v("long clicked" , "pos" + position);

                AlertDialog.Builder alert = new AlertDialog.Builder(
                        getActivity());
                alert.setTitle("PAYING BILLS");
                alert.setMessage("Is this dept paid?");
                alert.setPositiveButton("SETTLE UP", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        User user = myDataSnapshot.child("users").child(auth.getCurrentUser().getUid()).getValue(User.class);
                        ArrayList<Expense> expenses = user.getExpenses();
                        expenses.remove(position);
                        myRef.child("users").child(auth.getCurrentUser().getUid()).child("expenses").setValue(expenses);


                        dialog.dismiss();

                    }
                });
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();



                return true;
            }
        });




        return rootView;
    }


    public ConstraintLayout getConstraintLayout(){
        return constraintLayout;
    }

    private void updateViews(User user) {

        String [] expenses= new String[user.getExpenses().size()];

        int i =0;
        for (Expense e : user.getExpenses()){
            expenses[i]=myDataSnapshot.child("users").child(e.getFriendid()).child("email").getValue(String.class) + " \n\nDEBT: " + e.getValue() +"€";
            i++;
        }





        Integer[] imageId = {
                R.drawable.common_google_signin_btn_icon_light_normal,
                R.drawable.check_split,
                R.drawable.check_split,
                R.drawable.check_split,
                R.drawable.check_split,
                R.drawable.check_split,
                R.drawable.check_split,
                R.drawable.check_split,
                R.drawable.check_split,



        };


        CustomList adapter = new CustomList(getActivity(),expenses,imageId);
        list.setAdapter(adapter);

    }



    private void initialize(View v) {



        constraintLayout = (ConstraintLayout) v.getRootView().findViewById(R.id.constraintLayout);

        list = (ListView) v.getRootView().findViewById(R.id.list);




    }
}
