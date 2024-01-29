package com.example.adventours.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adventours.R;
import com.example.adventours.ui.adapters.ItineraryAdapter;
import com.example.adventours.ui.models.ItineraryModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MyIterinaryActivity extends AppCompatActivity {

    TextView back;
    
    RecyclerView recyclerView;

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

        back = findViewById(R.id.back);
        back.setOnClickListener(View -> finish());

        // Firebase data fetching and visibility control
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();

        CollectionReference itinerariesRef = db.collection("users").document(userId).collection("itineraries");
        itinerariesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    List<ItineraryModel> itineraryList = querySnapshot.toObjects(ItineraryModel.class);

                    // Set up RecyclerView and adapter
                    recyclerView = findViewById(R.id.existingitinerary);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    ItineraryAdapter ItineraryAdapter = new ItineraryAdapter(itineraryList);
                    recyclerView.setAdapter(ItineraryAdapter);
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