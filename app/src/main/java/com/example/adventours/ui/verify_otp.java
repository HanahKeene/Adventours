package com.example.adventours.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class verify_otp extends AppCompatActivity {

    private CountDownTimer countDownTimer;
    private long otpExpirationTime;
    private long timeLeftInMillis;
    EditText otp1, otp2, otp3, otp4, otp5, otp6;
    Button verify;

    ProgressBar progressBar;

    TextView resendotp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        otp6 = findViewById(R.id.otp6);
        progressBar = findViewById(R.id.progressBar);
        verify = findViewById(R.id.verify);
        
        otpExpirationTime = getIntent().getLongExtra("otpExpirationTime", 0);

        setupOTP();
//        startCountDownTimer(otpExpirationTime - System.currentTimeMillis());

        String verificationid = getIntent().getStringExtra("verificationId");

        Intent intent = getIntent();
        otpExpirationTime = intent.getLongExtra("otpExpirationTime", 0);



        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(otp1.getText().toString().trim().isEmpty()
                || otp2.getText().toString().trim().isEmpty()
                || otp3.getText().toString().trim().isEmpty()
                || otp4.getText().toString().trim().isEmpty()
                || otp5.getText().toString().trim().isEmpty()
                || otp6.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(verify_otp.this, "Please enter valid code.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String code = otp1.getText().toString()
                        + otp2.getText().toString()
                        + otp3.getText().toString()
                        + otp4.getText().toString()
                        + otp5.getText().toString()
                        + otp6.getText().toString() ;

                if(verificationid != null)
                {
                    progressBar.setVisibility(View. VISIBLE);
                    verify.setVisibility(View.VISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                            verificationid,
                            code
                    );
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    verify.setVisibility(View.VISIBLE);
                                    if (task.isSuccessful()) {
                                        Intent resultIntent = new Intent(verify_otp.this, SigninActivity.class);
                                        resultIntent.putExtra("verificationStatus", "Verified");
                                        setResult(Activity.RESULT_OK, resultIntent);
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(verify_otp.this, "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });
    }

//    private void startCountDownTimer(long l) {
//        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                timeLeftInMillis = millisUntilFinished;
//                updateCountdownText();
//            }
//
//            @Override
//            public void onFinish() {
//                // Handle what happens when the countdown finishes
//                // For example, show a message or take appropriate action
//                // This could include setting a flag to indicate that the time has expired
//                // and preventing further verification attempts
//                Toast.makeText(verify_otp.this, "OTP has expired", Toast.LENGTH_SHORT).show();
//                finish(); // Finish the activity when the OTP expires
//            }
//        }.start();
//    }

//    private void updateCountdownText() {
//
//        TextView countdownTextView = findViewById(R.id.countdown);
//        long minutes = (timeLeftInMillis / 1000) / 60;
//        long seconds = (timeLeftInMillis / 1000) % 60;
//        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
//        countdownTextView.setText(timeLeftFormatted);
//    }

    private void setupOTP()
    {
        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if(!s.toString().trim().isEmpty()){
                    otp2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if(!s.toString().trim().isEmpty()){
                    otp3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if(!s.toString().trim().isEmpty()){
                    otp4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if(!s.toString().trim().isEmpty()){
                    otp5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if(!s.toString().trim().isEmpty()){
                    otp6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if(!s.toString().trim().isEmpty()){
                    otp1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (countDownTimer != null) {
//            countDownTimer.cancel();
//        }
//    }
}