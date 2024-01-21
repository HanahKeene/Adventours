package com.example.adventours.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.adventours.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MyIterinaryActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;

    ImageView pictureView;
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_iterinary);

        auth = FirebaseAuth.getInstance();

        pictureView = findViewById(R.id.pictureView);
        addButton = findViewById(R.id.addButton);

        // Firebase data fetching and visibility control
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference itinerariesRef = db.collection("users").document(userId).collection("itineraries");
        itinerariesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot.isEmpty()) {
                    pictureView.setVisibility(View.GONE);
                    addButton.setVisibility(View.VISIBLE);
                } else {
                    pictureView.setVisibility(View.VISIBLE);
                    addButton.setVisibility(View.VISIBLE);
                    // Load picture using retrieved data and chosen library
                }
            } else {
                // Handle errors
            }
        });

        // Add button click listener and navigation
        addButton.setOnClickListener(v -> {
            // Intent to start activity for creating itinerary
        });

    }
}