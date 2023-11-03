package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.adventours.ui.adapters.activityAdapter;
import com.example.adventours.ui.models.activityModel;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.example.adventours.ui.adapters.photogalleryAdapter;
import com.example.adventours.ui.models.TouristSpotDetails;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class touristspotinfo extends AppCompatActivity {

    private ImageView img_spot;

    private TextView placeTextView;
    private TextView locationTextView;
    private TextView descriptionTextView;

    private RecyclerView photogalleryRecyclerView, activitiesRecyclerView;

    private FirebaseFirestore db;

    @Override


    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touristspotinfo);

        img_spot = findViewById(R.id.spot_img);
        placeTextView = findViewById(R.id.spot_place);
        locationTextView = findViewById(R.id.spot_location);
        descriptionTextView = findViewById(R.id.spot_desc);
        photogalleryRecyclerView = findViewById(R.id.galleryRecyclerview);
        activitiesRecyclerView = findViewById(R.id.activitiesrecyclerview);



        db = FirebaseFirestore.getInstance();

        // Get the spot ID from the Intent
        Intent intent = getIntent();
        String spotId = intent.getStringExtra("spot_id");

        // Fetch the other details of the spot from Firebase
        fetchSpotDetailsFromFirebase(spotId);

    }

    private void fetchSpotDetailsFromFirebase(String spotId) {
        DocumentReference spotRef = db.collection("Tourist Spots").document(spotId);

        spotRef.get().addOnSuccessListener(documentSnapshot -> {
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

                // Load the spot image into img_spot ImageView
                if (spotImageUrl != null) {
                    Glide.with(this)
                            .load(spotImageUrl)
                            .into(img_spot);
                }

                // Retrieve image URLs from the sub-collection
                spotRef.collection("Photo Gallery").get().addOnSuccessListener(queryDocumentSnapshots -> {
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

                // Retrieve activities from the "Activities" sub-collection
                spotRef.collection("Activities").get().addOnSuccessListener(activitiesSnapshots -> {
                    ArrayList<activityModel> activityModelList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : activitiesSnapshots) {
                        String imageUrl = document.getString("img_url");
                        String activityname = document.getString("name");
                        if (imageUrl != null && activityname != null) {
                            activityModel activityItem = new activityModel(imageUrl, activityname);
                            activityModelList.add(activityItem);
                        }
                    }

                    // Set up the RecyclerView with the activityModel objects using activityAdapter
                    activityAdapter adapter = new activityAdapter(this, activityModelList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                    activitiesRecyclerView.setLayoutManager(layoutManager);
                    activitiesRecyclerView.setAdapter(adapter);
                }).addOnFailureListener(e -> {
                    // Handle errors that occurred while fetching data from the "Activities" sub-collection
                });

            } else {
                // Handle the case where the document does not exist
            }
        }).addOnFailureListener(e -> {
            // Handle errors that occurred while fetching spot details
        });
    }
}