package com.example.adventours.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_itinerary);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);

        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        back = findViewById(R.id.back);

        back.setOnClickListener(View -> finish());

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