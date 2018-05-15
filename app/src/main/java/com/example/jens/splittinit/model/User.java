package com.example.jens.splittinit.model;


import android.net.Uri;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class User {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Uri image;
    private ArrayList<String> friends;
    private ArrayList<Expense> expenses;




    public User() {
        friends=new ArrayList<>();
        expenses = new ArrayList<>();


    }

    public User(String id, String firstName, String lastName, String email) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        friends=new ArrayList<>();
        expenses = new ArrayList<>();
    }

    public User(String id, String firstName, String lastName, String email, Uri image) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.image = image;
        friends=new ArrayList<>();
        expenses = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(ArrayList<Expense> expenses) {
        this.expenses = expenses;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", image=" + image +
                '}';
    }
}
