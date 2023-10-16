package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button = findViewById(R.id.loginbutton);
        button.setOnClickListener(view -> openlogin());
    }
    private void openlogin() {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
    }

        }
