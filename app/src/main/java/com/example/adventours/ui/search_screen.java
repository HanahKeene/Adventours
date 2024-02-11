package com.example.adventours.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.adventours.R;
import com.example.adventours.ui.adapters.CombinedAdapter;
import com.example.adventours.ui.models.HotelsModel;
import com.example.adventours.ui.models.RestaurantsModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class search_screen extends AppCompatActivity {

    TextInputEditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);

        searchEditText = findViewById(R.id.searchfld);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = s.toString().trim();
                if (!searchText.isEmpty()) {
                    searchInFirestore(searchText);
                } else {
                    // Clear search results if search text is empty
                    // Clear or hide RecyclerViews
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }
    private void searchInFirestore(String searchText) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<HotelsModel> hotelResults = new ArrayList<>();
        List<RestaurantsModel> restaurantResults = new ArrayList<>();
        
        db.collection("Hotels")
                .whereEqualTo("name", searchText)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<HotelsModel> hotels = queryDocumentSnapshots.toObjects(HotelsModel.class);
                    hotelResults.addAll(hotels);
                    updateRecyclerView(hotelResults, restaurantResults);
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                });
        
        db.collection("Restaurants")
                .whereEqualTo("name", searchText)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<RestaurantsModel> restaurants = queryDocumentSnapshots.toObjects(RestaurantsModel.class);
                    restaurantResults.addAll(restaurants);
                    updateRecyclerView(hotelResults, restaurantResults);
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                });
    }

    private void updateRecyclerView(List<HotelsModel> hotelResults, List<RestaurantsModel> restaurantResults) {
        List<Object> combinedResults = new ArrayList<>();
        combinedResults.addAll(hotelResults);
        combinedResults.addAll(restaurantResults);

        RecyclerView recyclerView = findViewById(R.id.searchresults);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CombinedAdapter adapter = new CombinedAdapter(combinedResults);
        recyclerView.setAdapter(adapter);
    }



}