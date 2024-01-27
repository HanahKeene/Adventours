package com.example.adventours.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.adventours.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class newitineraryplan extends AppCompatActivity {

    Button cover;

    TextInputEditText name, startdate, enddate;
    
    ImageButton startbtn, endbtn;
    Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newitineraryplan);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        cover = findViewById(R.id.edit_plan_cover);
        name = findViewById(R.id.itineraryName);
        startdate = findViewById(R.id.startdate);
        enddate = findViewById(R.id.enddate);
        startbtn = findViewById(R.id.startdatebtn);
        endbtn = findViewById(R.id.enddatebtn);
        create = findViewById(R.id.createbtn);
        
        startbtn.setOnClickListener(View -> opencalendarstart());
        endbtn.setOnClickListener(View -> opencalendarend());
        create.setOnClickListener(View -> createitinerary());

    }

    private void opencalendarstart() {
        showDatePicker(startdate);
    }

    private void opencalendarend() {
        showDatePicker(enddate);
    }

    private void showDatePicker(final TextView dateTextView) {
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
                dateTextView.setText(formattedDate);
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



    private void createitinerary() {

        String itineraryname = name.toString();
        String start = startdate.toString();
        String end = enddate.toString();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        
    }
}