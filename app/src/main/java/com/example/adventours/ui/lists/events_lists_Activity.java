package com.example.adventours.ui.lists;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventours.R;
import com.example.adventours.events_info;
import com.example.adventours.ui.adapters.eventsListAdapter;
import com.example.adventours.ui.models.EventsListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class events_lists_Activity extends AppCompatActivity {

    RecyclerView eventsRecyclerView;
    eventsListAdapter eventsListAdapter;

    TextView  back;
    List<EventsListModel> eventsListModelList;
    FirebaseFirestore db;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);

        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


        eventsRecyclerView = findViewById(R.id.events);
        back = findViewById(R.id.back);

        back.setOnClickListener(View -> finish());

        db = FirebaseFirestore.getInstance();

        eventsListModelList = new ArrayList<>();

        eventsListAdapter = new eventsListAdapter(this, eventsListModelList, new eventsListAdapter.OnEventListItemClickListener() {
            @Override
            public void onEventListItemClickListener(String event_id) {

                Intent intent = new Intent(events_lists_Activity.this, events_info.class);
                intent.putExtra("events_id", event_id);
                startActivity(intent);

            }

        });

        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
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
                            Toast.makeText(events_lists_Activity.this, "Error fetching hotels: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
