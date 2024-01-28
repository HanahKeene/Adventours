package com.example.adventours.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.adventours.R;

public class notification_activities extends AppCompatActivity {

    TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activities);

        back = findViewById(R.id.back);
        back.setOnClickListener(View -> finish());
    }
}