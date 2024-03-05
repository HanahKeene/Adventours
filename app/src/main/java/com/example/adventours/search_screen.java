package com.example.adventours;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventours.ui.adapters.FYPAdapter;
import com.example.adventours.ui.adapters.hotelListAdapter;
import com.example.adventours.ui.adapters.searchAdapter;
import com.example.adventours.ui.lists.hotel_lists_Activity;
import com.example.adventours.ui.models.HotelListModel;
import com.example.adventours.ui.models.searchModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class search_screen extends AppCompatActivity {

    FirebaseFirestore db;
    searchAdapter searchAdapter;
    TextInputEditText searchEditText;
    RecyclerView showresults;
    List<searchModel> searchModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);

        searchEditText = findViewById(R.id.searchfld);
        showresults = findViewById(R.id.searchresults);
        db = FirebaseFirestore.getInstance();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                loadallData();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().trim();
//                queryFirestore(searchText);
            }
        });

        com.example.adventours.ui.adapters.searchAdapter.SearchItemClickListener listener = new com.example.adventours.ui.adapters.searchAdapter.SearchItemClickListener() {
            @Override
            public void searchItemClickListener(String spot_id, String hotel_id, String restau_id) {

            }
        };

        searchModelList = new ArrayList<>();
        searchAdapter = new searchAdapter( this, searchModelList, listener);
        showresults.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        showresults.setAdapter(searchAdapter);

    }

    private void loadallData() {

        db.collection("Tourist Spots")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                searchModel searchModel = document.toObject(searchModel.class);
                                String spot_id = document.getId(); // Retrieve document ID
                                searchModel.setSpot_id(spot_id);
                                searchModelList.add(searchModel);
                            }

                            searchAdapter.notifyDataSetChanged(); // Notify adapter after adding new data
                        } else {
                            Toast.makeText(search_screen.this, "Error fetching hotels: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        db.collection("Hotels")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                searchModel searchModel = document.toObject(searchModel.class);
                                String hotel_id = document.getId(); // Retrieve document ID
                                searchModel.setHotel_id(hotel_id);
                                searchModelList.add(searchModel);
                            }

                            searchAdapter.notifyDataSetChanged(); // Notify adapter after adding new data
                        } else {
                            Toast.makeText(search_screen.this, "Error fetching hotels: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        db.collection("Restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                searchModel searchModel = document.toObject(searchModel.class);
                                String restau_id = document.getId();
                                searchModel.setRestau_id(restau_id);
                                searchModelList.add(searchModel);
                            }

                            searchAdapter.notifyDataSetChanged(); // Notify adapter after adding new data
                        } else {
                            Toast.makeText(search_screen.this, "Error fetching hotels: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
