package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;

public class SetupBiometrics extends AppCompatActivity {
    
    TextView back;
    
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_biometrics);
        
        back = findViewById(R.id.back);
        register = findViewById(R.id.register);


        
        register.setOnClickListener(View -> opensetting());
    }

    private void opensetting() {

        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        startActivity(intent);
        finish();
    }
}