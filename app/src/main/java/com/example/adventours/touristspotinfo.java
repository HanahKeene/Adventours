package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adventours.ui.adapters.activityAdapter;
import com.example.adventours.ui.adapters.hotelAdapter;
import com.example.adventours.ui.adapters.restaurantAdapter;
import com.example.adventours.ui.models.HotelsModel;
import com.example.adventours.ui.models.RestaurantsModel;
import com.example.adventours.ui.models.activityModel;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.example.adventours.ui.adapters.photogalleryAdapter;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class touristspotinfo extends AppCompatActivity {

    private static final String CATEGORY_FIELD = "location";

    private ImageView img_spot;

    private TextView placeTextView;
    private TextView locationTextView;
    private TextView descriptionTextView;

    private RecyclerView photogalleryRecyclerView, activitiesRecyclerView, hotelsRecyclerview, restaurantRecyclerview;

    private FirebaseFirestore db;

    @Override


    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touristspotinfo);

        img_spot = findViewById(R.id.spot_img);
        placeTextView = findViewById(R.id.spot_place);
        locationTextView = findViewById(R.id.spot_location);
        descriptionTextView = findViewById(R.id.spot_desc);
        photogalleryRecyclerView = findViewById(R.id.galleryRecyclerview);
        activitiesRecyclerView = findViewById(R.id.roomsrecyclerview);
        hotelsRecyclerview = findViewById(R.id.hotelsrecyclerview);
        restaurantRecyclerview = findViewById(R.id.nearbyrestaurecyclerview);



        db = FirebaseFirestore.getInstance();

        // Get the spot ID from the Intent
        Intent intent = getIntent();
        String spotId = intent.getStringExtra("spot_id");

        // Fetch the other details of the spot from Firebase
        fetchSpotDetailsFromFirebase(spotId);

        DocumentReference spotRef = db.collection("Tourist Spots").document(spotId);
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
                    restauList.add(restauModel);
                }
            } else {
                Log.e("HotelAdapter", "Error fetching hotels: " + task.getException());
                Toast.makeText(touristspotinfo.this, "Error fetching hotels: " + task.getException(), Toast.LENGTH_SHORT).show();
            }

            // Set up the RecyclerView with the hotelList using an adapter
            restaurantAdapter adapter = new restaurantAdapter(this, restauList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            restaurantRecyclerview.setLayoutManager(layoutManager);
            restaurantRecyclerview.setAdapter(adapter);

            Log.d("HotelAdapter", "Number of resturant fetched: " + restauList.size());

            adapter.notifyDataSetChanged(); // Notify adapter after setting up
        });
    }

    private void fetchSpotDetailsFromFirebase(String spotId) {
        DocumentReference spotRef = db.collection("Tourist Spots").document(spotId);

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

                // Retrieve image URLs from the sub-collection
                spotRef.collection("Photo Gallery").get().addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<String> imageUrls = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String imageUrl = document.getString("img_url");
                        if (imageUrl != null) {
                            imageUrls.add(imageUrl);
                        }
                    }

                    // Set up the RecyclerView with the image URLs using photogalleryAdapter
                    photogalleryAdapter adapter = new photogalleryAdapter(this, imageUrls);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                    photogalleryRecyclerView.setLayoutManager(layoutManager);
                    photogalleryRecyclerView.setAdapter(adapter);

                }).addOnFailureListener(e -> {
                    // Handle errors that occurred while fetching spot details
                });

                // Retrieve activities from the "Activities" sub-collection
                spotRef.collection("Activities").get().addOnSuccessListener(activitiesSnapshots -> {
                    ArrayList<activityModel> activityModelList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : activitiesSnapshots) {
                        String imageUrl = document.getString("img_url");
                        String activityname = document.getString("name");
                        if (imageUrl != null && activityname != null) {
                            activityModel activityItem = new activityModel(imageUrl, activityname);
                            activityModelList.add(activityItem);
                        }
                    }

                    // Set up the RecyclerView with the activityModel objects using activityAdapter
                    activityAdapter adapter = new activityAdapter(this, activityModelList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                    activitiesRecyclerView.setLayoutManager(layoutManager);
                    activitiesRecyclerView.setAdapter(adapter);
                }).addOnFailureListener(e -> {
                    // Handle errors that occurred while fetching data from the "Activities" sub-collection
                });

            } else {
                // Handle the case where the document does not exist
            }
        }).addOnFailureListener(e -> {
            // Handle errors that occurred while fetching spot details
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
                        Toast.makeText(touristspotinfo.this, "Clicked Hotel ID: " + hotelId, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(touristspotinfo.this, hotelinfo.class);
                        intent.putExtra("hotel_id", hotelId);
                        startActivity(intent);
                    }
                };

                hotelAdapter adapter = new hotelAdapter(this, hotelList, hotelItemClickListener);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                hotelsRecyclerview.setLayoutManager(layoutManager);
                hotelsRecyclerview.setAdapter(adapter);

                Log.d("HotelAdapter", "Number of hotels fetched: " + hotelList.size());

                adapter.notifyDataSetChanged(); // Notify adapter after setting up
            } else {
                Log.e("HotelAdapter", "Error fetching hotels: " + task.getException());
                Toast.makeText(touristspotinfo.this, "Error fetching hotels: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}