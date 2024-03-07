package com.example.adventours;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adventours.ui.adapters.hotelListAdapter;
import com.example.adventours.ui.lists.hotel_lists_Activity;
import com.example.adventours.ui.models.HotelListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class musttry_hotels extends Fragment {

    RecyclerView hotelRecyclerView;
    hotelListAdapter hotelListAdapter;

    TextView back;
    List<HotelListModel> hotelListModelList;
    FirebaseFirestore db;

    SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_musttry_hotels, container, false);


        hotelRecyclerView = rootView.findViewById(R.id.hotelrecyclerview);

        db = FirebaseFirestore.getInstance();

        hotelListModelList = new ArrayList<>();

        hotelListAdapter = new hotelListAdapter(getContext(), hotelListModelList, new hotelListAdapter.OnHotelListItemClickListener() {
            @Override
            public void onHotelListItemClick(String hotelId) {
                // Handle the click event here
//                Toast.makeText(hotel_lists_Activity.this, "Clicked Hotel ID: " + hotelId, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), hotelinfo.class);
                intent.putExtra("hotel_id", hotelId);
                startActivity(intent);
            }
        });

        hotelRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        hotelRecyclerView.setAdapter(hotelListAdapter);

        db.collection("Hotels")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            hotelListModelList.clear(); // Clear the list before adding new data
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HotelListModel hotelListModel = document.toObject(HotelListModel.class);
                                String hotel_id = document.getId(); // Retrieve document ID
                                hotelListModel.setHotel_id(hotel_id);
                                hotelListModelList.add(hotelListModel);
                            }

                            hotelListAdapter.notifyDataSetChanged(); // Notify adapter after adding new data
                        } else {
                            Toast.makeText(getContext(), "Error fetching hotels: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    return rootView;
    }
}