package com.example.adventours.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adventours.MainActivity;
import com.example.adventours.R;
import com.example.adventours.touristspotinfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class select_itinerary extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;

    ImageView pictureView;
    Button addButton;

    TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_itinerary);

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(select_itinerary.this, touristspotinfo.class);
                startActivity(intent);
            }
        });

        auth = FirebaseAuth.getInstance();

        pictureView = findViewById(R.id.pictureView);
        addButton = findViewById(R.id.addButton);

        // Firebase data fetching and visibility control
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();

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
            Intent intent = new Intent(this, newitineraryplan.class);
            startActivity(intent);
        });

    }
}