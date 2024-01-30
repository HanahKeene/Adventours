package com.example.adventours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

    TextInputEditText email_txtfld, password_txtfld;

    TextView forgotpass, fingerprint;
    Button signupbtn, loginbtn;

    ImageButton showpass;

    FirebaseAuth mAuth;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
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
        showpass.setOnClickListener(new View.OnClickListener() {
            boolean isPasswordVisible = false;

            @Override
            public void onClick(View v) {
                isPasswordVisible = !isPasswordVisible;

                // Change transformation method based on visibility state
                password_txtfld.setTransformationMethod(
                        isPasswordVisible ? null : new PasswordTransformationMethod()
                );

                // Move the cursor to the end of the text to ensure it's always visible
                password_txtfld.setSelection(password_txtfld.getText().length());
                int imageResource = isPasswordVisible ? R.drawable.eye_slash_solid : R.drawable.eye_solid;
                showpass.setImageResource(imageResource);
            }
        });

        fingerprint = findViewById(R.id.fingerprint);
        fingerprint.setOnClickListener(view -> showFingerprintScanner());

        showFingerprintScanner();
    }

    private void showFingerprintScanner() {
        View biometricsView = LayoutInflater.from(this).inflate(R.layout.biometrics_screen, null);

        Dialog dialog = new Dialog(this);
        dialog.setContentView(biometricsView);
        dialog.show();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);

        Executor executor = Executors.newSingleThreadExecutor();
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Biometric authentication succeeded
                openHome();
                dialog.dismiss();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Place your finger on the sensor")
                .setNegativeButtonText("Cancel")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }



    private void openforgotPass() {

        Intent intent = new Intent(this, forgot_password.class);
        startActivity(intent);
    }

    private void openHome()
    {

        String email, password;

        email = String.valueOf(email_txtfld.getText());
        password = String.valueOf(password_txtfld.getText());

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void openRegister()
    {
        Intent intent= new Intent(this, SigninActivity.class);
        startActivity(intent);
    }
}

