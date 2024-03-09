package com.example.adventours.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.adventours.OnboardingAdapter;
import com.example.adventours.OnboardingItem;
import com.example.adventours.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class tutorial extends AppCompatActivity {

    private OnboardingAdapter onboardingAdapter;
    private LinearLayout layoutOnboardindicator;
    private Button buttonOnboardinAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        layoutOnboardindicator = findViewById(R.id.layoutOnboardingIndicators);
        buttonOnboardinAction = findViewById(R.id.buttonOnboardingAction);

        Intent intent = getIntent();
        String spotId = intent.getStringExtra("Spot_ID");
        Toast.makeText(this, "ID" + spotId, Toast.LENGTH_SHORT).show();

        setOnboardingItems();

        ViewPager2 onboardingViewPager = findViewById(R.id.onboardingViewPager);
        onboardingViewPager.setAdapter(onboardingAdapter);

        setonboardIndicators();
        setCurrentOnboardingIndicator(0);

        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicator(position);
            }
        });

        buttonOnboardinAction.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                if(onboardingViewPager.getCurrentItem() + 1 < onboardingAdapter.getItemCount()) {

                    onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem() + 1);
                } else {
                   openGoogleMaps(spotId);
                }
            }
        });
    }

    public void openGoogleMaps(String spotId) {
        // Fetch GeoPoint from Firestore (example)
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Tourist Spots").document(spotId);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Retrieve GeoPoint from document
                    GeoPoint geoPoint = documentSnapshot.getGeoPoint("coordinates");

                    // Proceed if GeoPoint is available
                    if (geoPoint != null) {
                        // Extract latitude and longitude from GeoPoint
                        double latitude = geoPoint.getLatitude();
                        double longitude = geoPoint.getLongitude();

                        // Create a Uri from the latitude and longitude
                        Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude);

                        // Create an Intent to open Google Maps with the specified location
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                        // Set the package for Google Maps
                        mapIntent.setPackage("com.google.android.apps.maps");

                        // Check if there's an activity to handle the Intent
                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            // Start Google Maps
                            startActivity(mapIntent);
                        } else {
                            // If Google Maps is not available, show a message
                            Toast.makeText(tutorial.this, "Google Maps app is not installed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // GeoPoint not found in document
                        Toast.makeText(tutorial.this, "Location not found in Firestore", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Document does not exist
                    Toast.makeText(tutorial.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Error fetching document
                Toast.makeText(tutorial.this, "Error fetching document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setOnboardingItems()
    {
        List<OnboardingItem> onboardingItems = new ArrayList<>();

        OnboardingItem tutorial1 = new OnboardingItem();
        tutorial1.setTitle("Use Google Maps even if your phone is offline");
        tutorial1.setImage(R.drawable.tutorial1);

        OnboardingItem tutorial2 = new OnboardingItem();
        tutorial2.setTitle("Swipe up!");
        tutorial2.setImage(R.drawable.t1);

        OnboardingItem tutorial3 = new OnboardingItem();
        tutorial3.setTitle("Search for your destination in Google Maps. \n" +
                "(For example: “River Palm Hotel and Resort, \n" +
                "Pangasinan City.”)");
        tutorial3.setImage(R.drawable.t2);

        OnboardingItem tutorial4 = new OnboardingItem();
        tutorial4.setTitle("Tap the three dots in the top right corner of \n" +
                "the information panel.\n");
        tutorial4.setImage(R.drawable.t3);

        OnboardingItem tutorial5 = new OnboardingItem();
        tutorial5.setTitle("Tap “Download Offline Maps.”");
        tutorial5.setImage(R.drawable.t4);

        OnboardingItem tutorial6 = new OnboardingItem();
        tutorial6.setTitle("Click “Download”");
        tutorial6.setImage(R.drawable.t5);

        OnboardingItem tutorial7 = new OnboardingItem();
        tutorial7.setTitle("Maps are downloaded already. \n" +
                "You can navigate without an internet connection");
        tutorial7.setImage(R.drawable.t6);

        onboardingItems.add(tutorial1);
        onboardingItems.add(tutorial2);
        onboardingItems.add(tutorial3);
        onboardingItems.add(tutorial4);
        onboardingItems.add(tutorial5);
        onboardingItems.add(tutorial6);
        onboardingItems.add(tutorial7);

        onboardingAdapter = new OnboardingAdapter(onboardingItems);
    }

    private void setonboardIndicators()
    {
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0, 8, 0);
        for(int i = 0; i < indicators.length; i++)
        {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.onboarding_indicator_inactive
            ));

            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardindicator.addView(indicators[i]);
        }

    }

    private void setCurrentOnboardingIndicator(int index)
    {
        int childCount = layoutOnboardindicator.getChildCount();
        for(int i = 0; i < childCount; i++)
        {
            ImageView imageview = (ImageView)layoutOnboardindicator.getChildAt(i);
            if (i == index)
            {
                imageview.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_active)
                );
            } else {
                imageview.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactive)
                );
            }
        }
        if(index == onboardingAdapter.getItemCount() -1)
        {
            buttonOnboardinAction.setText("Start");
        } else {
            buttonOnboardinAction.setText("Next");
        }
    }

}