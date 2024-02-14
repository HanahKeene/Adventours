package com.example.adventours.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adventours.R;
import com.example.adventours.User;
import com.example.adventours.ui.models.ItineraryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class itineraryplan extends AppCompatActivity {

    TextView startdatefld, endDatefld, itineraryname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itineraryplan);

        startdatefld = findViewById(R.id.startdate);
        endDatefld = findViewById(R.id.enddate);
        itineraryname = findViewById(R.id.ItineraryName);


        Intent intent = getIntent();
        String id = intent.getStringExtra("ItineraryID");

        fetchItineraryDetails(id);

    }

    private void fetchItineraryDetails(String id) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();

        DocumentReference itinerarydetails =  db.collection("users").document(userId).collection("itineraries").document(id);

        itinerarydetails.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {

                String startdate = documentSnapshot.getString("start");
                String enddate = documentSnapshot.getString("end");
                String name = documentSnapshot.getString("name");

                itineraryname.setText(name);
                startdatefld.setText(startdate);
                endDatefld.setText(enddate);

            }
        }).addOnFailureListener(e -> {
        });
    }
}