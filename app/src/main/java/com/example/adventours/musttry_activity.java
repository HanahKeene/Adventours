package com.example.adventours;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager2.widget.ViewPager2;

import com.example.adventours.ui.adapters.VPAdapter;
import com.example.adventours.ui.home.HomeFragment;
import com.example.adventours.ui.lists.hotel_lists_Activity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class musttry_activity extends AppCompatActivity {

    TextView back;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musttry);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);

        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        back = findViewById(R.id.back);

        back.setOnClickListener(View -> finish());

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewpage);

        // Create an instance of your adapter
        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), getLifecycle());

        // Add fragments to the adapter
        vpAdapter.addFragment(new musttry_hotels(), "Hotels");
        vpAdapter.addFragment(new musttry_restaurants(), "Restaurants");
        vpAdapter.addFragment(new musttry_tours(), "Tours");
        vpAdapter.addFragment(new musttry_events(), "Events");

        // Set the adapter to the ViewPager
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
