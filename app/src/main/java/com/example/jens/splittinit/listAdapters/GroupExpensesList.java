package com.example.jens.splittinit.listAdapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jens.splittinit.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupExpensesList extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;

    public GroupExpensesList(Activity context,
                     String[] web, Integer[] imageId) {
        super(context, R.layout.group_list, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.group_checkbox_list, null, true);
        CheckBox memberName = rowView.findViewById(R.id.memberName);

        CircleImageView profilePicture = rowView.findViewById(R.id.profileImage);


        memberName.setText(web[position]);
        profilePicture.setImageResource(imageId[position]);
        return rowView;
    }
}
