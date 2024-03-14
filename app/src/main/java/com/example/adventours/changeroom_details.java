package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adventours.ui.adapters.photogalleryAdapter;
import com.example.adventours.ui.check_reservation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class changeroom_details extends AppCompatActivity {

    ImageButton back;

    Button reservebtn;
    RecyclerView galleryRecyclerView;

    ImageView room_img, addtoitinerary;

    TextView priceTxtView, paxTxtView, roomName, amenitiesTxtView, descTxtView;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeroom_details);

        Intent intent = getIntent();
        String roomId = intent.getStringExtra("RoomId");
        String hotelId = intent.getStringExtra("HotelId");
        String reservationId = intent.getStringExtra("reservationId");

        back = findViewById(R.id.backbtn);

        back.setOnClickListener(View -> finish());

        Toast.makeText(this, "Hotel ID: " + hotelId + " ROOM ID: " + roomId, Toast.LENGTH_SHORT).show();

        db = FirebaseFirestore.getInstance();

        room_img = findViewById(R.id.room_img);
        paxTxtView = findViewById(R.id.pax);
        roomName = findViewById(R.id.room_name);
        priceTxtView = findViewById(R.id.price);
        amenitiesTxtView = findViewById(R.id.amenities);
        descTxtView = findViewById(R.id.desc);
        reservebtn = findViewById(R.id.changeroombtn);
        galleryRecyclerView = findViewById(R.id.galleryRecyclerview);

        reservebtn.setOnClickListener(View -> updateroom(reservationId));

        fetchRoomDetailsFromFirebase(hotelId, roomId);
    }

    private void updateroom(String reservationId) {
        String roomname = roomName.getText().toString();

        DocumentReference reservationRef = db.collection("Hotel Reservation").document(reservationId);

        reservationRef.update("roomName", roomname)
                .addOnSuccessListener(aVoid -> {
                    // Room name updated successfully
                    Intent intent = new Intent(this, check_reservation.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Handle errors that occurred while updating the room name
                    Toast.makeText(this, "Failed to update room name: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
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