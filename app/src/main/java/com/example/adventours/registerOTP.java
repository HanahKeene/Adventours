package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class registerOTP extends AppCompatActivity {

    ImageButton backbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerotp);

        backbtn = findViewById(R.id.backButton);
        backbtn.setOnClickListener(view -> backtoform());

    }

    private void backtoform() {

        Intent intent = new Intent(this, SigninActivity.class);
        startActivity(intent);
    }


}