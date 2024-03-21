package com.example.adventours;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adventours.ui.verify_otp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SigninActivity extends AppCompatActivity {

    private static final String TAG = "Register";

    EditText untxtfld, emailtxtfld, nametxtfld, surnametxtfld, agetxtfld, bdaytxtfld, citytxtfld, phonetxtfld, passwordtxtfld, confirmpasstxtfld;
    RadioGroup gendergroup;
    Button verify, submit, terms;

    ImageButton bdaybtn, showpassbtn, showconfirmpassbtn;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private static final int REQUEST_CODE_VERIFY_OTP = 123;

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
        showpassbtn = findViewById(R.id.showpassword);
        showconfirmpassbtn = findViewById(R.id.showconfirmpassword);

        bdaybtn.setOnClickListener(view -> openCalendar());

        passwordtxtfld.setTransformationMethod(new PasswordTransformationMethod());
        confirmpasstxtfld.setTransformationMethod(new PasswordTransformationMethod());

        showpassbtn.setOnClickListener(new View.OnClickListener() {
            boolean isPasswordVisible = false;

            @Override
            public void onClick(View v) {
                isPasswordVisible = !isPasswordVisible;

                // Change transformation method based on visibility state
                passwordtxtfld.setTransformationMethod(
                        isPasswordVisible ? null : new PasswordTransformationMethod()
                );

                // Move the cursor to the end of the text to ensure it's always visible
                passwordtxtfld.setSelection(passwordtxtfld.getText().length());
                int imageResource = isPasswordVisible ? R.drawable.eye_slash_solid : R.drawable.eye_solid;
                showpassbtn.setImageResource(imageResource);
            }
        });

        showconfirmpassbtn.setOnClickListener(new View.OnClickListener() {
            boolean isPasswordVisible = false;

            @Override
            public void onClick(View v) {
                isPasswordVisible = !isPasswordVisible;

                // Change transformation method based on visibility state
                confirmpasstxtfld.setTransformationMethod(
                        isPasswordVisible ? null : new PasswordTransformationMethod()
                );

                // Move the cursor to the end of the text to ensure it's always visible
                confirmpasstxtfld.setSelection(confirmpasstxtfld.getText().length());
                int imageResource = isPasswordVisible ? R.drawable.eye_slash_solid : R.drawable.eye_solid;
                showconfirmpassbtn.setImageResource(imageResource);
            }
        });

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
                // Format the date as words (e.g., "June 12, 2023")
                String formattedDate = formatDateAsWords(year, month, day);

                // Set the formatted date to the TextView
                bdaytxtfld.setText(formattedDate);
            }

            private String formatDateAsWords(int year, int month, int day) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                // Create a SimpleDateFormat with the desired format
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());

                // Format the date and return the result
                return sdf.format(calendar.getTime());
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
        String phone = "+639" + phonetxtfld.getText().toString();
        String password = passwordtxtfld.getText().toString();
        String confirmpassword = confirmpasstxtfld.getText().toString();
        CheckBox termsCheckBox = findViewById(R.id.checkterms);
        ProgressBar progressBarlogin = findViewById(R.id.progressBarlogin);


        int requiredage = 18;
        int userAge = Integer.parseInt(age);


        if (uname.isEmpty() || email.isEmpty() || gname.isEmpty() || lname.isEmpty() || password.isEmpty() || confirmpassword.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
        } else if (!isAgeValid(age, bday)) {
            Toast.makeText(this, "The entered age does not match the birthdate.", Toast.LENGTH_SHORT).show();
        } else if (userAge < requiredage) {
            Toast.makeText(this, "You must be at least 18 years old to register.", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmpassword)) {
            Toast.makeText(this, "Passwords didn't match.", Toast.LENGTH_SHORT).show();
        } else if (!termsCheckBox.isChecked()) {
            Toast.makeText(this, "Please accept the terms and conditions.", Toast.LENGTH_SHORT).show();
        } else if (!isPhoneNumberVerified()) {
            Toast.makeText(this, "Verify phone number.", Toast.LENGTH_SHORT).show();
        } else {

            progressBarlogin.setVisibility(View.VISIBLE);
            submit.setVisibility(View.INVISIBLE);

            if (!email.isEmpty()) {
                mAuth.getCurrentUser().updateEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SigninActivity.this, "Email linked successfully", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        // Create a new user object
                                        User newUser = new User(uname, email, gname, lname, gender, age, bday, city, phone);

                                        // Add the user to Firestore
                                        db.collection("users")
                                                .document(user.getUid())
                                                .set(newUser)
                                                .addOnCompleteListener(aVoid -> {
                                                    // User registered successfully, now add empty subcollection
                                                    db.collection("users")
                                                            .document(user.getUid())
                                                            .collection("Itinerary")
                                                            .get()
                                                            .addOnCompleteListener(subcollectionTask -> {
                                                                if (subcollectionTask.isSuccessful()) {
                                                                    if (subcollectionTask.getResult().isEmpty()) {
                                                                        // Subcollection doesn't exist, you can perform any necessary actions here
                                                                        Toast.makeText(SigninActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                                                                        startActivity(new Intent(getApplicationContext(), interestform.class));
                                                                        finish();
                                                                    } else {
                                                                        Toast.makeText(SigninActivity.this, "Subcollection creation failed. Please try again.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } else {
                                                                    Toast.makeText(SigninActivity.this, "Subcollection creation failed. Please try again.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(SigninActivity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                                                });

                                        db.collection("users")
                                                .document(user.getUid())
                                                .collection("Notification")
                                                .get()
                                                .addOnCompleteListener(unreadNotifTask -> {
                                                    if (unreadNotifTask.isSuccessful()) {
                                                        if (unreadNotifTask.getResult().isEmpty()) {
                                                            // unread_notif subcollection doesn't exist, you can perform any necessary actions here
                                                            Toast.makeText(SigninActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(getApplicationContext(), interestform.class));
                                                            finish();
                                                        } else {
                                                            Toast.makeText(SigninActivity.this, "Subcollection creation failed. Please try again.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(SigninActivity.this, "Subcollection creation failed. Please try again.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                } else {
                                    Toast.makeText(SigninActivity.this, "Failed to link email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(SigninActivity.this, "Please enter an email address", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private boolean isPhoneNumberVerified() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Check if the phone number is linked to the user account
            return user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty();
        }
        return false;
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
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressBar.setVisibility(View.GONE);
                        verify.setVisibility(View.VISIBLE);
                        verify.setText("Verified");
                        Toast.makeText(SigninActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(SigninActivity.this, verify_otp.class);
                        intent.putExtra("uname", untxtfld.getText().toString());
                        intent.putExtra("email", emailtxtfld.getText().toString());
                        intent.putExtra("gname", nametxtfld.getText().toString());
                        intent.putExtra("lname", surnametxtfld.getText().toString());
                        intent.putExtra("gender", getSelectedGender());
                        intent.putExtra("age", agetxtfld.getText().toString());
                        intent.putExtra("bday", bdaytxtfld.getText().toString());
                        intent.putExtra("city", citytxtfld.getText().toString());
                        intent.putExtra("number", mobilenumber.getText().toString());
                        intent.putExtra("verificationId", verificationId);
                        startActivityForResult(intent, REQUEST_CODE_VERIFY_OTP);
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_VERIFY_OTP && resultCode == Activity.RESULT_OK) {
            String verificationStatus = data.getStringExtra("verificationStatus");
            Button verifyButton = findViewById(R.id.verify);

            if ("Verified".equals(verificationStatus) && verifyButton != null) {

                String username = data.getStringExtra("username");
                String email = data.getStringExtra("email");
                String gname = data.getStringExtra("gname");
                String lname = data.getStringExtra("lname");
                String gender = data.getStringExtra("gender");
                String age = data.getStringExtra("age");
                String bday = data.getStringExtra("bday");
                String city = data.getStringExtra("city");
                String number = data.getStringExtra("number");

                untxtfld.setText(username);
                emailtxtfld.setText(email);
                nametxtfld.setText(gname);
                surnametxtfld.setText(lname);
                setGenderSelection(gender);
                agetxtfld.setText(age);
                bdaytxtfld.setText(bday);
                citytxtfld.setText(city);
                phonetxtfld.setText(number);

                verifyButton.setText("Verified");
                verifyButton.setEnabled(false);
            }
        }
    }

    private void setGenderSelection(String gender) {
        int childCount = gendergroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = gendergroup.getChildAt(i);
            if (view instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) view;
                if (radioButton.getText().toString().equals(gender)) {
                    radioButton.setChecked(true);
                    return;
                }
            }
        }
    }


    private boolean isAgeValid(String age, String birthdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());

        try {
            Date birthdateDate = sdf.parse(birthdate);

            Calendar birthdateCalendar = Calendar.getInstance();
            birthdateCalendar.setTime(birthdateDate);

            Calendar currentCalendar = Calendar.getInstance();

            int calculatedAge = currentCalendar.get(Calendar.YEAR) - birthdateCalendar.get(Calendar.YEAR);

            if (currentCalendar.get(Calendar.MONTH) < birthdateCalendar.get(Calendar.MONTH)
                    || (currentCalendar.get(Calendar.MONTH) == birthdateCalendar.get(Calendar.MONTH)
                    && currentCalendar.get(Calendar.DAY_OF_MONTH) < birthdateCalendar.get(Calendar.DAY_OF_MONTH))) {
                calculatedAge--;
            }

            return Integer.parseInt(age) == calculatedAge;
        } catch (ParseException e) {
            e.printStackTrace();
            return false; // Return false in case of parsing error
        }
    }
}
