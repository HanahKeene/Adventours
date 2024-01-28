package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.adventours.ui.check_reservation;
import com.example.adventours.ui.faqs_activity;

public class HelpCenterActivity extends AppCompatActivity {

    TextView back;

    TextView checkreservebtn, faqs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);

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