package com.example.adventours.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.adventours.R;
import com.example.adventours.ui.adapters.activereservationAdapter;
import com.example.adventours.ui.adapters.historyreservationAdapter;
import com.example.adventours.ui.models.activereservationModel;
import com.example.adventours.ui.models.historyreservationModel;
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

public class reservation_history extends Fragment {

    RecyclerView list;
    historyreservationAdapter adapter;
    private List<historyreservationModel> historyreservationModelList;
    private Timer timer;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reservation_history, container, false);
        sharedPreferences = getActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        list = rootView.findViewById(R.id.reservations);

        historyreservationModelList = new ArrayList<>();
        adapter = new historyreservationAdapter(getContext(), historyreservationModelList);
        list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        list.setAdapter(adapter);

        startDataRefreshTask();
        return rootView;
    }

    private void startDataRefreshTask() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        List<String> statusList = Arrays.asList("Completed", "Cancelled", "Expired", "No Show", "Rejected");

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
                                historyreservationModel reservation = document.toObject(historyreservationModel.class);
                                if (reservation.getHotelName() != null) {
                                    historyreservationModelList.add(reservation);
                                }
                                adapter.notifyDataSetChanged();
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "Error fetching categories: " + task.getException(), Toast.LENGTH_SHORT).show();
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
                        if (task.isSuccessful()) {// Clear the list before adding new data
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                historyreservationModel reservation = document.toObject(historyreservationModel.class);
                                if (reservation.getHotelName() != null) {
                                    historyreservationModelList.add(reservation);
                                }
                                adapter.notifyDataSetChanged();
                            }
                            adapter.notifyDataSetChanged(); // Notify adapter after adding new data
                        } else {
                            Toast.makeText(getActivity(), "Error fetching categories: " + task.getException(), Toast.LENGTH_SHORT).show();
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