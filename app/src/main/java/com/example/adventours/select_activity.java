package com.example.adventours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.adventours.ui.adapters.selectActivityAdapter;
import com.example.adventours.ui.adapters.selectDayAdapter;
import com.example.adventours.ui.models.selectActivityModel;
import com.example.adventours.ui.models.selectDayModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class select_activity extends AppCompatActivity {

    ConstraintLayout button;

    selectActivityAdapter selectActivityAdapter;

    List<selectActivityModel> selectActivityModelList;

    selectActivityAdapter.OnActivityItemClickListener listener;

    RecyclerView activityrecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_activity);

        button = findViewById(R.id.button);
        activityrecyclerview = findViewById(R.id.activities);

        button.setOnClickListener(View -> showActivities());


        selectActivityModelList = new ArrayList<>();
        selectActivityAdapter = new selectActivityAdapter(this, selectActivityModelList, listener);
        activityrecyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        activityrecyclerview.setAdapter(selectActivityAdapter);

        // Firebase data fetching and visibility control
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Tourist Spots").document(userId).collection("Activities").document(itineraryId).collection("days")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            selectActivityModelList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                selectActivityModel selectActivityModel = document.toObject(selectActivityModel.class);
                                String dayId = document.getId();
//                                selectActivityModel.setName(dayId);
                                selectActivityModelList.add(selectDayModel);
                            }
                            Log.d("Firestore", "Number of days retrieved: " + selectActivityModelList.size());
                            selectActivityAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void showActivities() {

        Dialog firstDialog = new Dialog(this);
        firstDialog.setContentView(R.layout.prompt_add_custom_activity);
        firstDialog.show();

//        Button insideDialogBtn = firstDialog.findViewById(R.id.submitrate);
//        insideDialogBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                firstDialog.dismiss();
//                Dialog secondDialog = new Dialog(this);
//                secondDialog.setContentView(R.layout.prompt_thanksfortherate);
//                secondDialog.show();
//
//                Button welcomebtn = secondDialog.findViewById(R.id.welcomebtn);
//                welcomebtn.setOnClickListener(View -> secondDialog.dismiss());
//            }
//        });

    }
}