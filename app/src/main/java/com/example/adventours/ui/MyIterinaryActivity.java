package com.example.adventours.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adventours.R;
import com.example.adventours.touristspotinfo;
import com.example.adventours.ui.adapters.FYPAdapter;
import com.example.adventours.ui.adapters.ItineraryAdapter;
import com.example.adventours.ui.models.CategoryModel;
import com.example.adventours.ui.models.ItineraryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyIterinaryActivity extends AppCompatActivity {

    List <ItineraryModel> itineraryModelList;

    ItineraryAdapter itineraryAdapter;

    ItineraryAdapter.OnItineraryItemClickListener listener;

    TextView back;

    RecyclerView existingitinerary;

    private FirebaseAuth auth;
    private FirebaseUser user;

    ImageView pictureView;
    FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_iterinary);

        auth = FirebaseAuth.getInstance();

        addButton = findViewById(R.id.addButton);
        back = findViewById(R.id.back);
        existingitinerary = findViewById(R.id.existingitinerary);
        back.setOnClickListener(View -> finish());

        itineraryModelList = new ArrayList<>();
        itineraryAdapter = new ItineraryAdapter(MyIterinaryActivity.this, itineraryModelList, listener);
        existingitinerary.setLayoutManager(new GridLayoutManager(this, 3));
        existingitinerary.setAdapter(itineraryAdapter);


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
                            itineraryAdapter.notifyDataSetChanged();
                        }
                    }
                });
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MyIterinaryActivity.this, newitineraryplan.class);
            startActivity(intent);
        });
    }
}