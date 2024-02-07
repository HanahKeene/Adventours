package com.example.adventours.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adventours.R;

public class about_us extends AppCompatActivity {

    TextView back;
    ImageView logo, logo2;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);

        logo = findViewById(R.id.logo);
        logo2 = findViewById(R.id.logo2);

        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            logo.setImageResource(R.drawable.logowithwhitetext);
            logo2.setImageResource(R.drawable.logowithwhitetext);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            logo.setImageResource(R.drawable.logowtext);
            logo2.setImageResource(R.drawable.logowtext);
        }

        back = findViewById(R.id.back);
        back.setOnClickListener(View -> finish());
    }
}