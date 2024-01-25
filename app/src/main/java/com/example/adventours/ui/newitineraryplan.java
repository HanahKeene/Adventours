package com.example.adventours.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.adventours.R;

public class newitineraryplan extends AppCompatActivity {

    TextView choosedestination;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newitineraryplan);

        choosedestination = findViewById(R.id.next);

        choosedestination.setOnClickListener(View -> choosedestination());
    }

    private void choosedestination() {
        Intent intent = new Intent(this, choosedestination.class);
        startActivity(intent);
    }
}