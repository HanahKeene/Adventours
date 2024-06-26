package com.example.adventours.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.adventours.R;
import com.example.adventours.reservation_details;
import com.example.adventours.restauinfo;
import com.example.adventours.ui.adapters.CategoryAdapter;
import com.example.adventours.ui.adapters.activereservationAdapter;
import com.example.adventours.ui.adapters.restaurantListAdapter;
import com.example.adventours.ui.lists.restaurant_lists_activity;
import com.example.adventours.ui.models.CategoryModel;
import com.example.adventours.ui.models.activereservationModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class reservation_active extends Fragment {

    RecyclerView list;
    activereservationAdapter adapter;
    private List<activereservationModel> activereservationModelList;
    private Timer timer;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reservation_active, container, false);
        sharedPreferences = getActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        list = rootView.findViewById(R.id.reservations);
        activereservationModelList = new ArrayList<>();
        adapter = new activereservationAdapter(getContext(), activereservationModelList, new activereservationAdapter.OnActiveReservationListItemClickListener() {
            @Override
            public void onActiveReservationListItemClick(String reservation_id, String reservationType) {
                String message = "Reservation ID: " + reservation_id + "\nReservation Type: " + reservationType;
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), reservation_details.class);
                intent.putExtra("reservation_id", reservation_id);
                intent.putExtra("reservationType", reservationType);
                startActivity(intent);
            }
        });
        list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        list.setAdapter(adapter);

        // Start periodic data refresh
        startDataRefreshTask();

        return rootView;
    }

    private void startDataRefreshTask() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                fetchDataFromFirebase();
            }
        }, 0, 10000); // Refresh every 5 seconds
    }

    private void fetchDataFromFirebase() {
        activereservationModelList.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        List<String> statusList = Arrays.asList("Confirmed", "Checked in", "In Progress", "On Hold", "Pending Approval", "Upcoming");

        db.collection("Hotel Reservation")
                .whereEqualTo("UserID", userId)
                .whereIn("status", statusList)
                .orderBy("Timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                activereservationModel reservation = document.toObject(activereservationModel.class);
                                if (reservation.getHotelName() != null) {
                                    activereservationModelList.add(reservation);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.e("Firestore Error", "Error fetching hotel reservations: " + task.getException());
                        }
                    }
                });

        db.collection("Restaurant Reservation")
                .whereEqualTo("UserID", userId)
                .whereIn("status", statusList)
                .orderBy("Timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                activereservationModel reservation = document.toObject(activereservationModel.class);
                                if (reservation.getRestaurantName() != null) {
                                    activereservationModelList.add(reservation);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.e("Firestore Error", "Error fetching restaurant reservations: " + task.getException());
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Stop the timer when the fragment is destroyed
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}