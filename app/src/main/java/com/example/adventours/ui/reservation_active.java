package com.example.adventours.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.adventours.R;
import com.example.adventours.ui.adapters.CategoryAdapter;
import com.example.adventours.ui.adapters.activereservationAdapter;
import com.example.adventours.ui.models.CategoryModel;
import com.example.adventours.ui.models.activereservationModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class reservation_active extends Fragment {

    RecyclerView list;


    activereservationAdapter adapter;
    private List<activereservationModel> activereservationModelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reservation_active, container, false);

        list = rootView.findViewById(R.id.reservations);

        activereservationModelList = new ArrayList<>();
        adapter = new activereservationAdapter(getContext(), activereservationModelList);
        list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        list.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();


        db.collection("Hotel Reservation")
                .whereEqualTo("UserID", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            activereservationModelList.clear(); // Clear the list before adding new data
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                activereservationModel activereservationModel = document.toObject(activereservationModel.class);
                                String reservation_id = document.getId(); // Retrieve document ID
//                                activereservationModel.setCa(reservation_id);
                                activereservationModelList.add(activereservationModel);
                            }
                            adapter.notifyDataSetChanged(); // Notify adapter after adding new data
                        } else {
                            Toast.makeText(getActivity(), "Error fetching categories: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        return rootView;
    }
}