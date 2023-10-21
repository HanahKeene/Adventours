package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    Button signupbtn, loginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginbtn = findViewById(R.id.loginbutton);
        loginbtn.setOnClickListener(view -> openHome());

        signupbtn = findViewById(R.id.signupbutton);
        signupbtn.setOnClickListener(view -> openRegister());
    }
    private void openHome()
    {
        Intent intent= new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void openRegister()
    {
        Intent intent= new Intent(this, SigninActivity.class);
        startActivity(intent);
    }
}


