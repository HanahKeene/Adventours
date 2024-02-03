package com.example.adventours.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adventours.LoginActivity;
import com.example.adventours.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class forgot_password extends AppCompatActivity {
    TextView back;
    EditText emailfld;

    ProgressBar forgetPasswordProgressbar;
    FirebaseAuth mAuth;
    String email;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        back = findViewById(R.id.back);
        send = findViewById(R.id.sendbtn);
        emailfld = findViewById(R.id.emailtxtfld);
        forgetPasswordProgressbar = findViewById(R.id.forgetPasswordProgressbar);
        mAuth = FirebaseAuth.getInstance();

        back.setOnClickListener(View -> finish());
        send.setOnClickListener(view -> sendotp());

    }

    private void sendotp() {

        email = emailfld.getText().toString().trim();
        if (!TextUtils.isEmpty(email)) {
            ResetPassword();
        } else {
            emailfld.setError("Email field can't be empty");
        }
    }

    private void ResetPassword() {

        forgetPasswordProgressbar.setVisibility(View.VISIBLE);
        send.setVisibility(View.INVISIBLE);

        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(forgot_password.this, "Reset Password link has been sent to your registered Email", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(forgot_password.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(forgot_password.this, "Error :- " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        forgetPasswordProgressbar.setVisibility(View.INVISIBLE);
                        send.setVisibility(View.VISIBLE);
                    }
                });
    }


}