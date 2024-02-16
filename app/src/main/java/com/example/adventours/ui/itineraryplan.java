        package com.example.adventours.ui;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.content.Intent;
        import android.os.Bundle;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.adventours.R;
        import com.example.adventours.ui.adapters.SubCollectionAdapter;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.firestore.CollectionReference;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.QueryDocumentSnapshot;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;

        public class itineraryplan extends AppCompatActivity {

            TextView startdatefld, endDatefld, itineraryname;
            RecyclerView dayRecyclerView;

            List<String> subCollectionNames;
            SubCollectionAdapter adapter;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.itineraryplan);

                startdatefld = findViewById(R.id.startdate);
                endDatefld = findViewById(R.id.enddate);
                itineraryname = findViewById(R.id.ItineraryName);
                dayRecyclerView = findViewById(R.id.activities);

                dayRecyclerView = findViewById(R.id.activities);
                dayRecyclerView.setLayoutManager(new LinearLayoutManager(this));

                subCollectionNames = new ArrayList<>();
                adapter = new SubCollectionAdapter(subCollectionNames);
                dayRecyclerView.setAdapter(adapter);

                Intent intent = getIntent();
                String id = intent.getStringExtra("ItineraryID");

                fetchItineraryDetails(id);
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
