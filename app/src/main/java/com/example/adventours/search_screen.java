package com.example.adventours;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventours.ui.adapters.searchAdapter;
import com.example.adventours.ui.models.searchModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class search_screen extends AppCompatActivity {

    private FirebaseFirestore db;
    private searchAdapter adapter;
    private TextInputEditText searchEditText;
    private RecyclerView showResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);

        // Initialize views
        searchEditText = findViewById(R.id.searchfld);
        showResults = findViewById(R.id.searchresults);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView
        showResults.setLayoutManager(new LinearLayoutManager(this));
        adapter = new searchAdapter();
        showResults.setAdapter(adapter);

        // Set up text change listener
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().trim();
                queryFirestore(searchText);
            }
        });
    }

    private void queryFirestore(String searchText) {
        Log.d("FirestoreQuery", "Search Text: " + searchText);

        Query searchRef = db.collection("Hotels").whereEqualTo("name", searchText);

        searchRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<searchModel> dataList = new ArrayList<>();
                QuerySnapshot queryDocumentSnapshots = task.getResult();
                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        searchModel data = documentSnapshot.toObject(searchModel.class);
                        dataList.add(data);
                    }
                    adapter.setData(dataList);
                } else {
                    Log.d("FirestoreQuery", "No documents found.");
                }
            } else {
                Log.e("FirestoreQuery", "Error getting documents: ", task.getException());
            }
        });
    }
}
