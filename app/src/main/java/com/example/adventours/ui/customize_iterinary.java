package com.example.adventours.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.adventours.R;
import com.google.android.material.textfield.TextInputEditText;

public class customize_iterinary extends AppCompatActivity {
    String itineraryName;
    String StartDate;
    String EndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customize_iterinary);
    }
}