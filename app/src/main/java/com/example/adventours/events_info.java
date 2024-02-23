package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adventours.ui.adapters.activityAdapter;
import com.example.adventours.ui.adapters.eventAdapter;
import com.example.adventours.ui.adapters.hotelAdapter;
import com.example.adventours.ui.adapters.photogalleryAdapter;
import com.example.adventours.ui.adapters.restaurantAdapter;
import com.example.adventours.ui.adapters.roomAdapter;
import com.example.adventours.ui.models.HotelsModel;
import com.example.adventours.ui.models.RestaurantsModel;
import com.example.adventours.ui.models.activityModel;
import com.example.adventours.ui.models.eventModel;
import com.example.adventours.ui.models.roomModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class events_info extends AppCompatActivity {

    ImageButton back;
    private ImageView img_spot;

    private TextView placeTextView;
    private TextView locationTextView;
    private TextView descriptionTextView;

    private RecyclerView eventsRecyclerview, restaurantRecyclerview, hotelRecyclerview;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_info);

        db = FirebaseFirestore.getInstance();

        back = findViewById(R.id.backbtn);
        img_spot = findViewById(R.id.spot_img);
        placeTextView = findViewById(R.id.spot_place);
        locationTextView = findViewById(R.id.spot_location);
        descriptionTextView = findViewById(R.id.spot_desc);
        eventsRecyclerview = findViewById(R.id.eventslists);
        restaurantRecyclerview = findViewById(R.id.restaurants);
        hotelRecyclerview = findViewById(R.id.hotels);



        back.setOnClickListener(View -> finish());
        locationTextView.setOnClickListener(View -> openlocation());

        Intent intent = getIntent();
        String eventsId = intent.getStringExtra("events_id");

        fetchEventsDetailsFromFirebase(eventsId);

        DocumentReference spotRef = db.collection("Events").document(eventsId);
        spotRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String location = documentSnapshot.getString("location");
                fetchHotelListsFromDatabase(location);
            }
        });

        spotRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String location = documentSnapshot.getString("location");
                fetchRestauListsFromDatabase(location);
            }
        });
    }

    private void fetchRestauListsFromDatabase(String location) {

        Query restauRef = db.collection("Restaurants").whereEqualTo("location", location);

        restauRef.get().addOnCompleteListener(task -> {
            List<RestaurantsModel> restauList = new ArrayList<>();

            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    RestaurantsModel restauModel = document.toObject(RestaurantsModel.class);
                    String restau_id = document.getId();
                    restauModel.setRestau_id(restau_id);
                    restauList.add(restauModel);
                }

                restaurantAdapter.OnRestauItemClickListener restauItemClickListener = new restaurantAdapter.OnRestauItemClickListener() {
                    @Override
                    public void onRestauItemClick(String restauId) {
                        Toast.makeText(events_info.this, "Clicked Restaurant ID: " + restauId, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(events_info.this, restauinfo.class);
                        intent.putExtra("restau_id", restauId);
                        startActivity(intent);
                    }
                };

                restaurantAdapter adapter = new restaurantAdapter(this, restauList, restauItemClickListener);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                restaurantRecyclerview.setLayoutManager(layoutManager);
                restaurantRecyclerview.setAdapter(adapter);

                Log.d("HotelAdapter", "Number of hotels fetched: " + restauList.size());

                adapter.notifyDataSetChanged();
            } else {
                Log.e("HotelAdapter", "Error fetching hotels: " + task.getException());
                Toast.makeText(events_info.this, "Error fetching hotels: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchHotelListsFromDatabase(String location) {

        Query hotelRef = db.collection("Hotels").whereEqualTo("location", location);

        hotelRef.get().addOnCompleteListener(task -> {
            List<HotelsModel> hotelList = new ArrayList<>();

            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    HotelsModel hotelModel = document.toObject(HotelsModel.class);
                    String hotel_id = document.getId(); // Retrieve document ID
                    hotelModel.setHotel_id(hotel_id); // Set the itemId in your FYPModel object
                    hotelList.add(hotelModel);
                }

                // Set up the RecyclerView with the hotelList using an adapter
                hotelAdapter.OnHotelItemClickListener hotelItemClickListener = new hotelAdapter.OnHotelItemClickListener() {
                    @Override
                    public void onHotelItemClick(String hotelId) {
                        // Handle the click event here, for example, display a toast with the hotel ID
                        Toast.makeText(events_info.this, "Clicked Hotel ID: " + hotelId, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(events_info.this, hotelinfo.class);
                        intent.putExtra("hotel_id", hotelId);
                        startActivity(intent);
                    }
                };

                hotelAdapter adapter = new hotelAdapter(this, hotelList, hotelItemClickListener);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                hotelRecyclerview.setLayoutManager(layoutManager);
                hotelRecyclerview.setAdapter(adapter);

                Log.d("HotelAdapter", "Number of hotels fetched: " + hotelList.size());

                adapter.notifyDataSetChanged(); // Notify adapter after setting up
            } else {
                Log.e("HotelAdapter", "Error fetching hotels: " + task.getException());
                Toast.makeText(events_info.this, "Error fetching hotels: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchEventsDetailsFromFirebase( String eventsId) {

        DocumentReference spotRef = db.collection("Events").document(eventsId);

        spotRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Retrieve basic spot details
                String name = documentSnapshot.getString("name");
                String location = documentSnapshot.getString("location");
                String desc = documentSnapshot.getString("desc");
                String spotImageUrl = documentSnapshot.getString("img_url");


                // Set basic spot details to the corresponding views
                placeTextView.setText(name);
                locationTextView.setText(location);
                descriptionTextView.setText(desc);

                // Load the spot image into img_spot ImageView
                if (spotImageUrl != null) {
                    Glide.with(this)
                            .load(spotImageUrl)
                            .into(img_spot);
                }

                List<eventModel> eventModelList = new ArrayList<>();

                spotRef.collection("Activities").get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots) {
                        String img_url = documentSnapshot1.getString("img_url");
                        String name1 = documentSnapshot1.getString("name");
                        String desc1 = documentSnapshot1.getString("desc");

                        // Create a Room object
                        eventModel event = new eventModel(img_url, name1, desc1);

                        // Add the room to the list
                        eventModelList.add(event);
                    }

                    eventAdapter eventAdapter = new eventAdapter(this, eventModelList);

                    eventsRecyclerview.setAdapter(eventAdapter);
                    eventsRecyclerview.setLayoutManager(new LinearLayoutManager(this));
                }).addOnFailureListener(e -> {
                    // Handle errors that occurred while fetching room details
                });
            }
        }).addOnFailureListener(e -> {
            // Handle errors that occurred while fetching spot details
        });
    }


    private void openlocation() {
    }
}