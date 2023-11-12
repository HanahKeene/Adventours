package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.adventours.ui.adapters.photogalleryAdapter;
import com.example.adventours.ui.adapters.roomAdapter;
import com.example.adventours.ui.models.roomModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class hotelinfo extends AppCompatActivity {

    private ImageView img_spot;

    private TextView placeTextView;
    private TextView locationTextView;
    private TextView descriptionTextView;

    private RecyclerView photogalleryRecyclerView, roomsRecyclerview;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotelinfo);

        img_spot = findViewById(R.id.spot_img);
        placeTextView = findViewById(R.id.spot_place);
        locationTextView = findViewById(R.id.spot_location);
        descriptionTextView = findViewById(R.id.spot_desc);
        photogalleryRecyclerView = findViewById(R.id.galleryRecyclerview);
        roomsRecyclerview = findViewById(R.id.roomsrecyclerview);

        db = FirebaseFirestore.getInstance();

        // Get the spot ID from the Intent
        Intent intent = getIntent();
        String hotelId = intent.getStringExtra("hotel_id");

        // Fetch the other details of the spot from Firebase
        fetchHotelDetailsFromFirebase(hotelId);

//        DocumentReference spotRef = db.collection("Tourist Spots").document(spotId);
//        spotRef.get().addOnSuccessListener(documentSnapshot -> {
//            if (documentSnapshot.exists()) {
//                String location = documentSnapshot.getString("location");
//                fetchHotelListsFromDatabase(location);
//            }
//        });
    }

    private void fetchHotelDetailsFromFirebase(String hotelId) {

        DocumentReference hotelsRef = db.collection("Hotels").document(hotelId);

        hotelsRef.get().addOnSuccessListener(documentSnapshot -> {
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
                hotelsRef.collection("Photo Gallery").get().addOnSuccessListener(queryDocumentSnapshots -> {
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

                List<roomModel> roomModelList = new ArrayList<>();

                hotelsRef.collection("Rooms").get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots) {
                        // Retrieve room details from Firestore
                        String amenities = documentSnapshot1.getString("amenities");
                        String desc1 = documentSnapshot1.getString("desc");
                        String img_url = documentSnapshot1.getString("img_url");
                        String name1 = documentSnapshot1.getString("name");
                        String price = documentSnapshot1.getString("price");

                        // Create a Room object
                        roomModel room = new roomModel(amenities, desc1, img_url, name1, price);

                        // Add the room to the list
                        roomModelList.add(room);
                    }

                    roomAdapter roomAdapter = new roomAdapter(this, roomModelList);

                    roomsRecyclerview.setAdapter(roomAdapter);
                    roomsRecyclerview.setLayoutManager(new LinearLayoutManager(this));
                }).addOnFailureListener(e -> {
                    // Handle errors that occurred while fetching room details
                });

            }
        });
    }
}