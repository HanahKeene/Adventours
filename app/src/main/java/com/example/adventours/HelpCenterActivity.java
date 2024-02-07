package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.adventours.ui.check_reservation;
import com.example.adventours.ui.faqs_activity;

public class HelpCenterActivity extends AppCompatActivity {

    TextView back;

    TextView checkreservebtn, faqs;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);

        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        checkreservebtn = findViewById(R.id.check_reservationbtn);
        faqs = findViewById(R.id.faqsbtn);

        checkreservebtn.setOnClickListener(View -> checkreserve());
        faqs.setOnClickListener(View -> faq());
        back = findViewById(R.id.back);
        back.setOnClickListener(View -> finish());
    }

    private void faq() {

        Intent intent = new Intent(this, faqs_activity.class);
        startActivity(intent);
    }

    private void checkreserve() {

        Intent intent = new Intent(this, check_reservation.class);
        startActivity(intent);
    }
}