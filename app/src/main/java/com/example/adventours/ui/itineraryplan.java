        package com.example.adventours.ui;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.adventours.R;
        import com.example.adventours.ui.adapters.SubCollectionAdapter;
        import com.example.adventours.ui.adapters.individualitineraryAdapter;
        import com.example.adventours.ui.adapters.selectDayAdapter;
        import com.example.adventours.ui.models.individualitineraryModel;
        import com.example.adventours.ui.models.selectDayModel;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.firestore.CollectionReference;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.QueryDocumentSnapshot;
        import com.google.firebase.firestore.QuerySnapshot;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;

        public class itineraryplan extends AppCompatActivity {

            TextView startdatefld, endDatefld, itineraryname;
            RecyclerView dayRecyclerView;

            individualitineraryAdapter adapter;

            List<individualitineraryModel> individualitineraryModellist;



            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.itineraryplan);

                startdatefld = findViewById(R.id.startdate);
                endDatefld = findViewById(R.id.enddate);
                itineraryname = findViewById(R.id.ItineraryName);
                dayRecyclerView = findViewById(R.id.activities);


                individualitineraryModellist = new ArrayList<>();
                adapter= new individualitineraryAdapter(this, individualitineraryModellist);
                dayRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
                dayRecyclerView.setAdapter(adapter);

                Intent intent = getIntent();
                String id = intent.getStringExtra("ItineraryID");

                fetchItineraryDetails(id);
                fetchDetailedPlan(id);
            }

            private void fetchDetailedPlan(String id) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String userId = currentUser.getUid();

                db.collection("users").document(userId).collection("itineraries").document(id).collection("days")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    individualitineraryModellist.clear();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        individualitineraryModel individualitineraryModel = document.toObject(individualitineraryModel.class);
                                        String dayId = document.getId();
                                        individualitineraryModel.setId(dayId);
                                        individualitineraryModellist.add(individualitineraryModel);
                                    }
                                    Log.d("Firestore", "Number of days retrieved: " + individualitineraryModellist.size());
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
            }

            private void fetchItineraryDetails(String id) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String userId = currentUser.getUid();

                DocumentReference itineraryDetails =  db.collection("users").document(userId).collection("itineraries").document(id);

                itineraryDetails.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String startDate = documentSnapshot.getString("start");
                        String endDate = documentSnapshot.getString("end");
                        String name = documentSnapshot.getString("name");

                        itineraryname.setText(name);
                        startdatefld.setText(startDate);
                        endDatefld.setText(endDate);
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to fetch itinerary details", Toast.LENGTH_SHORT).show();
                });
            }



        }
