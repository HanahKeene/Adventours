package com.example.adventours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
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
    ImageButton showpassbtn;
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

        showpassbtn = findViewById(R.id.showpassword);
        showpassbtn.setOnClickListener(View -> showpassword());

        loginButton = findViewById(R.id.loginbutton);
        loginButton.setOnClickListener(view -> login());

        forgotPassword = findViewById(R.id.forgotpass);
        forgotPassword.setOnClickListener(view -> openForgotPassword());

        signupButton = findViewById(R.id.signupbutton);
        signupButton.setOnClickListener(view -> openRegister());

    }

    private void showpassword() {

        boolean isPasswordVisible = false;

        isPasswordVisible = !isPasswordVisible;

        // Change transformation method based on visibility state
        passwordTxtField.setTransformationMethod(
                isPasswordVisible ? null : new PasswordTransformationMethod()
        );

        // Move the cursor to the end of the text to ensure it's always visible
        passwordTxtField.setSelection(passwordTxtField.getText().length());
        int imageResource = isPasswordVisible ? R.drawable.eye_slash_solid : R.drawable.eye_solid;
        showpassbtn.setImageResource(imageResource);
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
