package com.example.adventours;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musttry);

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(musttry_activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewpage);

        // Create an instance of your adapter
        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), getLifecycle());

        // Add fragments to the adapter
        vpAdapter.addFragment(new musttry_hotels(), "Hotels");
        vpAdapter.addFragment(new musttry_restaurants(), "Restaurants");
        vpAdapter.addFragment(new musttry_events(), "Tours");
        vpAdapter.addFragment(new musttry_tours(), "Events");

        // Set the adapter to the ViewPager
        viewPager.setAdapter(vpAdapter);

        // Link the TabLayout with the ViewPager
        tabLayout.addTab(tabLayout.newTab().setText("Hotels"));
        tabLayout.addTab(tabLayout.newTab().setText("Restaurants"));
        tabLayout.addTab(tabLayout.newTab().setText("Tours"));
        tabLayout.addTab(tabLayout.newTab().setText("Events"));

        // Attach the ViewPager to the TabLayout
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText("Fragment " + (position + 1))).attach();

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
