package com.example.adventours.ui.lists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adventours.R;
import com.example.adventours.toursinfo;
import com.example.adventours.ui.adapters.toursListAdapter;
import com.example.adventours.ui.models.ToursListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class tours_list_activity extends AppCompatActivity {

    TextView back;
    toursListAdapter toursListAdapter;
    List<ToursListModel> tourListModelList;
    FirebaseFirestore db;
    SharedPreferences sharedPreferences;
    RecyclerView tours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tours_list);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);

        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        back = findViewById(R.id.back);
        tours = findViewById(R.id.tours_list_recyclerview);

        back.setOnClickListener(View -> finish());

        db = FirebaseFirestore.getInstance();


        tourListModelList = new ArrayList<>();

        toursListAdapter = new toursListAdapter(new toursListAdapter.OnToursListItemClickListener() {
                    @Override
                    public void onTourListItemClick(String tour_id) {
                        Intent intent = new Intent(tours_list_activity.this, toursinfo.class);
                        intent.putExtra("hotel_id", tour_id);
                        startActivity(intent);
                    }
                }, this, tourListModelList);

        tours.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        tours.setAdapter(toursListAdapter);

        db.collection("Tours")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            tourListModelList.clear(); // Clear the list before adding new data
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ToursListModel toursListModel = document.toObject(ToursListModel.class);
                                String tours_id = document.getId(); // Retrieve document ID
                                toursListModel.setTour_id(tours_id);
                                tourListModelList.add(toursListModel);
                            }

                            toursListAdapter.notifyDataSetChanged(); // Notify adapter after adding new data
                        } else {
                            Toast.makeText(tours_list_activity.this, "Error fetching hotels: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}