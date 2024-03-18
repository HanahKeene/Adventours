package com.example.adventours.ui.myiterinary;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventours.databinding.FragmentMyiterinaryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.protobuf.Timestamp;

import com.example.adventours.R;

import java.util.Calendar;

public class MyIterinaryFragment extends Fragment {

    private FragmentMyiterinaryBinding binding;
    private SharedPreferences sharedPreferences;

    private RecyclerView activitiesRecyclerView;
    private CalendarView calendarView;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyIterinaryViewModel MyIterinaryViewModel =
                new ViewModelProvider(this).get( MyIterinaryViewModel .class);

        binding = FragmentMyiterinaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedPreferences = getActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView
        activitiesRecyclerView = root.findViewById(R.id.recyclerView);
        activitiesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize CalendarView
        calendarView = root.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, 1);

            // Convert to Firestore Timestamp
            Timestamp startTimestamp = Timestamp.newBuilder()
                    .setSeconds(selectedDate.getTimeInMillis() / 1000)
                    .build();

            int numberOfDaysInMonth = selectedDate.getActualMaximum(Calendar.DAY_OF_MONTH);
            Calendar endDate = (Calendar) selectedDate.clone();
            endDate.set(year, month, numberOfDaysInMonth, 23, 59, 59);

            // Convert to Firestore Timestamp
            Timestamp endTimestamp = Timestamp.newBuilder()
                    .setSeconds(endDate.getTimeInMillis() / 1000)
                    .build();

            fetchActivities(startTimestamp, endTimestamp);
        });

        return root;
    }

    private void fetchActivities(Timestamp startDate, Timestamp endDate) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();


        db.collection("users")
                .document(userId)
                .collection("itineraries")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot itinerarySnapshot : task.getResult()) {
                            // For each itinerary, query the days collection for activities within the date range
                            db.collection("users")
                                    .document(userId)
                                    .collection("itineraries")
                                    .document(itinerarySnapshot.getId())
                                    .collection("days")
                                    .whereGreaterThanOrEqualTo("date", startDate)
                                    .whereLessThanOrEqualTo("date", endDate)
                                    .get()
                                    .addOnCompleteListener(dayTask -> {
                                        if (dayTask.isSuccessful()) {
                                            // Process the retrieved activities and update RecyclerView
                                            // You need to create an adapter and add the retrieved activities to it
                                            // Assuming you have an adapter called ActivitiesAdapter, you would do something like this:
                                            // adapter.addAll(dayTask.getResult().toObjects(YourActivityModel.class));
                                        } else {
                                            // Handle errors
                                        }
                                    });
                        }
                    } else {
                        // Handle errors
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
