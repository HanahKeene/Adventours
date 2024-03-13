package com.example.adventours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
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
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.biometric.BiometricPrompt;

import java.security.AlgorithmParameters;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailTxtField, passwordTxtField;
    private Button loginButton, signupButton;
    private TextView forgotPassword, fingerprintAuth;
    private FirebaseAuth mAuth;
    private final Executor executor = Executors.newSingleThreadExecutor();

    private SharedPreferences sharedPreferences;

    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private static final String PREF_EMAIL_KEY = "last_email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);

        // Retrieve the last logged-in email and set it in the EditText
        String lastEmail = sharedPreferences.getString(PREF_EMAIL_KEY, "");
        emailTxtField = findViewById(R.id.email);
        emailTxtField.setText(lastEmail);


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

    private void authenticateWithFingerprint() {
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                showToastOnUiThread("Authentication error: " + errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                showToastOnUiThread("Fingerprint authentication successful!");
                // Proceed with login using fingerprint authentication
                super.onAuthenticationSucceeded(result);
                byte[] fingerprintData = retrieveFingerprintData(result);
                loginWithBiometric(fingerprintData);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                showToastOnUiThread("Fingerprint authentication failed.");
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Fingerprint Authentication")
                .setSubtitle("Place your finger on the fingerprint sensor")
                .setNegativeButtonText("Cancel")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private byte[] retrieveFingerprintData(BiometricPrompt.AuthenticationResult result) {
        try {
            BiometricPrompt.CryptoObject cryptoObject = result.getCryptoObject();
            if (cryptoObject != null) {
                Cipher cipher = cryptoObject.getCipher();
                if (cipher != null) {
                    AlgorithmParameters params = cipher.getParameters();
                    if (params != null) {
                        IvParameterSpec ivParameterSpec = params.getParameterSpec(IvParameterSpec.class);
                        if (ivParameterSpec != null) {
                            return ivParameterSpec.getIV();
                        } else {
                            Log.e("RetrieveFingerprint", "IvParameterSpec is null");
                        }
                    } else {
                        Log.e("RetrieveFingerprint", "AlgorithmParameters is null");
                    }
                } else {
                    Log.e("RetrieveFingerprint", "Cipher is null");
                }
            } else {
                Log.e("RetrieveFingerprint", "CryptoObject is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("RetrieveFingerprint", "Exception: " + e.getMessage());
        }
        return null;
    }

    private void loginWithBiometric(byte[] fingerprintData) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(currentUser.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String storedFingerprintData = documentSnapshot.getString("fingerprintData");
                            if (storedFingerprintData != null) {
                                // Decode the stored fingerprint data from Base64
                                byte[] storedFingerprintBytes = Base64.decode(storedFingerprintData, Base64.DEFAULT);
                                // Compare the two byte arrays
                                if (Arrays.equals(storedFingerprintBytes, fingerprintData)) {
                                    // Fingerprint authentication successful, proceed with login
                                    showToast("Fingerprint authentication successful!");
                                    openMainActivity();
                                } else {
                                    // Fingerprint data mismatch
                                    showToast("Fingerprint data mismatch. Unable to authenticate with fingerprint.");
                                }
                            } else {
                                // Stored fingerprint data is null
                                showToast("Stored fingerprint data is null. Unable to authenticate with fingerprint.");
                            }
                        } else {
                            // User document not found in Firestore
                            showToast("User document not found in Firestore.");
                        }
                    })
                    .addOnFailureListener(e -> {
                        showToast("Failed to retrieve user data from Firestore: " + e.getMessage());
                    });
        } else {
            showToast("User not authenticated. Unable to authenticate with fingerprint.");
        }
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
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(PREF_EMAIL_KEY, email);
                        editor.apply();

                        showToast("Login successful!");

                        // Proceed with fingerprint authentication
                        authenticateWithFingerprint();
                    } else {
                        showToast("Login failed. Please check your credentials.");
                    }
                });
    }



    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void openForgotPassword() {
        Intent intent = new Intent(this, forgot_password.class);
        startActivity(intent);
    }

    private void openRegister() {
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }


    private void showToastOnUiThread(String message) {
        runOnUiThread(() -> showToast(message));
    }
}
