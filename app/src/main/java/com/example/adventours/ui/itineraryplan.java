package com.example.adventours.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adventours.R;
import com.example.adventours.ui.adapters.individualitineraryAdapter;
import com.example.adventours.ui.adapters.individualItineraryactivityAdapter;
import com.example.adventours.ui.models.individualitineraryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class itineraryplan extends AppCompatActivity {

    TextView startdatefld, endDatefld, itineraryname;
    RecyclerView dayRecyclerView;

    individualitineraryAdapter adapter;

    List<individualitineraryModel> individualitineraryModellist;

    private Map<String, individualItineraryactivityAdapter> dayAdapters = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itineraryplan);

        startdatefld = findViewById(R.id.startdate);
        endDatefld = findViewById(R.id.enddate);
        itineraryname = findViewById(R.id.ItineraryName);
        dayRecyclerView = findViewById(R.id.activities);

        individualitineraryModellist = new ArrayList<>();
        adapter= new individualitineraryAdapter(this, individualitineraryModellist);
        dayRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        dayRecyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        String id = intent.getStringExtra("ItineraryID");

        fetchItineraryDetails(id);
        fetchDetailedPlan(id);
    }

    private void fetchDetailedPlan(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();

        db.collection("users").document(userId).collection("itineraries").document(id).collection("days")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            individualitineraryModellist.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                individualitineraryModel individualitineraryModel = document.toObject(individualitineraryModel.class);
                                String dayId = document.getId();
                                individualitineraryModel.setId(dayId);
                                individualitineraryModellist.add(individualitineraryModel);

                                // Retrieve activities for this day
                                fetchActivitiesForDay(userId, id, dayId);
                            }
                            Log.d("Firestore", "Number of days retrieved: " + individualitineraryModellist.size());
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void fetchActivitiesForDay(String userId, String itineraryId, String dayId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId).collection("itineraries").document(itineraryId)
                .collection("days").document(dayId).collection("activities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> activities = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String activityName = document.getString("activity");
                                String location = document.getString("place");
                                activities.add(activityName + " at " + location);
                            }
                            // Find the day with the matching ID in the list
                            for (int i = 0; i < individualitineraryModellist.size(); i++) {
                                individualitineraryModel dayModel = individualitineraryModellist.get(i);
                                if (dayModel.getId().equals(dayId)) {
                                    // Set activities for this day model
                                    dayModel.setActivities(activities);
                                    // Notify adapter about data change
                                    adapter.notifyDataSetChanged(); // Notify adapter of dataset change
                                    return; // Exit the loop once the day is found
                                }
                            }
                            Log.e("Firestore", "Day with ID " + dayId + " not found in the list");
                        } else {
                            Log.e("Firestore", "Error getting activities for day " + dayId, task.getException());
                        }
                    }
                });
    }



    private individualItineraryactivityAdapter getAdapterForDay(String dayId) {
        return dayAdapters.get(dayId);
    }

    // Method to set the adapter for a specific day
    private void setAdapterForDay(String dayId, individualItineraryactivityAdapter adapter) {
        dayAdapters.put(dayId, adapter);
    }

    private void fetchItineraryDetails(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();

        DocumentReference itineraryDetails =  db.collection("users").document(userId).collection("itineraries").document(id);

        itineraryDetails.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String startDate = documentSnapshot.getString("start");
                String endDate = documentSnapshot.getString("end");
                String name = documentSnapshot.getString("name");

                itineraryname.setText(name);
                startdatefld.setText(startDate);
                endDatefld.setText(endDate);
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to fetch itinerary details", Toast.LENGTH_SHORT).show();
        });
    }



}
