package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adventours.ui.ConfirmationScreen;
import com.example.adventours.ui.RoomDetails;
import com.example.adventours.ui.adapters.photogalleryAdapter;
import com.example.adventours.ui.adapters.roomAdapter;
import com.example.adventours.ui.models.roomModel;
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

    TextView in_date, out_date, childnum, adultnum;

    EditText note;

    ImageButton in_btn, out_btn, adultnumdec, adultnuminc, childnuminc, childnumdec;
    private RecyclerView photogalleryRecyclerView, delicaciesRecyclerview;

    private FirebaseFirestore db;

    int currentRoomNumber = 1;
    final int MIN_ROOM_NUMBER = 1;

    final int MAX_ROOM_NUMBER = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restauinfo);

        back = findViewById(R.id.backbtn);
        img_spot = findViewById(R.id.spot_img);
        placeTextView = findViewById(R.id.spot_place);
        locationTextView = findViewById(R.id.spot_location);
        descriptionTextView = findViewById(R.id.spot_desc);
        photogalleryRecyclerView = findViewById(R.id.galleryRecyclerview);
        delicaciesRecyclerview = findViewById(R.id.delicaciesrecyclerview);
        reserve = findViewById(R.id.reservetable);

        back.setOnClickListener(View -> finish());
        reserve.setOnClickListener(View -> showDialog());

        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        String restauId = intent.getStringExtra("restau_id");

        fetchRestaurantDetailsFromDatabase(restauId);
    }

    private void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.reserve_table);

        in_date = dialog.findViewById(R.id.in_date);
        out_date = dialog.findViewById(R.id.out_date);
        in_btn = dialog.findViewById(R.id.in_btn);
        out_btn = dialog.findViewById(R.id.out_btn);
        adultnuminc = dialog.findViewById(R.id.adultnuminc);
        adultnumdec = dialog.findViewById(R.id.adultnumdec);
        childnuminc = dialog.findViewById(R.id.childnuminc);
        childnumdec = dialog.findViewById(R.id.childnumdec);
        adultnum = dialog.findViewById(R.id.adultnum);
        childnum = dialog.findViewById(R.id.childnum);
        note = dialog.findViewById(R.id.note);
        reserveatable = dialog.findViewById(R.id.reserveatable);

        in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a custom popup window
                showDatePicker(in_date);
            }
        });

        out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a custom popup window
                showDatePicker(out_date);
            }
        });

        adultnum.setText(String.valueOf(currentRoomNumber));
        childnum.setText(String.valueOf(currentRoomNumber));

        // Decrease the room number when the decrement button is clicked
        adultnumdec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentRoomNumber > MIN_ROOM_NUMBER) {
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

        childnumdec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentRoomNumber > MIN_ROOM_NUMBER) {
                    currentRoomNumber--;
                    childnum.setText(String.valueOf(currentRoomNumber));
                }
            }
        });

        // Increase the room number when the increment button is clicked
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

    private void confirmreserve() {

        Intent intent = new Intent(this, RestauConfirmationScreen.class);

        Intent intent1 = getIntent();
        String restauId = intent1.getStringExtra("restau_id");

        intent.putExtra("RestaurantID", restauId);

        Toast.makeText(this, "Restaurant ID: " + restauId , Toast.LENGTH_SHORT).show();

        intent.putExtra("Start Date", in_date.getText().toString());
        intent.putExtra("End Date", out_date.getText().toString());
        intent.putExtra("Adult", adultnum.getText().toString());
        intent.putExtra("Child", childnum.getText().toString());
        intent.putExtra("Start Date", note.getText().toString());

        startActivity(intent);

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
        }, year, month, day);

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
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                    photogalleryRecyclerView.setLayoutManager(layoutManager);
                    photogalleryRecyclerView.setAdapter(adapter);

                }).addOnFailureListener(e -> {
                    // Handle errors that occurred while fetching spot details
                });
            }
        });
    }
}