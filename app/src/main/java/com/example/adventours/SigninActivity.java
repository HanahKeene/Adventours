package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SigninActivity extends AppCompatActivity {

    EditText username, email, ageNumber, birthday, City, phone;
    RadioGroup gender;
    Button verify, submit, terms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        ageNumber = findViewById(R.id.ageNumber);
        birthday = findViewById(R.id.birthday);
        City = findViewById(R.id.City);
        phone = findViewById(R.id.phone);
        gender = findViewById(R.id.gender);

        verify = findViewById(R.id.verifyButton);
        verify.setOnClickListener(view -> openVerifier());

        submit = findViewById(R.id.submit);
        submit.setOnClickListener(view -> register());

        terms = findViewById(R.id.terms);
        terms.setOnClickListener(view -> openterms());

    }

    private void openterms() {
    }

    private void register() {
    }

    private void openVerifier() {

        Intent intent = new Intent(this, registerOTP.class);
        startActivity(intent);
    }
}