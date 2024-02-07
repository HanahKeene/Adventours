package com.example.adventours.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adventours.R;

public class faqs_activity extends AppCompatActivity {

    TextView back;
    SharedPreferences sharedPreferences;

    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);

        logo = findViewById(R.id.logo);

        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            logo.setImageResource(R.drawable.logowithwhitetext);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            logo.setImageResource(R.drawable.logowtext);
        }

        back = findViewById(R.id.back);
        back.setOnClickListener(View -> finish());
    }
}