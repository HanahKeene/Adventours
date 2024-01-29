package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.adventours.ui.EnableBiometrics;

public class SettingsActivity extends AppCompatActivity {

    TextView back, biometrics, push_notif, delete_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        back = findViewById(R.id.back);
        biometrics = findViewById(R.id.biometricsbtn);
        push_notif = findViewById(R.id.pushnotifbtn);
        delete_account = findViewById(R.id.deleteaccntbtn);


        back.setOnClickListener(View -> finish());
        biometrics.setOnClickListener(View -> openbiomtrics());
        push_notif.setOnClickListener(View -> openpushnotif());
        delete_account.setOnClickListener(View -> deleteacc());

    }

    private void deleteacc() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.prompt_delete_account);
        dialog.show();
    }

    private void openpushnotif() {

        Intent intent = new Intent(this, push_notifications.class);
        startActivity(intent);
    }

    private void openbiomtrics() {

        Intent intent = new Intent(this, EnableBiometrics.class);
        startActivity(intent);
    }
}