package com.example.adventours.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.adventours.R;
import com.example.adventours.ui.adapters.selectDayAdapter;
import com.example.adventours.ui.models.selectDayModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class selectDay extends AppCompatActivity {

    selectDayAdapter selectDayAdapter;

    List<selectDayModel> selectDayModelList;

    String spot_id;

    selectDayAdapter.OnDayItemClickListener listener;

    RecyclerView daysrecylerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_day);

        Intent intent = getIntent();
        String spot_ID = intent.getStringExtra("Spot_ID");
        String itineraryId = intent.getStringExtra("ItineraryID");
        String source = intent.getStringExtra("Source");

        daysrecylerview = findViewById(R.id.days);

        listener = new selectDayAdapter.OnDayItemClickListener() {
            @Override
            public void onDayItemClickListener(String id) {
                // Handle item click if needed
            }
        };

        Toast.makeText(this, "Source" + source, Toast.LENGTH_SHORT).show();

        selectDayModelList = new ArrayList<>();
        selectDayAdapter = new selectDayAdapter(this, selectDayModelList, spot_ID, itineraryId, source, listener);
        daysrecylerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        daysrecylerview.setAdapter(selectDayAdapter);

        // Firebase data fetching and visibility control
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();


        db.collection("users").document(userId).collection("itineraries").document(itineraryId).collection("days")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            selectDayModelList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                selectDayModel selectDayModel = document.toObject(selectDayModel.class);
                                String dayId = document.getId();
                                selectDayModel.setId(dayId);
                                selectDayModelList.add(selectDayModel);
                            }
                            Log.d("Firestore", "Number of days retrieved: " + selectDayModelList.size());
                            selectDayAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}