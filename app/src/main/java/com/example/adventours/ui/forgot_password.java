package com.example.adventours.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.adventours.R;

public class forgot_password extends AppCompatActivity {

    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        send = findViewById(R.id.sendbtn);

        //before sending otp the system must check first if email is registered in the app.
        send.setOnClickListener(view -> sendotp());

    }

    private void sendotp() {

        Intent intent = new Intent(this, verify_email.class);
        startActivity(intent);
    }


}