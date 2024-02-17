package com.example.adventours;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventours.ui.adapters.Search.TouristSpotAdapter;
import com.example.adventours.ui.models.Search.TouristSpotModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class search_screen extends AppCompatActivity {

    private TextInputEditText searchEditText;
    private RecyclerView showresults;

    private FirebaseFirestore db;
    private CollectionReference toursRef;
    private TouristSpotAdapter touristSpotAdapter;
    private List<TouristSpotModel> touristSpotModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);

        searchEditText = findViewById(R.id.searchfld);
        showresults = findViewById(R.id.searchresults);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        toursRef = db.collection("Tourists Spots"); // Assuming your collection name is "tours"

        // Initialize RecyclerView
        showresults.setLayoutManager(new LinearLayoutManager(this));
        touristSpotModelList = new ArrayList<>();
        touristSpotAdapter = new TouristSpotAdapter(touristSpotModelList);
        showresults.setAdapter(touristSpotAdapter);

        // Add TextWatcher to listen for changes in search query
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter data based on search query
                String searchText = s.toString().trim();
                searchTours(searchText);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void searchTours(String searchText) {
        // Clear previous search results
        touristSpotModelList.clear();

        if (!TextUtils.isEmpty(searchText)) {
            Query query = toursRef.whereEqualTo("name", searchText);
            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        TouristSpotModel touristspot = documentSnapshot.toObject(TouristSpotModel.class);
                        touristSpotModelList.add(touristspot);
                    }
                    touristSpotAdapter.notifyDataSetChanged();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle failure
                }
            });
        } else {
            // Handle empty search query (optional)
        }
    }
}
