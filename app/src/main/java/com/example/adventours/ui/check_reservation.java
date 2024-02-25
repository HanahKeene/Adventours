package com.example.adventours.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.TextView;

import com.example.adventours.R;
import com.example.adventours.musttry_hotels;
import com.example.adventours.musttry_restaurants;
import com.example.adventours.ui.adapters.VPAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class check_reservation extends AppCompatActivity {

    TextView back;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_reservation);

        back = findViewById(R.id.back);
        back.setOnClickListener(View -> finish());

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewpage);

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), getLifecycle());

        // Add fragments to the adapter
        vpAdapter.addFragment(new reservation_active(), "ACTIVE");
        vpAdapter.addFragment(new reservation_history(), "HISTORY");

        viewPager.setAdapter(vpAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(vpAdapter.getPageTitle(position))).attach();

        // Add a listener to handle tab selection
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Set the current item in the ViewPager to switch fragments
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // You can perform additional actions when a tab is unselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // You can perform additional actions when a tab is reselected
            }
        });
    }
}