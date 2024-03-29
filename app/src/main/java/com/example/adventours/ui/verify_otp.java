package com.example.adventours.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.CountDownTimer;

import com.bumptech.glide.Glide;
import com.example.adventours.MainActivity;
import com.example.adventours.R;
import com.example.adventours.SigninActivity;
import com.example.adventours.User;
import com.example.adventours.interestform;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class verify_otp extends AppCompatActivity {

    private EditText[] otpFields;
    private ProgressBar progressBar;
    private Button verifyButton;
    private TextView countdown, resendButton;
    private CountDownTimer countDownTimer;
    private Dialog loadingDialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean isTimerRunning = false;
    private final long COUNTDOWN_TIME = 60000; // 60 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        countdown = findViewById(R.id.countdown);


        startCountdown();

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

        Toast.makeText(this, "Phone" + phone, Toast.LENGTH_SHORT).show();

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

    private void startCountdown() {
        countDownTimer = new CountDownTimer(COUNTDOWN_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsLeft = millisUntilFinished / 1000;
                countdown.setText("Resend OTP in " + secondsLeft + " seconds");
            }

            @Override
            public void onFinish() {
                countdown.setText("Resend OTP");
                isTimerRunning = false;
                resendButton.setEnabled(true);
            }
        }.start();

        isTimerRunning = true;
    }

    private void verifyOTP(String verificationId, String email, String password, String gname, String lname, String gender, String age, String bday, String city, String phone) {

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.prompt_loading_screen);
        loadingDialog.setCancelable(false);
        Window dialogWindow = loadingDialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        loadingDialog.show();

        ImageView loadingImageView = loadingDialog.findViewById(R.id.loading);
        Glide.with(this)
                .asGif()
                .load(R.drawable.loading)
                .into(loadingImageView);

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
                })
                .addOnFailureListener(e -> {
                    // Handle network errors or other failures
                    Toast.makeText(verify_otp.this, "Phone verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    verifyButton.setVisibility(View.VISIBLE);
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
                    createCollections(userId);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Failed to save user details
                    Toast.makeText(verify_otp.this, "Failed to save user details.", Toast.LENGTH_SHORT).show();
                });
    }

    private void createCollections(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the "itineraries" collection under the user's ID
        CollectionReference itinerariesCollection = db.collection("users").document(userId).collection("itineraries");
        itinerariesCollection.addSnapshotListener((value, error) -> {
            if (error != null) {
                // Handle error
                Toast.makeText(verify_otp.this, "Error creating itineraries collection: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Reference to the "notifications" collection under the user's ID
        CollectionReference notificationsCollection = db.collection("users").document(userId).collection("notifications");
        notificationsCollection.addSnapshotListener((value, error) -> {
            if (error != null) {
                // Handle error
                Toast.makeText(verify_otp.this, "Error creating notifications collection: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        loadingDialog.dismiss();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
