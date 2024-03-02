package com.example.adventours;

import androidx.annotation.NonNull;
import java.util.HashMap;
import java.util.Map;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adventours.databinding.FragmentHomeBinding;
import com.example.adventours.ui.MyIterinaryActivity;
import com.example.adventours.ui.adapters.activityAdapter;
import com.example.adventours.ui.adapters.selectActivityAdapter;
import com.example.adventours.ui.adapters.selectDayAdapter;
import com.example.adventours.ui.itineraryplan;
import com.example.adventours.ui.models.activityModel;
import com.example.adventours.ui.models.selectActivityModel;
import com.example.adventours.ui.models.selectDayModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class select_activity extends AppCompatActivity implements selectActivityAdapter.OnActivityItemClickListener {

    private static final long SUCCESS_DIALOG_DELAY = 2000;

    private Dialog savingDialog, successDialog;

    ConstraintLayout addbutton;
    Button save;

    selectActivityAdapter selectActivityAdapter;

    List<selectActivityModel> selectActivityModelList;

    RecyclerView activityrecyclerview;

    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_activity);

        activityrecyclerview = findViewById(R.id.activities);
        addbutton = findViewById(R.id.button);
        save = findViewById(R.id.savetoitinerary);

        addbutton.setOnClickListener(View -> showActivitiesDialog());
        save.setOnClickListener(View -> savetoitinerary());

        // Initialize RecyclerView
        selectActivityModelList = new ArrayList<>();
        selectActivityAdapter = new selectActivityAdapter(this, selectActivityModelList, this);
        activityrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        activityrecyclerview.setAdapter(selectActivityAdapter);

        // Fetch activities from Firebase
        Intent intent = getIntent();
        String spotId = intent.getStringExtra("SpotId");


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference spotRef = db.collection("Tourist Spots").document(spotId);

        spotRef.collection("Activities").get().addOnSuccessListener(activitiesSnapshots -> {
            for (QueryDocumentSnapshot document : activitiesSnapshots) {
                String activityName = document.getString("name");
                if (activityName != null) {
                    selectActivityModel activityItem = new selectActivityModel(activityName);
                    selectActivityModelList.add(activityItem);
                }
            }
            selectActivityAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to fetch activities", Toast.LENGTH_SHORT).show();
        });
    }

    private void savetoitinerary() {
        // Get all checked items
        List<selectActivityModel> checkedItems = selectActivityAdapter.getCheckedItems();

        // Check if there are any checked items
        if (checkedItems.isEmpty()) {
            Toast.makeText(this, "No activities selected", Toast.LENGTH_SHORT).show();
        } else {
            showSavingDialog();

            // Store each activity to Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String user = currentUser.getUid();
            Intent intent = getIntent();
            String itineraryId = intent.getStringExtra("ItineraryId");
            String dayId = intent.getStringExtra("Day");
            String spotId = intent.getStringExtra("SpotId");

            // Fetch spotName from TouristSpot collection using spotId
            db.collection("Tourist Spots").document(spotId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String spotName = documentSnapshot.getString("name");

                            for (selectActivityModel activity : checkedItems) {
                                Map<String, Object> data = new HashMap<>();
                                data.put("activity", activity.getName());
                                data.put("place", spotName);

                                // Add the activity to Firestore
                                db.collection("users").document(user).collection("itineraries").document(itineraryId).collection("days").document(dayId).collection("activities")
                                        .add(data)
                                        .addOnSuccessListener(documentReference -> {
                                            // Activity added successfully
                                            Log.d("Firestore", "Activity added: " + activity.getName());
                                        })
                                        .addOnFailureListener(e -> {
                                            // Failed to add activity
                                            Log.e("Firestore", "Error adding activity", e);
                                        });
                            }

                            // Dismiss the saving dialog after a short delay
                            new Handler().postDelayed(() -> {
                                dismissSavingDialog();
                                // Navigate to another screen here (e.g., startActivity(new Intent(this, AnotherActivity.class)))
                            }, 2000);
                        } else {
                            Log.e("Firestore", "Document does not exist for spotId: " + spotId);
                            dismissSavingDialog(); // Dismiss dialog in case of error
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Failed to fetch spotName
                        Log.e("Firestore", "Error fetching spotName", e);
                        dismissSavingDialog(); // Dismiss dialog in case of error
                    });
        }
    }


    private void dismissSavingDialog() {
        if (savingDialog != null && savingDialog.isShowing()) {
            savingDialog.dismiss();
        }

        successDialog = new Dialog(this);
        successDialog.setContentView(R.layout.prompt_success);
        successDialog.show();

        // Post a delayed action to dismiss the success dialog and start MainActivity
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (successDialog != null && successDialog.isShowing()) {
                    successDialog.dismiss();
                }

                Intent intent = new Intent(select_activity.this, MyIterinaryActivity.class);
                startActivity(intent);
            }
        }, SUCCESS_DIALOG_DELAY);
    }

    private void showSavingDialog() {

        savingDialog = new Dialog(this);
        savingDialog.setContentView(R.layout.prompt_loading_screen);

        ImageView loadingGif = savingDialog.findViewById(R.id.loading);

        Glide.with(this)
                .load(R.drawable.loading) // Replace with your GIF resource
                .into(loadingGif);

        savingDialog.show();
    }



    private void showActivitiesDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.prompt_add_custom_activity);
        dialog.show();

        Button addButton = dialog.findViewById(R.id.addbtn);
        EditText activityEditText = dialog.findViewById(R.id.new_activity);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newActivityName = activityEditText.getText().toString().trim();
                if (!newActivityName.isEmpty()) {
                    selectActivityModel newActivity = new selectActivityModel(newActivityName);
                    selectActivityModelList.add(newActivity);
                    selectActivityAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                } else {
                    Toast.makeText(select_activity.this, "Please enter a valid activity name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onCheckboxClick(selectActivityModel item, boolean isChecked) {
        // Handle checkbox click here
        if (isChecked) {
            // Perform action when checkbox is checked
            Toast.makeText(this, item.getName() + " checked", Toast.LENGTH_SHORT).show();
        } else {
            // Perform action when checkbox is unchecked
            Toast.makeText(this, item.getName() + " unchecked", Toast.LENGTH_SHORT).show();
        }

        // Get all checked items
        List<selectActivityModel> checkedItems = selectActivityAdapter.getCheckedItems();
        for (selectActivityModel checkedItem : checkedItems) {
            Log.d("Checked Item", checkedItem.getName());
            // Do whatever you need with the checked items here
        }
    }
}
