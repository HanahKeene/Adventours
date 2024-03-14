package com.example.adventours;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.adventours.ui.adapters.activereservationAdapter;
import com.example.adventours.ui.adapters.roomAdapter;
import com.example.adventours.ui.models.roomModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class changeroomlist extends AppCompatActivity {

    RecyclerView hotel_list_recyclerview;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeroom);

        Intent intent = getIntent();
        String hotelname = intent.getStringExtra("HotelName");
        String roomname = intent.getStringExtra("roomname");
        String reservationId = intent.getStringExtra("reservationId");


        Toast.makeText(this, "Room Name" + roomname, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "HotelName" + hotelname, Toast.LENGTH_SHORT).show();
        hotel_list_recyclerview = findViewById(R.id.hotel_list_recyclerview);
        db = FirebaseFirestore.getInstance();

        fetchHotelID(hotelname, reservationId);
    }

    private void fetchHotelID(String hotelname, String reservationId) {
        CollectionReference hotelsRef = db.collection("Hotels");

        // Query hotels collection where the 'hotelName' field is equal to the provided hotelname
        Query query = hotelsRef.whereEqualTo("name", hotelname);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String hotelId = document.getId();
                        Toast.makeText(changeroomlist.this, "HotelID" + hotelId, Toast.LENGTH_SHORT).show();
                        fetchRooms(hotelId, reservationId);

                    }
                } else {
                     Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void fetchRooms(String hotelId, String reservationId) {
        DocumentReference hotelsRef = db.collection("Hotels").document(hotelId);

        List<roomModel> roomModelList = new ArrayList<>();
        Intent intent = getIntent();
        String roomname = intent.getStringExtra("roomname");

        // Construct a query to fetch rooms for the hotel excluding the room with the given name
        Query query = hotelsRef.collection("Rooms").whereNotEqualTo("name", roomname);

        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots) {
                String img_url = documentSnapshot1.getString("img_url");
                String name1 = documentSnapshot1.getString("name");
                String price = documentSnapshot1.getString("price");
                String room_id = documentSnapshot1.getId();
                // Create a Room object
                roomModel room = new roomModel(room_id, img_url, name1, price);

                // Add the room to the list
                roomModelList.add(room);
            }

            roomAdapter adapter = new roomAdapter(this, roomModelList, new roomAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String roomId) {
                    Intent intent = new Intent(changeroomlist.this, changeroom_details.class);
                    intent.putExtra("RoomId", roomId);
                    intent.putExtra("HotelId", hotelId);
                    intent.putExtra("reservationId", reservationId);
                    startActivity(intent);
                }
            });
            hotel_list_recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            hotel_list_recyclerview.setAdapter(adapter);
        }).addOnFailureListener(e -> {
            // Handle errors that occurred while fetching room details
        });
    }


}
