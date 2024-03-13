package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adventours.ui.EnableBiometrics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Switch switcher;
    boolean nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    TextView back, biometrics, push_notif, delete_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();

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
        delete_account.setOnClickListener(View -> deleteAccount());

    }

    private void deleteAccount() {
        Dialog firstDialog = new Dialog(this);
        firstDialog.setContentView(R.layout.prompt_delete_account);
        firstDialog.show();

        Button insideDialogBtn = firstDialog.findViewById(R.id.delete);

        insideDialogBtn.setOnClickListener(View -> checkForExistingReservations());
    }


    private void checkForExistingReservations() {


        FirebaseFirestore db =  FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();

        db.collection("Hotel Reservation")
                .whereEqualTo("UserID", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            showReservationWarning();
                        } else {
                            checkOtherReservations();
                        }
                    } else {
                        // Handle query error
                    }
                });
    }

    private void checkOtherReservations() {

        FirebaseFirestore db =  FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();

        db.collection("Restaurant Reservation")
                .whereEqualTo("UserID", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            // User has other reservations, display warning message
                            showReservationWarning();
                        } else {
                            // No reservations found, proceed with account deletion
                            retrieveMobileNumber();
                        }
                    } else {
                        // Handle query error
                    }
                });
    }

    private void retrieveMobileNumber() {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userId = currentUser.getUid();

            db.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String userMobileNumber = documentSnapshot.getString("phone");
//                            deleteAccountByMatchingPhoneNumber(userMobileNumber);
                        }
                    });
         }
        }

    private void removeUser(String userMobileNumber) {

    }

    private void deleteUserDataFromFirestore() {

    }

    private void deleteUserDataFromOtherPlaces() {

    }

    private void showReservationWarning() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.prompt_you_have_active_reservation);
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