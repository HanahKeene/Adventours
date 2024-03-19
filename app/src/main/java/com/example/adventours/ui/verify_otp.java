package com.example.adventours.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adventours.R;
import com.example.adventours.SigninActivity;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class verify_otp extends AppCompatActivity {

    private EditText[] otpFields;
    private ProgressBar progressBar;
    private Button verifyButton;

    private TextView countdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        countdown = findViewById(R.id.countdown);

        // Initialize views
        initViews();

        // Retrieve data from previous activity
        Intent intent = getIntent();
        String verificationId = intent.getStringExtra("verificationId");
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");
        String gname = intent.getStringExtra("gname");
        String lname = intent.getStringExtra("lname");
        String gender = intent.getStringExtra("gender");
        String age = intent.getStringExtra("age");
        String bday = intent.getStringExtra("bday");
        String city = intent.getStringExtra("city");
        String phone = intent.getStringExtra("phoneNumber");

        // Set up click listener for verify button
        verifyButton.setOnClickListener(view -> verifyOTP(verificationId, email, password, gname, lname, gender, age, bday, city, phone));
    }

    private void initViews() {
        otpFields = new EditText[] {
                findViewById(R.id.otp1),
                findViewById(R.id.otp2),
                findViewById(R.id.otp3),
                findViewById(R.id.otp4),
                findViewById(R.id.otp5),
                findViewById(R.id.otp6)
        };

        progressBar = findViewById(R.id.progressBar);
        verifyButton = findViewById(R.id.verify);

        setupOTPFields();
    }

    private void setupOTPFields() {
        for (int i = 0; i < otpFields.length; i++) {
            final int currentIndex = i;
            otpFields[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int count, int after) {
                    if (s.length() > 0 && currentIndex < otpFields.length - 1) {
                        otpFields[currentIndex + 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void verifyOTP(String verificationId, String email, String password, String gname, String lname, String gender, String age, String bday, String city, String phone) {
        // Validate OTP length
        StringBuilder otp = new StringBuilder();
        for (EditText field : otpFields) {
            String digit = field.getText().toString().trim();
            if (digit.isEmpty()) {
                Toast.makeText(this, "Please enter valid OTP.", Toast.LENGTH_SHORT).show();
                return;
            }
            otp.append(digit);
        }

        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);
        verifyButton.setVisibility(View.INVISIBLE);

        // Perform verification
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp.toString());
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    verifyButton.setEnabled(true);
                    if (task.isSuccessful()) {
                        // Verification successful, link email to phone number
                        linkEmailToPhoneNumber(email, password, gname, lname, gender, age, bday, city, phone);
                    } else {
                        // Verification failed
                        Toast.makeText(verify_otp.this, "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void linkEmailToPhoneNumber(String email, String password, String gname, String lname, String gender, String age, String bday, String city, String phone) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(email, password);
            user.linkWithCredential(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Email linking successful, save user details to Firestore
                            saveUserDetailsToFirestore(user.getUid(), email, gname, lname, gender, age, bday, city, phone);
                        } else {
                            // Email linking failed
                            Toast.makeText(verify_otp.this, "Failed to link email.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // User is not authenticated
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserDetailsToFirestore(String userId, String email, String gname, String lname, String gender, String age, String bday, String city, String phone) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("email", email);
        userDetails.put("firstName", gname);
        userDetails.put("lastName", lname);
        userDetails.put("gender", gender);
        userDetails.put("age", age);
        userDetails.put("birthdate", bday);
        userDetails.put("city", city);
        userDetails.put("phone", phone);
        userDetails.put("imageUrl", "");

        db.collection("users").document(userId)
                .set(userDetails)
                .addOnSuccessListener(aVoid -> {
                    // User details saved successfully
                    Intent resultIntent = new Intent();
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Failed to save user details
                    Toast.makeText(verify_otp.this, "Failed to save user details.", Toast.LENGTH_SHORT).show();
                });
    }
}
