package com.example.adventours.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adventours.MainActivity;
import com.example.adventours.R;
import com.example.adventours.touristspotinfo;
import com.example.adventours.ui.adapters.ItineraryAdapter;
import com.example.adventours.ui.adapters.selectitineraryAdapter;
import com.example.adventours.ui.models.ItineraryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class select_itinerary extends AppCompatActivity {

    List<ItineraryModel> itineraryModelList;

    selectitineraryAdapter selectitineraryAdapter;

    selectitineraryAdapter.OnItineraryItemClickListener listener;

    private FirebaseAuth auth;
    private FirebaseUser user;

    RecyclerView itineraries;

    TextView back;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_itinerary);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);

        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        back = findViewById(R.id.back);
        itineraries = findViewById(R.id.itineraries);

        back.setOnClickListener(View -> finish());

        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String spot_id = intent.getStringExtra("Spot_ID");
        String restau_id = intent.getStringExtra("Restau_ID");
        String hotel_id = intent.getStringExtra("Hotel_ID");
        String source = getIntent().getStringExtra("source");


        itineraryModelList = new ArrayList<>();
        selectitineraryAdapter = new selectitineraryAdapter(select_itinerary.this, itineraryModelList, listener, spot_id, restau_id, hotel_id, source);
        itineraries.setLayoutManager(new GridLayoutManager(this, 3));
        itineraries.setAdapter(selectitineraryAdapter);

        // Firebase data fetching and visibility control
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();

        db.collection("users").document(userId).collection("itineraries")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            itineraryModelList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ItineraryModel itineraryModel = document.toObject(ItineraryModel.class);
                                String itineraryId = document.getId();
                                itineraryModel.setId(itineraryId);
                                itineraryModelList.add(itineraryModel);
                            }
                            selectitineraryAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}