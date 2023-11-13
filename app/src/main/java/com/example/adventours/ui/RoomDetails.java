package com.example.adventours.ui;

import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adventours.R;
import com.example.adventours.ui.adapters.photogalleryAdapter;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class RoomDetails extends AppCompatActivity {

    RecyclerView galleryRecyclerView;

    ImageView room_img;

    TextView priceTxtView, paxTxtView, roomName, amenitiesTxtView, descTxtView, in_date, out_date, roomNumber;

    Button reservebtn, reservearoom;

    ImageButton in_btn, out_btn, roomnumdec, roomnumin;

    FirebaseFirestore db;

    int currentRoomNumber = 1;
    final int MIN_ROOM_NUMBER = 1;
    final int MAX_ROOM_NUMBER = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_details);

        Intent intent = getIntent();
        String roomId = intent.getStringExtra("RoomId");
        String hotelId = intent.getStringExtra("HotelId");

        Toast.makeText(this, "Hotel ID: " + hotelId + " ROOM ID: " + roomId, Toast.LENGTH_SHORT).show();

        db = FirebaseFirestore.getInstance();

        room_img = findViewById(R.id.room_img);
        paxTxtView = findViewById(R.id.pax);
        roomName = findViewById(R.id.room_name);
        priceTxtView = findViewById(R.id.price);
        amenitiesTxtView = findViewById(R.id.amenities);
        descTxtView = findViewById(R.id.desc);
        reservebtn = findViewById(R.id.reserveroombtn);
        galleryRecyclerView = findViewById(R.id.galleryRecyclerview);

        reservebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a custom popup window
                showDialog();
            }
        });

        fetchRoomDetailsFromFirebase(hotelId, roomId);
    }

    private void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.reserve_details);

        in_date = findViewById(R.id.in_date);
        out_date = findViewById(R.id.out_date);
        in_btn = findViewById(R.id.in_btn);
        out_btn = findViewById(R.id.out_btn);
        roomnumin = findViewById(R.id.roomnumin);
        roomnumdec = findViewById(R.id.roomnumdec);
        reservearoom = findViewById(R.id.reservearoom);




        // Find ImageButtons within the dialog's layout
        ImageButton in_btn = dialog.findViewById(R.id.in_btn);
        ImageButton out_btn = dialog.findViewById(R.id.out_btn);
        ImageButton roomnumin = dialog.findViewById(R.id.roomnumin);
        ImageButton roomnumdec = dialog.findViewById(R.id.roomnumdec);

        TextView in_date = dialog.findViewById(R.id.in_date);
        TextView out_date = dialog.findViewById(R.id.out_date);
        TextView roomNumber = dialog.findViewById(R.id.room_num);

        Button reservearoom = dialog.findViewById(R.id.reservearoom);

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

        roomNumber.setText(String.valueOf(currentRoomNumber));

        // Decrease the room number when the decrement button is clicked
        roomnumdec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentRoomNumber > MIN_ROOM_NUMBER) {
                    currentRoomNumber--;
                    roomNumber.setText(String.valueOf(currentRoomNumber));
                }
            }
        });

        // Increase the room number when the increment button is clicked
        roomnumin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentRoomNumber < MAX_ROOM_NUMBER) {
                    currentRoomNumber++;
                    roomNumber.setText(String.valueOf(currentRoomNumber));
                }
            }
        });

//        reservearoom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent((Activity) v.getContext(), ConfirmationScreen.class);
//                startActivity(intent);
//            }
//        });


        // Set the dialog window attributes to appear at the bottom
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.gravity = Gravity.BOTTOM;

            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            layoutParams.windowAnimations = R.style.DialogAnimation;

            window.setAttributes(layoutParams);
        }

        // Adjust content view padding to remove space around the content
        View contentView = dialog.findViewById(android.R.id.content);
        contentView.setPadding(0, 0, 0, 0);

        dialog.show();
    }

    private void showDatePicker(final TextView dateTextView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Showing the picked value in the textView
                dateTextView.setText(String.valueOf(year) + "." + String.valueOf(month) + "." + String.valueOf(day));
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void fetchRoomDetailsFromFirebase(String hotelId, String roomId) {
        DocumentReference roomRef = db.collection("Hotels")
                .document(hotelId)
                .collection("Rooms")
                .document(roomId);

        roomRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Retrieve room details
                String img_url = documentSnapshot.getString("img_url");
                String pax = documentSnapshot.getString("pax");
                String price = documentSnapshot.getString("price");
                String roomname = documentSnapshot.getString("name");
                String amenities = documentSnapshot.getString("amenities");
                String desc = documentSnapshot.getString("desc");

                // Set room details to the corresponding views
                priceTxtView.setText(price);
                paxTxtView.setText(pax);
                roomName.setText(roomname);
                amenitiesTxtView.setText(amenities);
                descTxtView.setText(desc);

                if (img_url != null) {
                    Glide.with(this)
                            .load(img_url)
                            .into(room_img);
                }

                roomRef.collection("Photo Gallery").get().addOnSuccessListener(queryDocumentSnapshots -> {
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
                    galleryRecyclerView.setLayoutManager(layoutManager);
                    galleryRecyclerView.setAdapter(adapter);

                }).addOnFailureListener(e -> {
                    // Handle errors that occurred while fetching spot details
                });

                // Add additional code to handle other room details if needed
            } else {
                // Handle the case where the room document does not exist
            }
        }).addOnFailureListener(e -> {
            // Handle errors that occurred while fetching room details
        });
    }
}
