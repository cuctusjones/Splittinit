package com.example.jens.splittinit.listAdapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.jens.splittinit.R;

public class LogList extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] logActivity;

    public LogList(Activity context,
                     String[] web) {
        super(context, R.layout.group_log, web);
        this.context = context;
        this.logActivity = web;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.group_log, null, true);
        TextView logMessage = rowView.findViewById(R.id.log);

        logMessage.setText(logActivity[position]);
        return rowView;
    }
}
