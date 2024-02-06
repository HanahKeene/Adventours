package com.example.adventours.ui.Notifications;

import static android.os.Build.VERSION_CODES.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.adventours.R;
import com.example.adventours.databinding.FragmentNotifBinding;
import com.example.adventours.ui.notification_activities;
import com.example.adventours.ui.notification_promotions;
import com.example.adventours.ui.notification_systemupdate;
import com.example.adventours.ui.notification_traveladvisory;

public class NotificationsFragment extends Fragment {

    private FragmentNotifBinding binding;

    TextView systemupdate, promotions, activities, travel_advisory;

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
        promotions = root.findViewById(com.example.adventours.R.id.promotionsbtn);
        activities = root.findViewById(com.example.adventours.R.id.activitiesbtn);
        travel_advisory = root.findViewById(com.example.adventours.R.id.travel_advisorybtn);

        systemupdate.setOnClickListener(View -> systemupdate());
        promotions.setOnClickListener(View -> promotions());
        activities.setOnClickListener(View -> activities());
        travel_advisory.setOnClickListener(View -> travel_advisory());
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