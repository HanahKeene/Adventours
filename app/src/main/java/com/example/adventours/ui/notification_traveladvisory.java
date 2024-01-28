package com.example.adventours.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.adventours.R;

public class notification_traveladvisory extends AppCompatActivity {

    TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_traveladvisory);

        back = findViewById(R.id.back);
        back.setOnClickListener(View -> finish());
    }
}