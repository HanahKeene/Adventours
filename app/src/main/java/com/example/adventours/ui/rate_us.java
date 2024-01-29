package com.example.adventours.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;

import com.example.adventours.R;

public class rate_us extends AppCompatActivity {
    
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);
        
        submit = findViewById(R.id.submitrate);
        submit.setOnClickListener(View -> submitratings());
        
    }

    private void submitratings() {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.prompt_thanksfortherate);
        dialog.show();
    }
}