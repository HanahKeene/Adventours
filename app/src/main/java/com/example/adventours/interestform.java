package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.adventours.ui.adapters.interestAdapter;
import com.example.adventours.ui.models.InterestModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class interestform extends AppCompatActivity {

    private static final String CATEGORY_FIELD = "category";

    RecyclerView destinationRecyclerView, travelStyleRecyclerView, activitiesRecyclerView, budgetaccommodationRecyclerView;
    interestAdapter destinationAdapter, travelStyleAdapter, activitiesAdapter, budgetaccommodationAdapter;
    List<InterestModel> destinationInterestList, travelStyleInterestList, activitiesInterestList, budgetInterestList;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interestform);

        destinationRecyclerView = findViewById(R.id.destination);
        travelStyleRecyclerView = findViewById(R.id.travelstyle);
        activitiesRecyclerView = findViewById(R.id.activities);
        budgetaccommodationRecyclerView = findViewById(R.id.budgetandaccommodation);

        destinationInterestList = new ArrayList<>();
        travelStyleInterestList = new ArrayList<>();
        activitiesInterestList = new ArrayList<>();
        budgetInterestList = new ArrayList<>();

        destinationAdapter = new interestAdapter(interestform.this, destinationInterestList);
        travelStyleAdapter = new interestAdapter(interestform.this, travelStyleInterestList);
        activitiesAdapter = new interestAdapter(interestform.this, activitiesInterestList);
        budgetaccommodationAdapter = new interestAdapter(interestform.this, budgetInterestList);

        destinationRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        destinationRecyclerView.setAdapter(destinationAdapter);

        travelStyleRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        travelStyleRecyclerView.setAdapter(travelStyleAdapter);

        activitiesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        activitiesRecyclerView.setAdapter(activitiesAdapter);

        budgetaccommodationRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        budgetaccommodationRecyclerView.setAdapter(budgetaccommodationAdapter);

        fetchAndSetInterests("Destinations", destinationInterestList, destinationAdapter);
        fetchAndSetInterests("Travel Style", travelStyleInterestList, travelStyleAdapter);
        fetchAndSetInterests("Activities", activitiesInterestList, activitiesAdapter);
        fetchAndSetInterests("Budget and Accommodation", budgetInterestList, budgetaccommodationAdapter);
    }

    private void fetchAndSetInterests(String category, List<InterestModel> interestList, interestAdapter adapter) {
        Query interestRef = db.collection("Interest").whereEqualTo(CATEGORY_FIELD, category);

        interestRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                interestList.clear(); // Clear the list before adding new data

                for (QueryDocumentSnapshot document : task.getResult()) {
                    InterestModel interestModel = document.toObject(InterestModel.class);
                    interestModel.setCategory(category); // Set the category of the interest.
                    interestList.add(interestModel);
                }

                adapter.notifyDataSetChanged(); // Notify adapter after adding new data
            } else {
                Toast.makeText(interestform.this, "Error fetching interests: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}