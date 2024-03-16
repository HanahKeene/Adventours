package com.example.adventours.ui.lists;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventours.R;
import com.example.adventours.hotelinfo;
import com.example.adventours.ui.adapters.hotelListAdapter;
import com.example.adventours.ui.models.HotelListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class hotel_lists_Activity extends AppCompatActivity {

    RecyclerView hotelRecyclerView;
    hotelListAdapter hotelListAdapter;

    TextView back;
    List<HotelListModel> hotelListModelList;
    FirebaseFirestore db;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_lists);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);

        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        hotelRecyclerView = findViewById(R.id.hotel_list_recyclerview);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        db = FirebaseFirestore.getInstance();

        hotelListModelList = new ArrayList<>();

        hotelListAdapter = new hotelListAdapter(this, hotelListModelList, new hotelListAdapter.OnHotelListItemClickListener() {
            @Override
            public void onHotelListItemClick(String hotelId) {
                Intent intent = new Intent(hotel_lists_Activity.this, hotelinfo.class);
                intent.putExtra("hotel_id", hotelId);
                startActivity(intent);
            }
        });

        hotelRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        hotelRecyclerView.setAdapter(hotelListAdapter);

        db.collection("Hotels")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Task<QuerySnapshot>> roomTasks = new ArrayList<>(); // Keep track of room fetch tasks

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HotelListModel hotelListModel = document.toObject(HotelListModel.class);
                                String hotel_id = document.getId(); // Retrieve document ID
                                hotelListModel.setHotel_id(hotel_id);

                                // Query the subcollection "rooms" for each hotel
                                Task<QuerySnapshot> roomTask = db.collection("Hotels").document(hotel_id).collection("Rooms")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> roomTask) {
                                                if (roomTask.isSuccessful()) {
                                                    List<Double> roomPrices = new ArrayList<>();
                                                    for (QueryDocumentSnapshot roomDocument : roomTask.getResult()) {
                                                        // Retrieve room prices and add them to the list
                                                        double price = roomDocument.getDouble("price");
                                                        roomPrices.add(price);
                                                    }

                                                    // Calculate the lowest and highest prices
                                                    double minPrice = roomPrices.isEmpty() ? 0.0 : Collections.min(roomPrices);
                                                    double maxPrice = roomPrices.isEmpty() ? 0.0 : Collections.max(roomPrices);

                                                    // Set the lowest and highest prices in HotelListModel
                                                    hotelListModel.setLowestPrice(minPrice);
                                                    hotelListModel.setHighestPrice(maxPrice);

                                                    // Add the hotel to the list
                                                    hotelListModelList.add(hotelListModel);

                                                    // Notify adapter after adding new data
                                                    hotelListAdapter.notifyDataSetChanged();
                                                } else {
                                                    Toast.makeText(hotel_lists_Activity.this, "Error fetching rooms: " + roomTask.getException(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                roomTasks.add(roomTask); // Add room fetch task to the list
                            }

                            // Combine all room fetch tasks into a single task
                            Task<Void> combinedTask = Tasks.whenAll(roomTasks);
                            combinedTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure of combined task, if needed
                                    Toast.makeText(hotel_lists_Activity.this, "Error fetching rooms: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(hotel_lists_Activity.this, "Error fetching hotels: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
