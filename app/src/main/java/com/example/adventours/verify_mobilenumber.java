package com.example.adventours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adventours.ui.verify_otp;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class verify_mobilenumber extends AppCompatActivity {

    TextInputEditText number;
    Button button;
    private Dialog loadingDialog;
    private static final int REQUEST_CODE_VERIFY_OTP = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_mobilenumber);

        number = findViewById(R.id.number);
        button = findViewById(R.id.button);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String gname = intent.getStringExtra("gname");
        String lname = intent.getStringExtra("lname");
        String gender = intent.getStringExtra("gender");
        String age = intent.getStringExtra("age");
        String bday = intent.getStringExtra("bday");
        String city = intent.getStringExtra("city");
        String password = intent.getStringExtra("password");


        button.setOnClickListener(View -> openverifier(email, gname, lname, gender, age, bday, city, password));

    }

    private void openverifier(String email, String gname, String lname, String gender, String age, String bday, String city, String password) {

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

        if (number.getText().toString().trim().isEmpty()) {
            Toast.makeText(verify_mobilenumber.this, "Enter your mobile number", Toast.LENGTH_SHORT).show();
            return;
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+63" + number.getText().toString(),
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
//                        progressBar.setVisibility(View.GONE);
//                        verify.setVisibility(View.VISIBLE);
//                        verify.setText("Verified");
//                        Toast.makeText(SigninActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        loadingDialog.dismiss();
                        String phone = number.getText().toString();
                        Intent intent = new Intent(verify_mobilenumber.this, verify_otp.class);
                        intent.putExtra("verificationId", verificationId);
                        intent.putExtra("email", email);
                        intent.putExtra("gname", gname);
                        intent.putExtra("lname", lname);
                        intent.putExtra("gender", gender);
                        intent.putExtra("age", age);
                        intent.putExtra("bday", bday);
                        intent.putExtra("city", city);
                        intent.putExtra("password", password);
                        intent.putExtra("phoneNumber", phone);
                        startActivity(intent);
                        startActivityForResult(intent, REQUEST_CODE_VERIFY_OTP);
                    }
                }
        );
    }
}