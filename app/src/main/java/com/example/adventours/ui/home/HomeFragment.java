package com.example.adventours.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventours.R;
import com.example.adventours.databinding.FragmentHomeBinding;
import com.example.adventours.ui.adapters.CategoryAdapter;
import com.example.adventours.ui.adapters.FYPAdapter;
import com.example.adventours.ui.adapters.MusttryAdapter;
import com.example.adventours.ui.models.CategoryModel;
import com.example.adventours.ui.models.FYPModel;
import com.example.adventours.ui.models.MusttryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView catRecyclerview, foryouRecyclerview, musttryRecycleview;
    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryModelList;
    FYPAdapter fypAdapter;
    List<FYPModel> fypModelList;

    MusttryAdapter musttryAdapter;
    List<MusttryModel> musttryModelList;

    FirebaseFirestore db;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        catRecyclerview = root.findViewById(R.id.category);
        foryouRecyclerview = root.findViewById(R.id.fyp);
        musttryRecycleview = root.findViewById(R.id.musttry_recycleview);
        db = FirebaseFirestore.getInstance();

        // Initialize your adapters with empty lists
        categoryModelList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), categoryModelList);
        catRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        catRecyclerview.setAdapter(categoryAdapter);

        fypModelList = new ArrayList<>();
        fypAdapter = new FYPAdapter(getContext(), fypModelList);
        foryouRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        foryouRecyclerview.setAdapter(fypAdapter);

        musttryModelList = new ArrayList<>();
        musttryAdapter = new MusttryAdapter(getContext(), musttryModelList);
        musttryRecycleview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        musttryRecycleview.setAdapter(musttryAdapter);

        // Fetch and set categories
        db.collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            categoryModelList.clear(); // Clear the list before adding new data
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CategoryModel categoryModel = document.toObject(CategoryModel.class);
                                categoryModelList.add(categoryModel);
                            }
                            categoryAdapter.notifyDataSetChanged(); // Notify adapter after adding new data
                        } else {
                            Toast.makeText(getActivity(), "Error fetching categories: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Fetch and set tourist spots
        db.collection("TouristSpots")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            fypModelList.clear(); // Clear the list before adding new data
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                FYPModel fypModel= document.toObject(FYPModel.class);
                                fypModelList.add(fypModel);
                            }
                            fypAdapter.notifyDataSetChanged(); // Notify adapter after adding new data
                        } else {
                            Toast.makeText(getActivity(), "Error fetching tourist spots: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        db.collection("TouristSpots")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            musttryModelList.clear(); // Clear the list before adding new data
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                MusttryModel musttryModel= document.toObject(MusttryModel.class);
                                musttryModelList.add(musttryModel);
                            }
                            musttryAdapter.notifyDataSetChanged(); // Notify adapter after adding new data
                        } else {
                            Toast.makeText(getActivity(), "Error fetching tourist spots: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
