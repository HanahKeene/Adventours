package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.adventours.ui.adapters.activityAdapter;
import com.example.adventours.ui.adapters.eventAdapter;
import com.example.adventours.ui.adapters.photogalleryAdapter;
import com.example.adventours.ui.adapters.roomAdapter;
import com.example.adventours.ui.models.activityModel;
import com.example.adventours.ui.models.eventModel;
import com.example.adventours.ui.models.roomModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class events_info extends AppCompatActivity {

    ImageButton back;
    private ImageView img_spot;

    private TextView placeTextView;
    private TextView locationTextView;
    private TextView descriptionTextView;

    private RecyclerView eventsRecyclerview;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_info);

        db = FirebaseFirestore.getInstance();

        back = findViewById(R.id.backbtn);
        img_spot = findViewById(R.id.spot_img);
        placeTextView = findViewById(R.id.spot_place);
        locationTextView = findViewById(R.id.spot_location);
        descriptionTextView = findViewById(R.id.spot_desc);
        eventsRecyclerview = findViewById(R.id.eventslists);


        back.setOnClickListener(View -> finish());
        locationTextView.setOnClickListener(View -> openlocation());

        Intent intent = getIntent();
        String eventsId = intent.getStringExtra("events_id");

        fetchEventsDetailsFromFirebase(eventsId);

        DocumentReference spotRef = db.collection("Events").document(eventsId);
        spotRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String location = documentSnapshot.getString("location");
                fetchHotelListsFromDatabase(location);
            }
        });

        spotRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String location = documentSnapshot.getString("location");
                fetchRestauListsFromDatabase(location);
            }
        });
    }

    private void fetchRestauListsFromDatabase(String location) {
    }

    private void fetchHotelListsFromDatabase(String location) {
    }

    private void fetchEventsDetailsFromFirebase( String eventsId) {

        DocumentReference spotRef = db.collection("Events").document(eventsId);

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

                List<eventModel> eventModelList = new ArrayList<>();

                spotRef.collection("Activities").get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots) {
                        String img_url = documentSnapshot1.getString("img_url");
                        String name1 = documentSnapshot1.getString("name");
                        String desc1 = documentSnapshot1.getString("desc");

                        // Create a Room object
                        eventModel event = new eventModel(img_url, name1, desc1);

                        // Add the room to the list
                        eventModelList.add(event);
                    }

                    eventAdapter eventAdapter = new eventAdapter(this, eventModelList);

                    eventsRecyclerview.setAdapter(eventAdapter);
                    eventsRecyclerview.setLayoutManager(new LinearLayoutManager(this));
                }).addOnFailureListener(e -> {
                    // Handle errors that occurred while fetching room details
                });
            }
        }).addOnFailureListener(e -> {
            // Handle errors that occurred while fetching spot details
        });
    }


    private void openlocation() {
    }
}