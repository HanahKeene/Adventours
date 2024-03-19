package com.example.adventours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adventours.MainActivity;
import com.example.adventours.R;
import com.example.adventours.ui.EnableBiometrics;
import com.example.adventours.ui.forgot_password;
import com.example.adventours.SigninActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.security.AlgorithmParameters;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
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

//        fingerprintAuth = findViewById(R.id.fingerprint);
//        fingerprintAuth.setOnClickListener(view -> authenticateWithFingerprint());
    }

//    private void authenticateWithFingerprint() {
//        Cipher cipher = getCipher();
//        if (cipher != null) {
//            BiometricPrompt.CryptoObject cryptoObject = new BiometricPrompt.CryptoObject(cipher);
//
//            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
//                    .setTitle("Authenticate to enable biometric login")
//                    .setSubtitle("Please use your biometrics to proceed")
//                    .setNegativeButtonText("Cancel")
//                    .build();
//
//            BiometricPrompt biometricPrompt = new BiometricPrompt(this, ContextCompat.getMainExecutor(this), new BiometricPrompt.AuthenticationCallback() {
//                @Override
//                public void onAuthenticationError(int errorCode, CharSequence errString) {
//                    super.onAuthenticationError(errorCode, errString);
//                    Toast.makeText(LoginActivity.this, "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
//                    super.onAuthenticationSucceeded(result);
//                    byte[] fingerprintData = retrieveFingerprintData(result);
//                    if (fingerprintData != null) {
//                        String email = emailTxtField.getText().toString().trim();
//                        if (!TextUtils.isEmpty(email)) {
//                            loginWithBiometric(email, fingerprintData);
//                        } else {
//                            showToast("Please enter your email to proceed with fingerprint authentication.");
//                        }
//                    } else {
//                        showToast("Failed to retrieve fingerprint data.");
//                    }
//                }
//
//                @Override
//                public void onAuthenticationFailed() {
//                    super.onAuthenticationFailed();
//                    Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            biometricPrompt.authenticate(promptInfo, cryptoObject);
//        } else {
//            Log.e("BiometricAuthentication", "Cipher is null");
//            Toast.makeText(this, "Failed to initialize cryptographic object", Toast.LENGTH_SHORT).show();
//        }
//    }

//    private void loginWithBiometric(String email, byte[] fingerprintData) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("users").whereEqualTo("email", email).get()  // Assuming email is a field in the document
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    if (!queryDocumentSnapshots.isEmpty()) {
//                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
//                        String uid = documentSnapshot.getId();
//                        String storedFingerprintData = documentSnapshot.getString("fingerprintData");
//                        if (storedFingerprintData != null) {
//                            byte[] storedFingerprintBytes = Base64.decode(storedFingerprintData, Base64.DEFAULT);
//                            Log.d("FingerprintDebug", "Stored Fingerprint Bytes: " + Arrays.toString(storedFingerprintBytes));
//
//                            Log.d("FingerprintDebug", "Fingerprint Data Bytes: " + Arrays.toString(fingerprintData));
//
//                            if (Arrays.equals(storedFingerprintBytes, fingerprintData)) {
//                                showToast("Fingerprint authentication successful!");
//                                openMainActivity();
//                            } else {
//                                showToast("Fingerprint data mismatch. Unable to authenticate with fingerprint.");
//                            }
//                        } else {
//                            showToast("Stored fingerprint data is null. Unable to authenticate with fingerprint.");
//                        }
//                    } else {
//                        showToast("User document not found in Firestore.");
//                    }
//                })
//                .addOnFailureListener(e -> showToast("Failed to retrieve user data from Firestore: " + e.getMessage()));
//    }

//    private byte[] retrieveFingerprintData(BiometricPrompt.AuthenticationResult result) {
//        try {
//            BiometricPrompt.CryptoObject cryptoObject = result.getCryptoObject();
//            if (cryptoObject != null) {
//                Cipher cipher = cryptoObject.getCipher();
//                if (cipher != null) {
//                    AlgorithmParameters params = cipher.getParameters();
//                    if (params != null) {
//                        IvParameterSpec ivParameterSpec = params.getParameterSpec(IvParameterSpec.class);
//                        if (ivParameterSpec != null) {
//                            return ivParameterSpec.getIV();
//                        } else {
//                            Log.e("RetrieveFingerprint", "IvParameterSpec is null");
//                        }
//                    } else {
//                        Log.e("RetrieveFingerprint", "AlgorithmParameters is null");
//                    }
//                } else {
//                    Log.e("RetrieveFingerprint", "Cipher is null");
//                }
//            } else {
//                Log.e("RetrieveFingerprint", "CryptoObject is null");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("RetrieveFingerprint", "Exception: " + e.getMessage());
//        }
//        return null;
//    }

//    private Cipher getCipher() {
//        try {
//            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
//            keyGenerator.init(new KeyGenParameterSpec.Builder("my_key", KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
//                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
//                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
//                    .build());
//            SecretKey secretKey = keyGenerator.generateKey();
//
//            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
//            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//
//            return cipher;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }



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
                        openMainActivity();
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
