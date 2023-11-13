package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.adventours.ui.home.HomeFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SigninActivity extends AppCompatActivity {

    TextInputEditText untxtfld, emailtxtfld, nametxtfld, surnametxtfld, agetxtfld, bdaytxtfld, citytxtfld, phonetxtfld, passwordtxtfld;
    RadioGroup gendergroup;
    Button verify, submit, terms;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

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
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        untxtfld = findViewById(R.id.usernametxtfld);
        emailtxtfld = findViewById(R.id.email);
        nametxtfld = findViewById(R.id.name);
        surnametxtfld = findViewById(R.id.surname);
        agetxtfld = findViewById(R.id.age);
        bdaytxtfld = findViewById(R.id.birthdate);
        citytxtfld = findViewById(R.id.city);
        phonetxtfld = findViewById(R.id.number);
        passwordtxtfld = findViewById(R.id.password);
        gendergroup = findViewById(R.id.gender);

        verify = findViewById(R.id.verifybtn);
        verify.setOnClickListener(view -> openVerifier());

        submit = findViewById(R.id.submitbtn);
        submit.setOnClickListener(view -> submitnewuser());

        terms = findViewById(R.id.terms);
        terms.setOnClickListener(view -> openterms());
    }

    private void submitnewuser() {
        String uname, email, gname, lname, gender, age, bday, city, phone, password, confirmpass;

        uname = String.valueOf(untxtfld.getText());
        email = String.valueOf(emailtxtfld.getText());
        gname = String.valueOf(nametxtfld.getText());
        lname = String.valueOf(surnametxtfld.getText());
        gender = getSelectedGender();
        age = String.valueOf(agetxtfld.getText());
        bday = String.valueOf(bdaytxtfld.getText());
        city = String.valueOf(citytxtfld.getText());
        phone = String.valueOf(phonetxtfld.getText());
        password = String.valueOf(passwordtxtfld.getText());
        confirmpass = ""; // You should get the confirmation password from the user input

        if (uname.isEmpty() || email.isEmpty() || gname.isEmpty() || lname.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
        } else {
            // Proceed with Firebase registration and database update
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Registration successful, update Realtime Database
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("gname", gname);
                            userData.put("lname", lname);
                            userData.put("email", email);
                            userData.put("gender", gender);
                            userData.put("age", age);
                            userData.put("bday", bday);
                            userData.put("city", city);
                            userData.put("phone", phone);

                            databaseReference.child("users").child(uname).setValue(userData);

                            Toast.makeText(this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), interestform.class);
                            startActivity(intent);
                            finish();
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

    private void openterms() {
        Intent intent = new Intent(this, termsandcondition.class);
        startActivity(intent);
    }

    private void openVerifier() {

    }
}