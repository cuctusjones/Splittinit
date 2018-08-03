package com.example.jens.splittinit.listAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.jens.splittinit.R;
import com.example.jens.splittinit.activities.Tab2Group;


import java.util.ArrayList;
import java.util.List;

public class GroupList extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;

    public GroupList(Activity context,
                      String[] web, Integer[] imageId) {
        super(context, R.layout.group_list, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.group_list, null, true);
        TextView groupName = (TextView) rowView.findViewById(R.id.groupName);

        ImageView groupImage = (ImageView) rowView.findViewById(R.id.groupImage);


        groupName.setText(web[position]);
        groupImage.setImageResource(imageId[position]);
        return rowView;
    }
}
