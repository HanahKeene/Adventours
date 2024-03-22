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

import com.example.adventours.ui.adapters.eventsListAdapter;
import com.example.adventours.ui.adapters.hotelListAdapter;
import com.example.adventours.ui.lists.events_lists_Activity;
import com.example.adventours.ui.models.EventsListModel;
import com.example.adventours.ui.models.HotelListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class musttry_events extends Fragment {

    RecyclerView eventsRecyclerView;
    eventsListAdapter eventsListAdapter;

    TextView back;
    List<EventsListModel> eventsListModelList;
    FirebaseFirestore db;

    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_musttry_events, container, false);

        eventsRecyclerView = rootView.findViewById(R.id.events);

        db = FirebaseFirestore.getInstance();

        eventsListModelList = new ArrayList<>();

        eventsListAdapter = new eventsListAdapter(getActivity(), eventsListModelList, new eventsListAdapter.OnEventListItemClickListener() {
            @Override
            public void onEventListItemClickListener(String event_id) {

                Intent intent = new Intent(getActivity(), events_info.class);
                intent.putExtra("events_id", event_id);
                startActivity(intent);

            }

        });

        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        eventsRecyclerView.setAdapter(eventsListAdapter);

        db.collection("Events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            eventsListModelList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                EventsListModel eventsListModel = document.toObject(EventsListModel.class);
                                String event_id = document.getId();
                                eventsListModel.setEvents_id(event_id);
                                eventsListModelList.add(eventsListModel);
                            }

                            eventsListAdapter.notifyDataSetChanged(); // Notify adapter after adding new data
                        } else {
                            Toast.makeText(getActivity(), "Error fetching hotels: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return rootView;
    }
}