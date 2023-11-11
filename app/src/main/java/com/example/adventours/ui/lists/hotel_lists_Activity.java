package com.example.adventours.ui.lists;


import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventours.R;
import com.example.adventours.ui.adapters.hotelListAdapter;
import com.example.adventours.ui.models.HotelListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class hotel_lists_Activity extends AppCompatActivity {

    RecyclerView HotelRecyclerView;

    hotelListAdapter hotelListAdapter;
    List<HotelListModel> hotelListModelList;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_lists);

        HotelRecyclerView = findViewById(R.id.hotel_list_recyclerview);
        db = FirebaseFirestore.getInstance();

        hotelListModelList = new ArrayList<>();
        hotelListAdapter = new hotelListAdapter(this, hotelListModelList, null );
        HotelRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        HotelRecyclerView.setAdapter(hotelListAdapter);

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
                            Toast.makeText(hotel_lists_Activity.this, "Error fetching hotels: " + task.getException(), Toast.LENGTH_SHORT).show();
//                            Log.e("FirestoreError", "Error fetching hotels", task.getException());
                        }
                    }
                });
    }
}
