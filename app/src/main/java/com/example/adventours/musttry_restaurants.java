package com.example.adventours;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adventours.ui.adapters.hotelListAdapter;
import com.example.adventours.ui.adapters.restaurantListAdapter;
import com.example.adventours.ui.lists.restaurant_lists_activity;
import com.example.adventours.ui.models.HotelListModel;
import com.example.adventours.ui.models.RestaurantListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class musttry_restaurants extends Fragment {

    RecyclerView  ResaurantRecyclerView;
    restaurantListAdapter restaurantListAdapter;
    List<RestaurantListModel> restaurantListModelList;
    FirebaseFirestore db;

    SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_musttry_restaurants, container, false);

        ResaurantRecyclerView = rootView.findViewById(R.id.restaurantrecyclerview);

        db = FirebaseFirestore.getInstance();

        restaurantListModelList = new ArrayList<>();
        restaurantListAdapter = new restaurantListAdapter(getActivity(), restaurantListModelList, new restaurantListAdapter.OnRestaurantItemClickListener() {
            @Override
            public void onRestaurantItemClick(String restau_id) {
                Intent intent = new Intent(getActivity(), restauinfo.class);
                intent.putExtra("restau_id", restau_id);
                startActivity(intent);
            }
        });
        ResaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
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
                                String restau_id = document.getId();
                                restaurantListModel.setRestau_id(restau_id);
                                restaurantListModelList.add(restaurantListModel);
                            }
                            restaurantListAdapter.notifyDataSetChanged(); // Notify adapter after adding new data
                        } else {
                            Toast.makeText(getActivity(), "Error fetching hotels: " + task.getException(), Toast.LENGTH_SHORT).show();
//                            Log.e("FirestoreError", "Error fetching hotels", task.getException());
                        }
                    }
                });

        return rootView;
    }
}