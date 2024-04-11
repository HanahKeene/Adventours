package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adventours.ui.ConfirmationScreen;
import com.example.adventours.ui.RoomDetails;
import com.example.adventours.ui.adapters.photogalleryAdapter;
import com.example.adventours.ui.adapters.roomAdapter;
import com.example.adventours.ui.models.roomModel;
import com.example.adventours.ui.select_itinerary;
import com.example.adventours.ui.tutorial;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class restauinfo extends AppCompatActivity {

    ImageButton back;

    Button reserve;

    private ImageView img_spot;

    private TextView placeTextView;
    private TextView locationTextView;
    private TextView descriptionTextView;

    Button reserveatable;

    TextView datefld,timefld, childnum, adultnum;

    EditText note;

    ImageView addtoitinerary;

    ImageButton date_btn, time_btn, adultnumdec, adultnuminc, childnuminc, childnumdec;
    private RecyclerView photogalleryRecyclerView;

    private FirebaseFirestore db;

    int currentRoomNumber = 1;
    final int MIN_ROOM_NUMBER = 1;
    final int adultguestcount = 1;
    final int childguestcount = 0;

    final int MAX_ROOM_NUMBER = 10;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restauinfo);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        back = findViewById(R.id.backbtn);
        img_spot = findViewById(R.id.spot_img);
        placeTextView = findViewById(R.id.spot_place);
        locationTextView = findViewById(R.id.spot_location);
        descriptionTextView = findViewById(R.id.spot_desc);
        photogalleryRecyclerView = findViewById(R.id.galleryRecyclerview);
        reserve = findViewById(R.id.reservetable);
        addtoitinerary = findViewById(R.id.addtoitinerary);

        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        String restauId = intent.getStringExtra("restau_id");

        back.setOnClickListener(View -> finish());
        reserve.setOnClickListener(View -> showDialog());
        addtoitinerary.setOnClickListener(View -> addtoitinerary(restauId));
        placeTextView.setOnClickListener(View -> openGMaps(restauId));

        fetchRestaurantDetailsFromDatabase(restauId);
    }

    private void openGMaps(String restauId) {

        Intent intent = new Intent(restauinfo.this, tutorial.class);
        intent.putExtra("source", "Restaurant");
        intent.putExtra("Restau_ID", restauId);
        startActivity(intent);
    }

    private void addtoitinerary(String restauId) {

        Intent intent = new Intent(restauinfo.this, select_itinerary.class);
        intent.putExtra("source", "Restaurant");
        intent.putExtra("Restau_ID", restauId);
        startActivity(intent);
    }

    private void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.reserve_table);

        datefld = dialog.findViewById(R.id.datefld);
        timefld = dialog.findViewById(R.id.timefld);
        date_btn = dialog.findViewById(R.id.date);
        time_btn = dialog.findViewById(R.id.time);
        adultnuminc = dialog.findViewById(R.id.adultnuminc);
        adultnumdec = dialog.findViewById(R.id.adultnumdec);
        childnuminc = dialog.findViewById(R.id.childnuminc);
        childnumdec = dialog.findViewById(R.id.childnumdec);
        adultnum = dialog.findViewById(R.id.adultnum);
        childnum = dialog.findViewById(R.id.childnum);
        note = dialog.findViewById(R.id.note);
        reserveatable = dialog.findViewById(R.id.reserveatable);

        // Set current date and time
        Calendar currentDateTime = Calendar.getInstance();
        String currentDate = formatDateAsWords(currentDateTime.get(Calendar.YEAR), currentDateTime.get(Calendar.MONTH), currentDateTime.get(Calendar.DAY_OF_MONTH));
        String currentTime = formatTime(currentDateTime.get(Calendar.HOUR_OF_DAY), currentDateTime.get(Calendar.MINUTE));
        datefld.setText(currentDate);
        timefld.setText(currentTime);

        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a custom popup window
                showDatePicker(datefld);
            }
        });

        time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current time
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);

                sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
                boolean nightMode = sharedPreferences.getBoolean("night", false);

                int dialogTheme;
                if (nightMode) {
                    dialogTheme = R.style.Clock_Dark; // Use night mode theme
                } else {
                    dialogTheme = R.style.Clock; // Use day mode theme
                }

                // Create a time picker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(restauinfo.this, dialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                        // Format the time as needed (12-hour format with AM/PM)
                        String formattedTime = formatTime(selectedHour, selectedMinute);

                        // Set the formatted time to the timefld TextView
                        timefld.setText(formattedTime);
                    }
                }, hour, minute, false); // false means 24-hour time format

                // Show the time picker dialog
                timePickerDialog.show();
            }
        });



        adultnum.setText(String.valueOf(currentRoomNumber));
        childnum.setText(String.valueOf(currentRoomNumber));

        // Decrease the room number when the decrement button is clicked
        adultnumdec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentRoomNumber > adultguestcount) {
                    currentRoomNumber--;
                    adultnum.setText(String.valueOf(currentRoomNumber));
                }
            }
        });

        // Increase the room number when the increment button is clicked
        adultnuminc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentRoomNumber < MAX_ROOM_NUMBER) {
                    currentRoomNumber++;
                    adultnum.setText(String.valueOf(currentRoomNumber));
                }
            }
        });

        childnum.setText("0");

        childnumdec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentRoomNumber > 0) { // Prevent count from going below 0
                    currentRoomNumber--;
                    childnum.setText(String.valueOf(currentRoomNumber));
                }
            }
        });

        childnuminc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentRoomNumber < MAX_ROOM_NUMBER) {
                    currentRoomNumber++;
                    childnum.setText(String.valueOf(currentRoomNumber));
                }
            }
        });

        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.gravity = Gravity.BOTTOM;

            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            layoutParams.windowAnimations = R.style.DialogAnimation;

            window.setAttributes(layoutParams);
        }

        reserveatable.setOnClickListener(View -> confirmreserve());

        dialog.show();
    }

    private String formatTime(int selectedHour, int selectedMinute) {
        String amPm;
        String hourString;
        if (selectedHour < 12) {
            amPm = "AM";
            if (selectedHour == 0) {
                hourString = "12";
            } else {
                hourString = String.valueOf(selectedHour);
            }
        } else {
            amPm = "PM";
            if (selectedHour == 12) {
                hourString = "12";
            } else {
                hourString = String.valueOf(selectedHour - 12);
            }
        }
        return String.format(Locale.getDefault(), "%s:%02d %s", hourString, selectedMinute, amPm);
    }

    private void confirmreserve() {

        Intent intent = new Intent(this, RestauConfirmationScreen.class);

        Intent intent1 = getIntent();
        String restauId = intent1.getStringExtra("restau_id");

        intent.putExtra("RestaurantID", restauId);

        Toast.makeText(this, "Restaurant ID: " + restauId , Toast.LENGTH_SHORT).show();

        intent.putExtra("Date", datefld.getText().toString());
        intent.putExtra("Time", timefld.getText().toString());
        intent.putExtra("Adult", adultnum.getText().toString());
        intent.putExtra("Child", childnum.getText().toString());
        intent.putExtra("Note", note.getText().toString());

        startActivity(intent);

    }

    private void showDatePicker(final TextView dateTextView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);

        int dialogTheme;
        if (nightMode) {
            dialogTheme = R.style.DialogTheme_Dark; // Use night mode theme
        } else {
            dialogTheme = R.style.DialogTheme; // Use day mode theme
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Format the date as words (e.g., "June 12, 2023")
                String formattedDate = formatDateAsWords(year, month, day);

                // Set the formatted date to the TextView
                dateTextView.setText(formattedDate);
            }
        }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
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

    private void fetchRestaurantDetailsFromDatabase(String restauId) {

        DocumentReference restauRef = db.collection("Restaurants").document(restauId);

        restauRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Retrieve basic spot details
                String name = documentSnapshot.getString("name");
                String location = documentSnapshot.getString("location");
                String desc = documentSnapshot.getString("desc");
                String spotImageUrl = documentSnapshot.getString("img_url");


                // Set basic spot details to the corresponding views
                placeTextView.setText(name);
                locationTextView.setText(location);
                descriptionTextView.setText(desc);

                if (spotImageUrl != null) {
                    Glide.with(this)
                            .load(spotImageUrl)
                            .into(img_spot);
                }

                // Retrieve image URLs from the sub-collection
                restauRef.collection("Photo Gallery").get().addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<String> imageUrls = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String imageUrl = document.getString("img_url");
                        if (imageUrl != null) {
                            imageUrls.add(imageUrl);
                        }
                    }

                    // Set up the RecyclerView with the image URLs using photogalleryAdapter
                    photogalleryAdapter adapter = new photogalleryAdapter(this, imageUrls);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    photogalleryRecyclerView.setLayoutManager(layoutManager);
                    photogalleryRecyclerView.setAdapter(adapter);

                }).addOnFailureListener(e -> {
                    // Handle errors that occurred while fetching spot details
                });
            }
        });
    }
}