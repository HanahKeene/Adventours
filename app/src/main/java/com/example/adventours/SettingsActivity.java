package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.example.adventours.ui.EnableBiometrics;

public class SettingsActivity extends AppCompatActivity {

    Switch switcher;
    boolean nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    TextView back, biometrics, push_notif, delete_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        back = findViewById(R.id.back);
        biometrics = findViewById(R.id.biometricsbtn);
        push_notif = findViewById(R.id.pushnotifbtn);
        delete_account = findViewById(R.id.deleteaccntbtn);
        switcher = findViewById(R.id.switch1);

        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);

        if (nightMode)
        {
            switcher.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nightMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", false);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", true);
                }
                editor.apply();
            }
        });

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