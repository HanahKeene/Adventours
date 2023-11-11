package com.example.adventours.ui.lists;


import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventours.R;
import com.example.adventours.ui.adapters.hotelListAdapter;
import com.example.adventours.ui.adapters.restaurantListAdapter;
import com.example.adventours.ui.models.HotelListModel;
import com.example.adventours.ui.models.RestaurantListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class restaurant_lists_activity extends AppCompatActivity {

    RecyclerView ResaurantRecyclerView;

    restaurantListAdapter restaurantListAdapter;
    List<RestaurantListModel> restaurantListModelList;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_lists);

        ResaurantRecyclerView = findViewById(R.id.restaurant_list_recyclerview);
        db = FirebaseFirestore.getInstance();

        restaurantListModelList = new ArrayList<>();
        restaurantListAdapter = new restaurantListAdapter(this, restaurantListModelList, null );
        ResaurantRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        ResaurantRecyclerView.setAdapter(restaurantListAdapter);

        db.collection("Restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            restaurantListModelList.clear(); // Clear the list before adding new data
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                RestaurantListModel restaurantListModel = document.toObject(RestaurantListModel.class);
                                String restau_id = document.getId(); // Retrieve document ID
                                restaurantListModel.setRestau_id(restau_id);
                                restaurantListModelList.add(restaurantListModel);
                            }
                            restaurantListAdapter.notifyDataSetChanged(); // Notify adapter after adding new data
                        } else {
                            Toast.makeText(restaurant_lists_activity.this, "Error fetching hotels: " + task.getException(), Toast.LENGTH_SHORT).show();
//                            Log.e("FirestoreError", "Error fetching hotels", task.getException());
                        }
                    }
                });
    }
}
