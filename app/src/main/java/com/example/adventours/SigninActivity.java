package com.example.adventours;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adventours.ui.home.HomeFragment;
import com.example.adventours.ui.verify_otp;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class SigninActivity extends AppCompatActivity {

    TextInputEditText untxtfld, emailtxtfld, nametxtfld, surnametxtfld, agetxtfld, bdaytxtfld, citytxtfld, phonetxtfld, passwordtxtfld, confirmpasstxtfld;
    RadioGroup gendergroup;
    Button verify, submit, terms;

    ImageButton bdaybtn;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);



        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initializeUI();

        // Check if the user is already signed in
        checkCurrentUser();
    }

    private void initializeUI() {
        untxtfld = findViewById(R.id.usernametxtfld);
        emailtxtfld = findViewById(R.id.email);
        nametxtfld = findViewById(R.id.name);
        surnametxtfld = findViewById(R.id.surname);
        agetxtfld = findViewById(R.id.age);
        bdaytxtfld = findViewById(R.id.birthdate);
        citytxtfld = findViewById(R.id.city);
        phonetxtfld = findViewById(R.id.number);
        passwordtxtfld = findViewById(R.id.password);
        confirmpasstxtfld = findViewById(R.id.confirmpassword);
        gendergroup = findViewById(R.id.gender);
        bdaybtn = findViewById(R.id.bday);

        bdaybtn.setOnClickListener(view -> openCalendar());

        verify = findViewById(R.id.verifybtn);
        verify.setOnClickListener(view -> openVerifier());

        submit = findViewById(R.id.submitbtn);
        submit.setOnClickListener(view -> submitNewUser());

        terms = findViewById(R.id.terms);
        terms.setOnClickListener(view -> openTerms());
    }

    private void checkCurrentUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already signed in, redirect to the main activity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    private void openCalendar() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                bdaytxtfld.setText(String.valueOf(year) + "/" + String.valueOf(month) + "/" + String.valueOf(day));
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void submitNewUser() {
        String uname = untxtfld.getText().toString();
        String email = emailtxtfld.getText().toString();
        String gname = nametxtfld.getText().toString();
        String lname = surnametxtfld.getText().toString();
        String gender = getSelectedGender();
        String age = agetxtfld.getText().toString();
        String bday = bdaytxtfld.getText().toString();
        String city = citytxtfld.getText().toString();
        String phone = phonetxtfld.getText().toString();
        String password = passwordtxtfld.getText().toString();
        String confirmpassword = confirmpasstxtfld.getText().toString();

        if (uname.isEmpty() || email.isEmpty() || gname.isEmpty() || lname.isEmpty() || password.isEmpty() || confirmpassword.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmpassword)) {
            Toast.makeText(this, "Passwords didn't match.", Toast.LENGTH_SHORT).show();
        } else {
            // Proceed with Firebase registration and database update
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null) {
                                // Create a new user object
                                com.example.adventours.User newUser = new com.example.adventours.User(uname, email, gname, lname, gender, age, bday, city, phone);

                                // Add the user to Firestore
                                db.collection("users")
                                        .document(user.getUid())
                                        .set(newUser)
                                        .addOnCompleteListener(aVoid -> {
                                            Toast.makeText(this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), interestform.class));
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        } else {
                            // Registration failed
                            Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private String getSelectedGender() {
        int selectedGenderId = gendergroup.getCheckedRadioButtonId();
        if (selectedGenderId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedGenderId);
            return selectedRadioButton.getText().toString();
        } else {
            // No gender selected
            return "";
        }
    }

    private void openTerms() {
        Intent intent = new Intent(this, termsandcondition.class);
        startActivity(intent);
    }

    private void openVerifier() {

        final EditText mobilenumber = findViewById(R.id.number);
        final ProgressBar progressBar = findViewById(R.id.progressBar);

        if (mobilenumber.getText().toString().trim().isEmpty()) {
            Toast.makeText(SigninActivity.this, "Enter your mobile number", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        verify.setVisibility(View.INVISIBLE);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+639" + mobilenumber.getText().toString(),
                60,
                TimeUnit.SECONDS,
                SigninActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        progressBar.setVisibility(View.GONE);
                        verify.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressBar.setVisibility(View.GONE);
                        verify.setVisibility(View.VISIBLE);
                        Toast.makeText(SigninActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        progressBar.setVisibility(View.GONE);
                        verify.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(SigninActivity.this, verify_otp.class);
                        intent.putExtra("number", mobilenumber.getText().toString());
                        intent.putExtra("verificationId", verificationId);
                        startActivity(intent);
                    }
                }
        );
    }

}
