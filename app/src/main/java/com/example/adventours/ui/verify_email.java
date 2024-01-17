package com.example.adventours.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.adventours.R;

public class verify_email extends AppCompatActivity {

    Button verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_email);

        verify = findViewById(R.id.verify);

        verify.setOnClickListener( View -> opennewPassword());

    }

    private void opennewPassword() {
        Intent intent = new Intent(this, new_password.class);
        startActivity(intent);
    }
}