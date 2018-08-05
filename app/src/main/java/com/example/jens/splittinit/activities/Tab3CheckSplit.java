package com.example.jens.splittinit.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.jens.splittinit.R;

public class Tab3CheckSplit extends Fragment {

    public TextView splittedValue;

    public NumberPicker tipNumberPicker, pplNumberPicker;

    public EditText checkAmount;

    public Button computeButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.tab3checksplit, container, false);

        //initializing stuff
        initialize(rootView);


        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();

        computeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAmount.getText() == null) {
                    double tip = tipNumberPicker.getValue() / 100;
                    double numberOfPpl = pplNumberPicker.getValue();
                    double invoiceAmount = Double.parseDouble(checkAmount.getText().toString());
                    double tmp = (float) Math.round(((((invoiceAmount * tip) + invoiceAmount) / numberOfPpl) * 100) / 100);
                    String amount = Double.toString(tmp);
                    splittedValue.setText(amount + "€");
                }
            }
        });
    }


    private void initialize(View v) {

        splittedValue = (TextView) v.findViewById(R.id.splittedValue);
        tipNumberPicker = (NumberPicker) v.findViewById(R.id.tipNumberPicker);
        pplNumberPicker = (NumberPicker) v.findViewById(R.id.pplNumberPicker);
        checkAmount = (EditText) v.findViewById(R.id.checkAmount);
        computeButton = (Button) v.findViewById(R.id.computeButton);

        tipNumberPicker.setMaxValue(50);
        tipNumberPicker.setMinValue(1);
        tipNumberPicker.setValue(20);

        pplNumberPicker.setMaxValue(10);
        pplNumberPicker.setMinValue(2);
        pplNumberPicker.setValue(5);
    }


}
