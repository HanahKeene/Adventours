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
import com.example.adventours.ui.adapters.toursListAdapter;
import com.example.adventours.ui.lists.tours_list_activity;
import com.example.adventours.ui.models.HotelListModel;
import com.example.adventours.ui.models.ToursListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class musttry_tours extends Fragment {

    RecyclerView tours;
    toursListAdapter toursListAdapter;
    TextView back;
    List<ToursListModel> tourListModelList;
    FirebaseFirestore db;

    SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_musttry_tours, container, false);

        tours = rootView.findViewById(R.id.toursrecyclerview);

        db = FirebaseFirestore.getInstance();

        tourListModelList = new ArrayList<>();

        toursListAdapter = new toursListAdapter(new toursListAdapter.OnToursListItemClickListener() {
            @Override
            public void onTourListItemClick(String tour_id) {
                Intent intent = new Intent(getActivity(), toursinfo.class);
                intent.putExtra("tour_id", tour_id);
                startActivity(intent);
            }
        }, getActivity(), tourListModelList);

        tours.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
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
                            Toast.makeText(getActivity(), "Error fetching hotels: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return rootView;
    }
}