package com.example.adventours.ui.Notifications;

import static android.os.Build.VERSION_CODES.R;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventours.R;
import com.example.adventours.databinding.FragmentNotifBinding;
import com.example.adventours.hotelinfo;
import com.example.adventours.ui.adapters.hotelListAdapter;
import com.example.adventours.ui.adapters.notificationAdapter;
import com.example.adventours.ui.models.HotelListModel;
import com.example.adventours.ui.models.ItineraryModel;
import com.example.adventours.ui.models.NotificationModel;
import com.example.adventours.ui.notification_activities;
import com.example.adventours.ui.notification_promotions;
import com.example.adventours.ui.notification_systemupdate;
import com.example.adventours.ui.notification_traveladvisory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private FragmentNotifBinding binding;
    RecyclerView notification_recyclerview;
    FirebaseFirestore db;
    TextView systemupdate, promotions, activities, travel_advisory;
    notificationAdapter notificationAdapter;
    List<NotificationModel> notificationModelList = new ArrayList<>();

    SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel NotificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotifBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sharedPreferences = getActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        systemupdate = root.findViewById(com.example.adventours.R.id.system_updatebtn);
//        promotions = root.findViewById(com.example.adventours.R.id.promotionsbtn);
//        activities = root.findViewById(com.example.adventours.R.id.activitiesbtn);
//        travel_advisory = root.findViewById(com.example.adventours.R.id.travel_advisorybtn);
        notification_recyclerview = root.findViewById(com.example.adventours.R.id.notification_recyclerview);

        systemupdate.setOnClickListener(View -> systemupdate());
//        promotions.setOnClickListener(View -> promotions());
//        activities.setOnClickListener(View -> activities());
//        travel_advisory.setOnClickListener(View -> travel_advisory());

        notificationAdapter = new notificationAdapter(getContext(), notificationModelList, new notificationAdapter.OnNotificationListItemClickListener() {
            @Override
            public void onNotificationItemClick(String document_id, String reservation_id, String reservation_category, String status) {

                String toastMessage = "Reservation ID: " + reservation_id + ", Category: " + reservation_category + ",Status" + status;
                Toast.makeText(getContext(), toastMessage, Toast.LENGTH_SHORT).show();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String userId = currentUser.getUid();

                // Reference to the specific document in the collection
                DocumentReference notificationRef = db.collection("users").document(userId)
                        .collection("unread_notification").document(document_id);

                // Update the status field to "read"
                notificationRef.update("status", "read")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Status updated successfully to read");
                                // Optionally, you can update the UI here if needed
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating status to read", e);
                            }
                        });
            }
        });

        notification_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        notification_recyclerview.setAdapter(notificationAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();

        db.collection("users").document(userId).collection("unread_notification")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Retrieve document ID along with other notification details
                                String documentId = document.getId();
                                String title = document.getString("title");
                                String desc = document.getString("description");
                                String status = document.getString("status");
                                String reservation_id = document.getString("reservation_id");
                                String reservation_category = document.getString("reservation_category");

                                // Create a new NotificationModel instance and set the document ID and other details
                                NotificationModel notificationModel = new NotificationModel();
                                notificationModel.setTitle(title);
                                notificationModel.setDescription(desc);
                                notificationModel.setStatus(status);
                                notificationModel.setReservation_id(reservation_id);
                                notificationModel.setReservation_category(reservation_category);
                                notificationModel.setDocument_id(documentId); // Set document ID

                                // Add the notificationModel to the list
                                notificationModelList.add(notificationModel);
                            }

                            // Notify adapter after adding new data
                            notificationAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Error fetching notifications: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return root;
    }

    private void travel_advisory() {
        Intent intent = new Intent(getActivity(), notification_traveladvisory.class);
        startActivity(intent);
    }

    private void activities() {
        Intent intent = new Intent(getActivity(), notification_activities.class);
        startActivity(intent);
    }

    private void promotions() {
        Intent intent = new Intent(getActivity(), notification_promotions.class);
        startActivity(intent);
    }

    private void systemupdate() {
        Intent intent = new Intent(getActivity(), notification_systemupdate.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}