package com.example.adventours.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.AlgorithmParameters;
import java.security.KeyStore;
import com.example.adventours.R;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class EnableBiometrics extends AppCompatActivity {

    private static final String PREFS_NAME = "BiometricPrefs";
    private static final String BIOMETRIC_ENABLED_KEY = "biometricEnabled";

    private Switch biometrics;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_biometrics);

        biometrics = findViewById(R.id.enablebiomtrics);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        biometrics.setChecked(isBiometricEnabled());

        biometrics.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                enableBiometricLogin();
            } else {
                disableBiometricLogin();
            }
        });
    }

    private boolean isBiometricEnabled() {
        return sharedPreferences.getBoolean(BIOMETRIC_ENABLED_KEY, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isBiometricEnabled()) {
            biometrics.setChecked(isBiometricAvailable());
        }
    }

    private boolean isBiometricAvailable() {
        BiometricManager biometricManager = BiometricManager.from(this);
        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS;
    }

    private void enableBiometricLogin() {
        if (isBiometricAvailable()) {
            promptBiometricAuthentication();
        } else {
            Toast.makeText(this, "Biometric authentication is not available on this device.", Toast.LENGTH_SHORT).show();
            biometrics.setChecked(false);
        }
    }

    private void disableBiometricLogin() {
        sharedPreferences.edit().putBoolean(BIOMETRIC_ENABLED_KEY, false).apply();
        Toast.makeText(this, "Biometric login disabled.", Toast.LENGTH_SHORT).show();
    }

    private void promptBiometricAuthentication() {
        Cipher cipher = getCipher();
        if (cipher != null) {
            BiometricPrompt.CryptoObject cryptoObject = new BiometricPrompt.CryptoObject(cipher);

            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Authenticate to enable biometric login")
                    .setSubtitle("Please use your biometrics to proceed")
                    .setNegativeButtonText("Cancel")
                    .build();

            BiometricPrompt biometricPrompt = new BiometricPrompt(this, ContextCompat.getMainExecutor(this), new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Toast.makeText(EnableBiometrics.this, "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    byte[] fingerprintData = retrieveFingerprintData(result);
                    showToastWithFingerprintData(fingerprintData);
                    linkBiometricToFirebase(fingerprintData);
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(EnableBiometrics.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            });

            biometricPrompt.authenticate(promptInfo, cryptoObject);
        } else {
            Log.e("BiometricAuthentication", "Cipher is null");
            Toast.makeText(this, "Failed to initialize cryptographic object", Toast.LENGTH_SHORT).show();
        }
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

    private void showToastWithFingerprintData(byte[] fingerprintData) {
        if (fingerprintData != null) {
            String hexString = Base64.encodeToString(fingerprintData, Base64.DEFAULT);
            Toast.makeText(EnableBiometrics.this, "Fingerprint Data: " + hexString, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(EnableBiometrics.this, "Fingerprint Data is null", Toast.LENGTH_SHORT).show();
        }
    }
    private void linkBiometricToFirebase(byte[] fingerprintData) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            if (fingerprintData == null) {
                showToast("Fingerprint data is null. Unable to link to Firebase.");
                return;
            }
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(userId);

            // Fetch existing document data
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Map<String, Object> data = documentSnapshot.getData();
                    if (data != null) {
                        // Merge existing data with fingerprint data
                        data.put("fingerprintData", Base64.encodeToString(fingerprintData, Base64.DEFAULT));

                        // Update the document with merged data
                        docRef.set(data)
                                .addOnSuccessListener(aVoid -> {
                                    sharedPreferences.edit().putBoolean(BIOMETRIC_ENABLED_KEY, true).apply();
                                    showToast("Biometric data linked to Firebase.");
                                })
                                .addOnFailureListener(e -> {
                                    showToast("Failed to link biometric data to Firebase: " + e.getMessage());
                                });
                    }
                } else {
                    showToast("Document does not exist.");
                }
            }).addOnFailureListener(e -> {
                showToast("Failed to fetch document: " + e.getMessage());
            });
        } else {
            showToast("User is not authenticated.");
        }
    }


    private Cipher getCipher() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(new KeyGenParameterSpec.Builder("my_key", KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            SecretKey secretKey = keyGenerator.generateKey();

            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            return cipher;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
