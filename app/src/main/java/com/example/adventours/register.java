package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
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

import com.example.adventours.ui.verify_otp;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class register extends AppCompatActivity {

    TextInputEditText emailtxtfld, nametxtfld, surnametxtfld, agetxtfld, bdaytxtfld, citytxtfld, phonetxtfld, passwordtxtfld, confirmpasstxtfld;
    RadioGroup gendergroup;
    Button submit, terms;

    ImageButton bdaybtn, showpassbtn, showconfirmpassbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailtxtfld = findViewById(R.id.email);
        nametxtfld = findViewById(R.id.name);
        surnametxtfld = findViewById(R.id.surname);
        agetxtfld = findViewById(R.id.age);
        bdaytxtfld = findViewById(R.id.birthdate);
        citytxtfld = findViewById(R.id.city);
        passwordtxtfld = findViewById(R.id.password);
        confirmpasstxtfld = findViewById(R.id.confirmpassword);
        gendergroup = findViewById(R.id.gender);
        bdaybtn = findViewById(R.id.bday);
        showpassbtn = findViewById(R.id.showpassword);
        showconfirmpassbtn = findViewById(R.id.showconfirmpassword);

        bdaybtn.setOnClickListener(view -> openCalendar());

        passwordtxtfld.setTransformationMethod(new PasswordTransformationMethod());
        confirmpasstxtfld.setTransformationMethod(new PasswordTransformationMethod());

        showpassbtn.setOnClickListener(View -> showpassword());

        showconfirmpassbtn.setOnClickListener(View -> showconfirmpassword());


        submit = findViewById(R.id.submitbtn);
        submit.setOnClickListener(View -> continuetonextscreen());

        terms = findViewById(R.id.terms);
        terms.setOnClickListener(view -> openTerms());
    }

    private void openTerms() {
        Intent intent = new Intent(this, termsandcondition.class);
        startActivity(intent);
    }

    private void continuetonextscreen() {
        // Get values from input fields
        String email = emailtxtfld.getText().toString();
        String gname = nametxtfld.getText().toString();
        String lname = surnametxtfld.getText().toString();
        String gender = getSelectedGender();
        String ageString = agetxtfld.getText().toString();
        String bday = bdaytxtfld.getText().toString();
        String city = citytxtfld.getText().toString();
        String password = passwordtxtfld.getText().toString();
        String confirmpassword = confirmpasstxtfld.getText().toString();

        // Check if any required fields are empty
        if (email.isEmpty() || gname.isEmpty() || lname.isEmpty() || password.isEmpty() || confirmpassword.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if age is numeric
        int age;
        try {
            age = Integer.parseInt(ageString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid age.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if age is 18 and above
        if (age < 18) {
            Toast.makeText(this, "You must be at least 18 years old to register.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate birthdate against age
        if (!isAgeValid(ageString, bday)) {
            Toast.makeText(this, "The entered age does not match the birthdate.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if terms checkbox is checked
        CheckBox termsCheckBox = findViewById(R.id.checkterms);
        if (!termsCheckBox.isChecked()) {
            Toast.makeText(this, "Please accept the terms and conditions.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Proceed to the next screen
        Intent intent = new Intent(this, verify_mobilenumber.class);
        intent.putExtra("email", email);
        intent.putExtra("gname", gname);
        intent.putExtra("lname", lname);
        intent.putExtra("gender", gender);
        intent.putExtra("age", ageString);
        intent.putExtra("bday", bday);
        intent.putExtra("city", city);
        intent.putExtra("password", password);
        startActivity(intent);
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

    private boolean isAgeValid(String age, String bday) {

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());

        try {
            Date birthdateDate = sdf.parse(bday);

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

    private void showconfirmpassword() {

        boolean isPasswordVisible = false;

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

    private void showpassword() {

        boolean isPasswordVisible = false;

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


}

