package com.example.adventours.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import com.example.adventours.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.Executor;

public class EnableBiometrics extends AppCompatActivity {

    private static final String PREFS_NAME = "BiometricPrefs";
    private static final String BIOMETRIC_ENABLED_KEY = "biometricEnabled";

    Switch biometrics;
    Executor executor;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_biometrics);

        biometrics = findViewById(R.id.enablebiomtrics);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Set up executor
        executor = ContextCompat.getMainExecutor(this);

        // Set the initial state of the switch based on stored preferences
        biometrics.setChecked(isBiometricEnabled());

        // Set up switch listener
        biometrics.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startBiometricAuthentication();
                } else {
                    // Handle case where user turned off biometric authentication
                }
                // Store the switch state in preferences
                saveBiometricEnabled(isChecked);
            }
        });

        // Check if biometric sensor is available and set the initial state of the switch
        checkBiometricSensorAvailability();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Re-check biometric sensor availability each time the activity resumes
        checkBiometricSensorAvailability();
    }

    private void checkBiometricSensorAvailability() {
        BiometricManager biometricManager = BiometricManager.from(this);
        int biometricAvailability = biometricManager.canAuthenticate();

        if (biometricAvailability == BiometricManager.BIOMETRIC_SUCCESS) {
            // Fingerprint sensor is available, enable the switch
            biometrics.setEnabled(true);
        } else {
            // Fingerprint sensor is not available, disable the switch
            biometrics.setChecked(false);
            biometrics.setEnabled(false);
        }
    }

    private void startBiometricAuthentication() {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authenticate using Biometrics")
                .setSubtitle("Place your finger on the sensor to authenticate.")
                .setDescription("This will authenticate you using your enrolled biometric data.")
                .setNegativeButtonText("Cancel")
                .build();

        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                showToast("Biometric authentication error: " + errString);
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                showToast("Biometric authentication succeeded");

                // Upon successful biometric authentication, link with Firebase
                linkBiometricWithFirebase();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                showToast("Biometric authentication failed. Please try again.");
            }
        });

        biometricPrompt.authenticate(promptInfo);
    }

    private void linkBiometricWithFirebase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String biometricIdentifier = currentUser.getUid();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(biometricIdentifier)
                    .build();
            currentUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            showToast("Biometric credential linked successfully");
                        } else {
                            showToast("Biometric credential linking failed");
                        }
                    });
        }
    }



    private void saveBiometricEnabled(boolean enabled) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(BIOMETRIC_ENABLED_KEY, enabled);
        editor.apply();
    }

    private boolean isBiometricEnabled() {
        return sharedPreferences.getBoolean(BIOMETRIC_ENABLED_KEY, false);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
