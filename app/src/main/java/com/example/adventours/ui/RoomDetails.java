package com.example.adventours.ui;


import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adventours.R;
import com.example.adventours.touristspotinfo;
import com.example.adventours.ui.adapters.photogalleryAdapter;
import com.example.adventours.ui.home.HomeFragment;
import com.example.adventours.ui.lists.hotel_lists_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Locale;

public class RoomDetails extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;

    ImageButton back;
    RecyclerView galleryRecyclerView;

    ImageView room_img, addtoitinerary;

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

        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String roomId = intent.getStringExtra("RoomId");
        String hotelId = intent.getStringExtra("HotelId");

        back = findViewById(R.id.backbtn);
        addtoitinerary = findViewById(R.id.addtoitinerarybtn);

        addtoitinerary.setOnClickListener(View -> addtoitinerary(hotelId));


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomDetails.this, touristspotinfo.class);
                startActivity(intent);
            }
        });



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
        user = auth.getCurrentUser();

        reservebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a custom popup window
                showDialog();
            }
        });

        fetchRoomDetailsFromFirebase(hotelId, roomId);
    }

    private void addtoitinerary(String hotel_id) {

        Intent intent = new Intent(RoomDetails.this, select_itinerary.class);
        intent.putExtra("source", "Hotel");
        intent.putExtra("Hotel_ID", hotel_id);
        startActivity(intent);
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

        reservearoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RoomDetails.this, ConfirmationScreen.class);

                // Add any data you want to pass to the ConfirmationScreen activity
                // For example, you can use putExtra to pass room details
                Intent intent1 = getIntent();
                String hotelId = intent1.getStringExtra("HotelId");
                String roomId = intent1.getStringExtra("RoomId");

                intent.putExtra("HotelId", hotelId);
                intent.putExtra("Room ID", roomId);

                Toast.makeText(RoomDetails.this, "Hotel ID: " + hotelId + " ROOM ID: " + roomId, Toast.LENGTH_SHORT).show();

                intent.putExtra("RoomQuantity", roomNumber.getText().toString());
                intent.putExtra("RoomName", roomName.getText().toString());
                intent.putExtra("Price", priceTxtView.getText().toString());
                intent.putExtra("CheckIn", in_date.getText().toString());
                intent.putExtra("CheckOut", out_date.getText().toString());

                // Add more data as needed

                startActivity(intent);

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if (currentUser != null) {
                    String userId = currentUser.getUid();

                    // Fetch user details from Firestore using the obtained userId
                    DocumentReference userRef = db.collection("Users").document(userId);

                    userRef.get().addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Retrieve user details
                            String firstName = documentSnapshot.getString("firstName");
                            String lastName = documentSnapshot.getString("lastName");
                            String city = documentSnapshot.getString("city");
                            String phone = documentSnapshot.getString("phone");

                            // Pass user details to the ConfirmationScreen activity
                            intent.putExtra("FirstName", firstName);
                            intent.putExtra("LastName", lastName);
                            intent.putExtra("City", city);
                            intent.putExtra("Phone", phone);
                            intent.putExtra("UserID", userId);


                            Toast.makeText(RoomDetails.this, "User:" + userId, Toast.LENGTH_SHORT).show();
                            // Start the ConfirmationScreen activity
//                            startActivity(intent);
                        } else {
                            // Handle the case where the user document does not exist
                            Toast.makeText(RoomDetails.this, "User details not found", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                        // Handle errors that occurred while fetching user details
                        Toast.makeText(RoomDetails.this, "Error fetching user details", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    // Handle the case where the user is not authenticated
                    Toast.makeText(RoomDetails.this, "User not authenticated", Toast.LENGTH_SHORT).show();
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
