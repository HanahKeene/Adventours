package com.example.adventours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adventours.ui.adapters.activityAdapter;
import com.example.adventours.ui.adapters.selectActivityAdapter;
import com.example.adventours.ui.adapters.selectDayAdapter;
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

    ConstraintLayout addbutton;
    Button save;

    selectActivityAdapter selectActivityAdapter;

    List<selectActivityModel> selectActivityModelList;

    RecyclerView activityrecyclerview;


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
            // Iterate over checked items and toast each one
            StringBuilder checkedItemsString = new StringBuilder();
            for (selectActivityModel item : checkedItems) {
                checkedItemsString.append(item.getName()).append("\n");
            }
            Toast.makeText(this, "Selected activities:\n" + checkedItemsString.toString(), Toast.LENGTH_LONG).show();
        }
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
