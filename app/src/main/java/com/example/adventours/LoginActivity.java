package com.example.adventours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import com.example.adventours.MainActivity;
import com.example.adventours.R;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adventours.SigninActivity;
import com.example.adventours.ui.forgot_password;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.biometric.BiometricPrompt;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText email_txtfld, password_txtfld;
    private TextView forgotpass, fingerprint;
    private Button signupbtn, loginbtn;
    private ImageButton showpass;
    private FirebaseAuth mAuth;
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        email_txtfld = findViewById(R.id.email);
        password_txtfld = findViewById(R.id.Password);

        loginbtn = findViewById(R.id.loginbutton);
        loginbtn.setOnClickListener(view -> openHome());

        forgotpass = findViewById(R.id.forgotpass);
        forgotpass.setOnClickListener(view -> openforgotPass());

        signupbtn = findViewById(R.id.signupbutton);
        signupbtn.setOnClickListener(view -> openRegister());

        showpass = findViewById(R.id.showpassword);
        showpass.setOnClickListener(v -> togglePasswordVisibility());

        fingerprint = findViewById(R.id.fingerprint);
        fingerprint.setOnClickListener(view -> showFingerprintScanner());
    }

    private void showFingerprintScanner() {
        if (isBiometricPromptEnabled()) {
            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric Authentication")
                    .setSubtitle("Place your finger on the sensor")
                    .setNegativeButtonText("Cancel")
                    .build();

            BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
                    new BiometricPrompt.AuthenticationCallback() {
                        @Override
                        public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                            super.onAuthenticationError(errorCode, errString);
                            showToast("Authentication error: " + errString);
                        }

                        @Override
                        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                            super.onAuthenticationSucceeded(result);
                            showToast("Authentication succeeded");
                            openMainActivity();
                        }

                        @Override
                        public void onAuthenticationFailed() {
                            super.onAuthenticationFailed();
                            showToast("Authentication failed");
                        }
                    });

            biometricPrompt.authenticate(promptInfo);
        } else {
            showToast("Biometric authentication is not available on this device.");
        }
    }

    private boolean isBiometricPromptEnabled() {
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
        return fingerprintManager != null && fingerprintManager.isHardwareDetected()
                && fingerprintManager.hasEnrolledFingerprints();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void openforgotPass() {
        startActivity(new Intent(this, forgot_password.class));
    }

    private void openRegister() {
        startActivity(new Intent(this, SigninActivity.class));
    }

    private void openHome() {
        String email = String.valueOf(email_txtfld.getText());
        String password = String.valueOf(password_txtfld.getText());

        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            showToast("Enter email or password");
            return;
        }

        if (!TextUtils.isEmpty(email)) {
            // Email authentication
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                showToast("Login Successful");
                                openMainActivity();
                            } else {
                                showToast("Authentication failed.");
                            }
                        }
                    });
        } else {
            // Fingerprint authentication
            showFingerprintScanner();
        }
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void togglePasswordVisibility() {
        boolean isPasswordVisible = password_txtfld.getTransformationMethod() == null;

        // Change transformation method based on visibility state
        password_txtfld.setTransformationMethod(
                isPasswordVisible ? new PasswordTransformationMethod() : null
        );

        // Move the cursor to the end of the text to ensure it's always visible
        password_txtfld.setSelection(password_txtfld.getText().length());
        int imageResource = isPasswordVisible ? R.drawable.eye_solid : R.drawable.eye_slash_solid;
        showpass.setImageResource(imageResource);
    }
}
