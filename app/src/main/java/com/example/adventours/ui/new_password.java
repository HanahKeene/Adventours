package com.example.adventours.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.adventours.LoginActivity;
import com.example.adventours.R;
import com.example.adventours.SigninActivity;

public class new_password extends AppCompatActivity {

    Button changepass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        changepass = findViewById(R.id.changepassbtn);

        changepass.setOnClickListener(View -> openlogin());
    }

    private void openlogin() {
        Intent  intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}