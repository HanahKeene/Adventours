package com.example.adventours.ui.home;

import android.content.Intent;
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
import com.example.adventours.touristspotinfo;
import com.example.adventours.ui.adapters.CategoryAdapter;
import com.example.adventours.ui.adapters.FYPAdapter;
import com.example.adventours.ui.adapters.MusttryAdapter;
import com.example.adventours.ui.adapters.hotdealsAdapter;
import com.example.adventours.ui.lists.events_list_activity;
import com.example.adventours.ui.lists.hotel_lists_Activity;
import com.example.adventours.ui.lists.restaurant_lists_activity;
import com.example.adventours.ui.lists.tours_list_activity;
import com.example.adventours.ui.models.CategoryModel;
import com.example.adventours.ui.models.FYPModel;
import com.example.adventours.ui.models.HotDealModel;
import com.example.adventours.ui.models.MusttryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements FYPAdapter.OnFYPItemClickListener, CategoryAdapter.OnCategoryItemClickListener {

    RecyclerView catRecyclerview, foryouRecyclerview, musttryRecycleview;
    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryModelList;

    FYPAdapter fypAdapter;
    List<FYPModel> fypModelList;

    MusttryAdapter musttryAdapter;
    List<MusttryModel> musttryModelList;

    FirebaseFirestore db;
    private FragmentHomeBinding binding;
    private FirebaseAnalytics mFirebaseAnalytics;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());


        catRecyclerview = root.findViewById(R.id.category);
        foryouRecyclerview = root.findViewById(R.id.fyp);
        musttryRecycleview = root.findViewById(R.id.musttry_recycleview);
        db = FirebaseFirestore.getInstance();

        // Initialize your adapters with empty lists
        categoryModelList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), categoryModelList, this);
        catRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        catRecyclerview.setAdapter(categoryAdapter);

        fypModelList = new ArrayList<>();
        fypAdapter = new FYPAdapter(getContext(), fypModelList, this);
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
                                String cat_id = document.getId(); // Retrieve document ID
                                categoryModel.setCat_id(cat_id);
                                categoryModelList.add(categoryModel);
                            }
                            categoryAdapter.notifyDataSetChanged(); // Notify adapter after adding new data
                        } else {
                            Toast.makeText(getActivity(), "Error fetching categories: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Fetch and set tourist spots
        db.collection("Tourist Spots")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            fypModelList.clear(); // Clear the list before adding new data
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                FYPModel fypModel= document.toObject(FYPModel.class);
                                String spot_id = document.getId(); // Retrieve document ID
                                fypModel.setSpot_id(spot_id); // Set the itemId in your FYPModel object
                                fypModelList.add(fypModel);
                            }
                            fypAdapter.notifyDataSetChanged(); // Notify adapter after adding new data
                        } else {
                            Toast.makeText(getActivity(), "Error fetching tourist spots: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        db.collection("Tourist Spots")
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

    @Override
    public void onFYPItemClick(String spot_id)
    {


        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, spot_id);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        Intent intent = new Intent(getActivity(), touristspotinfo.class);
        intent.putExtra("spot_id", spot_id);


        Toast.makeText(getActivity(), "the Spot id = " + spot_id, Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    @Override
    public void onCategoryItemClick(String cat_id) {

        switch (cat_id)
        {
            //Hotel
            case "1okvU07VfkhtT5rQ7DC2":
                Intent intent1 = new Intent(getActivity(), hotel_lists_Activity.class);
                startActivity(intent1);
                break;

            //Restaurant
            case "NPUYV7WGIuCwhTc08A88":
                Intent intent2 = new Intent(getActivity(), restaurant_lists_activity.class);
                startActivity(intent2);
                break;

                //Tours
            case "ObS7vKz7Ygp3oRTWXoGr":

                Intent intent3 = new Intent(getActivity(), tours_list_activity.class);
                startActivity(intent3);
                break;

                //Events
            case "qS5pvrgnFRTZD8dfNdR6":
                Intent intent4 = new Intent(getActivity(), events_list_activity.class);
                startActivity(intent4);
                break;

            default:
                Intent intent5 = new Intent(getActivity(), hotel_lists_Activity.class);
                startActivity(intent5);
                break;

        }
    }
}
