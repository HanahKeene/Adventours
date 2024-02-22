package com.example.adventours.ui.lists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adventours.MainActivity;
import com.example.adventours.R;
import com.example.adventours.hotelinfo;
import com.example.adventours.ui.adapters.eventsListAdapter;
import com.example.adventours.ui.adapters.hotelListAdapter;
import com.example.adventours.ui.home.HomeFragment;
import com.example.adventours.ui.models.EventsListModel;
import com.example.adventours.ui.models.HotelListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class events_list_activity extends AppCompatActivity {

    RecyclerView events;

    TextView back;

    SharedPreferences sharedPreferences;
    eventsListAdapter eventsListAdapter;

    List<EventsListModel> eventsListModelList;
    FirebaseFirestore db;

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

        back = findViewById(R.id.back);
        events = findViewById(R.id.events);

        back.setOnClickListener(View -> finish());

        db = FirebaseFirestore.getInstance();

        eventsListModelList = new ArrayList<>();

        eventsListAdapter = new eventsListAdapter(this, eventsListModelList, new eventsListAdapter.onEventsListItemClickListener() {
            @Override
            public void onEventsListItemClickListener(String event_id) {
                // Handle the click event here
//                Toast.makeText(hotel_lists_Activity.this, "Clicked Hotel ID: " + hotelId, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(events_list_activity.this, hotelinfo.class);
                intent.putExtra("event_id", hotelId);
                startActivity(intent);
            }
        });

        events.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        events.setAdapter(eventsListAdapter);

        db.collection("Hotels")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            eventsListModelList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                EventsListModel eventsListModel = document.toObject(EventsListModel.class);
                                String events_id = document.getId();
                                eventsListModel.setEvents_id(events_id);
                                eventsListModelList.add(eventsListModel);
                            }
                            eventsListAdapter.notifyDataSetChanged(); // Notify adapter after adding new data
                        } else {
                            Toast.makeText(events_list_activity.this, "Error fetching hotels: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    }
}