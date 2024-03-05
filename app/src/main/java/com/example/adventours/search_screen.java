package com.example.adventours;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventours.ui.adapters.searchAdapter;
import com.example.adventours.ui.models.searchModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class search_screen extends AppCompatActivity implements searchAdapter.OnSearchItemClickListener {

    private FirebaseFirestore db;
    private searchAdapter searchAdapter;
    private TextInputEditText searchEditText;
    private RecyclerView showResults;
    private List<searchModel> searchModelList = new ArrayList<>();
    private List<searchModel> originalSearchModelList = new ArrayList<>(); // Maintain original data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);

        searchEditText = findViewById(R.id.searchfld);
        showResults = findViewById(R.id.searchresults);
        db = FirebaseFirestore.getInstance();

        setupSearchAdapter();
        setupSearchTextListener();
        loadData();
    }

    private void setupSearchAdapter() {
        searchAdapter = new searchAdapter(this, searchModelList, this);
        showResults.setLayoutManager(new LinearLayoutManager(this));
        showResults.setAdapter(searchAdapter);
    }

    private void setupSearchTextListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString()); // Filter data based on user input
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });
    }

    private void filterData(String searchText) {
        // Clear previous results
        searchModelList.clear();

        // Convert search text to lowercase
        String query = searchText.toLowerCase();

        // Filter data based on searchText from original data fetched from the database
        for (searchModel item : originalSearchModelList) {
            if (item.getName().toLowerCase().contains(query)) {
                searchModelList.add(item);
            }
        }

        // Notify adapter about the changes
        searchAdapter.notifyDataSetChanged();
    }

    private void loadData() {
        // Fetch data from the database
        db.collection("Tourist Spots").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                searchModel searchModel = document.toObject(searchModel.class);
                                String id = document.getId();
                                searchModel.setId(id);
                                searchModel.setSpot_id(id);
                                originalSearchModelList.add(searchModel); // Add to original list
                            }
                        } else {
                            Log.e("search_screen", "Error fetching Tourist Spots: " + task.getException());
                            showToast("Error fetching Tourist Spots");
                        }
                    }
                });

        db.collection("Hotels").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                searchModel searchModel = document.toObject(searchModel.class);
                                String id = document.getId();
                                searchModel.setId(id);
                                searchModel.setHotel_id(id);
                                originalSearchModelList.add(searchModel); // Add to original list
                            }
                        } else {
                            Log.e("search_screen", "Error fetching Hotels: " + task.getException());
                            showToast("Error fetching Hotels");
                        }
                    }
                });

        db.collection("Restaurants").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                searchModel searchModel = document.toObject(searchModel.class);
                                String id = document.getId();
                                searchModel.setId(id);
                                searchModel.setRestau_id(id);
                                originalSearchModelList.add(searchModel); // Add to original list
                            }
                        } else {
                            Log.e("search_screen", "Error fetching Restaurants: " + task.getException());
                            showToast("Error fetching Restaurants");
                        }
                    }
                });
    }

    @Override
    public void onSearchItemClick(String itemId) {
        // Check if the item ID exists in the Hotel collection
        String hotelid = itemId;
        db.collection("Hotels").document(hotelid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Toast.makeText(this, "Found in Hotels" + itemId, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(search_screen.this, hotelinfo.class);
                            intent.putExtra("hotel_id", hotelid);
                            startActivity(intent);
                        } else {
                            String restauid = itemId;
                            db.collection("Restaurants").document(restauid).get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            DocumentSnapshot document1 = task1.getResult();
                                            if (document1.exists()) {
                                                Toast.makeText(this, "Found in Restaurants" + itemId, Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(search_screen.this, restauinfo.class);
                                                intent.putExtra("restau_id", restauid);
                                                startActivity(intent);
                                            } else {
                                                String spotid = itemId;
                                                db.collection("Tourist Spots").document(spotid).get()
                                                        .addOnCompleteListener(task2 -> {
                                                            if (task2.isSuccessful()) {
                                                                DocumentSnapshot document2 = task2.getResult();
                                                                if (document2.exists()) {
                                                                    Toast.makeText(this, "Found in Tourist Spot" + itemId, Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(search_screen.this, touristspotinfo.class);
                                                                    intent.putExtra("spot_id", itemId);
                                                                    startActivity(intent);
                                                                } else {
                                                                    // If the item ID doesn't exist in any collection
                                                                    showToast("Item ID not found");
                                                                }
                                                            } else {
                                                                Log.e("search_screen", "Error fetching Tourist Spots document: " + task2.getException());
                                                                showToast("Error fetching Tourist Spots document");
                                                            }
                                                        });
                                            }
                                        } else {
                                            Log.e("search_screen", "Error fetching Restaurants document: " + task1.getException());
                                            showToast("Error fetching Restaurants document");
                                        }
                                    });
                        }
                    } else {
                        Log.e("search_screen", "Error fetching Hotels document: " + task.getException());
                        showToast("Error fetching Hotels document");
                    }
                });
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}