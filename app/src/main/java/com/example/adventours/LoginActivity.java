package com.example.adventours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adventours.MainActivity;
import com.example.adventours.R;
import com.example.adventours.ui.forgot_password;
import com.example.adventours.SigninActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.biometric.BiometricPrompt;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailTxtField, passwordTxtField;
    private Button loginButton, signupButton;
    private TextView forgotPassword, fingerprintAuth;
    private FirebaseAuth mAuth;
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        emailTxtField = findViewById(R.id.email);
        passwordTxtField = findViewById(R.id.Password);

        loginButton = findViewById(R.id.loginbutton);
        loginButton.setOnClickListener(view -> login());

        forgotPassword = findViewById(R.id.forgotpass);
        forgotPassword.setOnClickListener(view -> openForgotPassword());

        signupButton = findViewById(R.id.signupbutton);
        signupButton.setOnClickListener(view -> openRegister());

        fingerprintAuth = findViewById(R.id.fingerprint);
        fingerprintAuth.setOnClickListener(view -> authenticateWithFingerprint());
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            openMainActivity();
        }
    }

    private void login() {
        String email = emailTxtField.getText().toString();
        String password = passwordTxtField.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            showToast("Please enter both email and password.");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        showToast("Login successful!");
                        openMainActivity();
                    } else {
                        showToast("Login failed. Please check your credentials.");
                    }
                });
    }

    private void authenticateWithFingerprint() {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authenticate using Biometrics")
                .setSubtitle("Place your finger on the sensor to authenticate.")
                .setDescription("This will authenticate you using your enrolled biometric data.")
                .setNegativeButtonText("Cancel")
                .build();

        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        showToastOnUiThread("Authentication error: " + errString);
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        showToastOnUiThread("Authentication succeeded");
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            String linkedBiometricData = user.getDisplayName(); // Retrieve linked biometric data from Firebase
                            String authenticatedBiometricData = ""; // Retrieve authenticated biometric data from the authentication result

                            // Perform biometric data comparison
                            if (authenticatedBiometricData.equals(linkedBiometricData)) {
                                openMainActivity(); // Open MainActivity if biometric data matches
                            } else {
                                showToastOnUiThread("Biometric data does not match."); // Show error message if biometric data doesn't match
                            }
                        } else {
                            showToastOnUiThread("User not logged in."); // Show error message if user is not logged in
                        }
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        showToastOnUiThread("Authentication failed. Please try again.");
                    }
                });

        biometricPrompt.authenticate(promptInfo);
    }


    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Finish the LoginActivity to prevent going back to it on back press
    }

    private void openForgotPassword() {
        Intent intent = new Intent(this, forgot_password.class);
        startActivity(intent);
    }

    private void openRegister() {
        Intent intent = new Intent(this, SigninActivity.class);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showToastOnUiThread(String message) {
        runOnUiThread(() -> showToast(message));
    }
}
