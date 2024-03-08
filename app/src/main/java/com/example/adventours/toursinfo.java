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
import com.example.adventours.ui.adapters.tourAdapter;
import com.example.adventours.ui.models.tourModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class toursinfo extends AppCompatActivity {

    ImageView cover;
    TextView tourname, accomodation, description, stay;
    RecyclerView inclusionRecyclerView;
    ImageButton back;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toursinfo);

        cover = findViewById(R.id.tour_cover);
        tourname = findViewById(R.id.tourname);
        accomodation = findViewById(R.id.guestsaccomodation);
        description = findViewById(R.id.description);
        stay = findViewById(R.id.stay);
        inclusionRecyclerView = findViewById(R.id.inclusions);
        back = findViewById(R.id.backbtn);

        back.setOnClickListener(View -> finish());

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String tour_id = intent.getStringExtra("tour_id");

        fetchtourDetails(tour_id);

    }

    private void fetchtourDetails(String tourId) {
        DocumentReference tourRef = db.collection("Tours").document(tourId);

        tourRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Retrieve basic spot details
                String name = documentSnapshot.getString("name");
                String promo_start = documentSnapshot.getString("promo_start");
                String promo_end = documentSnapshot.getString("promo_end");
                String desc = documentSnapshot.getString("desc");
                String spotImageUrl = documentSnapshot.getString("img_url");
                String days = documentSnapshot.getString("stay");


                // Set basic spot details to the corresponding views
                tourname.setText(name);
                accomodation.setText(promo_start + " - " +promo_end);
                description.setText(desc);
                stay.setText(days);

                // Load the spot image into img_spot ImageView
                if (spotImageUrl != null) {
                    Glide.with(this)
                            .load(spotImageUrl)
                            .into(cover);
                }

                tourRef.collection("Inclusions").get().addOnSuccessListener(activitiesSnapshots -> {
                    ArrayList<tourModel> tourModelList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : activitiesSnapshots) {
                        String activityname = document.getString("name");
                        if (activityname != null) {
                            tourModel activityItem = new tourModel(activityname);
                            tourModelList.add(activityItem);
                        }
                    }

                    // Set up the RecyclerView with the activityModel objects using activityAdapter
                    tourAdapter adapter = new tourAdapter(this, tourModelList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    inclusionRecyclerView.setLayoutManager(layoutManager);
                    inclusionRecyclerView.setAdapter(adapter);
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