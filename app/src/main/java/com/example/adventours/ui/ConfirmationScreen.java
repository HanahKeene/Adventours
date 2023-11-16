package com.example.adventours.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adventours.HotelReservationReceipt;
import com.example.adventours.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ConfirmationScreen extends AppCompatActivity {

    private FirebaseFirestore db;
    TextView roomtypeName, checkin, checkout, name, add, numbertxtfld, emailtxtfld, hotelnametxtfld, qtytxtfld, expirationtxtfld, reserveNumber;

    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_screen);

        // Get the current date
        Calendar currentDate = Calendar.getInstance();

        // Add three months to the current date
        Calendar expirationDate = (Calendar) currentDate.clone();
        expirationDate.add(Calendar.MONTH, 3);

        // Format the dates for display
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
        String expirationDateString = sdf.format(expirationDate.getTime());

        Intent intent = getIntent();
        String hotelid = intent.getStringExtra("HotelId");
        String roomid = intent.getStringExtra("RoomId");
        String roomtype = intent.getStringExtra("RoomName");
        String check_in = intent.getStringExtra("CheckIn");
        String check_out = intent.getStringExtra("CheckOut");
        String roomquantity = intent.getStringExtra("RoomQuantity");

        Toast.makeText(this, "Hotel ID" + hotelid, Toast.LENGTH_SHORT).show();

        roomtypeName = findViewById(R.id.roomtypeTextView);
        checkin = findViewById(R.id.checkinfield);
        checkout = findViewById(R.id.checkoutfield);
        name = findViewById(R.id.name);
        add = findViewById(R.id.add);
        numbertxtfld = findViewById(R.id.number);
        emailtxtfld = findViewById(R.id.email);
        hotelnametxtfld = findViewById(R.id.hotelname);
        qtytxtfld = findViewById(R.id.qty);
        expirationtxtfld = findViewById(R.id.expiration);
        submit = findViewById(R.id.submit);


        qtytxtfld.setText(roomquantity + " Room/s");
        roomtypeName.setText(roomtype);
        checkin.setText(check_in);
        checkout.setText(check_out);
        expirationtxtfld.setText(expirationDateString);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // Get user details
            String userId = currentUser.getUid();

            // Access Firestore and get user details
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userRef = db.collection("users").document(userId);

            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // DocumentSnapshot contains the data
                        String firstName = document.getString("firstName");
                        String lastName = document.getString("lastName");
                        String city= document.getString("city");
                        String number = document.getString("phone");
                        String email = document.getString("email");
                        // Add more fields as needed

                        // Update UI with user details
                        name.setText(firstName + " " +lastName);
                        add.setText(city);
                        numbertxtfld.setText(number);
                        emailtxtfld.setText(email);
                        // Add more UI updates as needed
                    } else {
                        // Document does not exist
                        Toast.makeText(this, "User document does not exist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Error getting document
                    Toast.makeText(this, "Error getting user document: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            });

            DocumentReference hotelRef = db.collection("Hotels").document(hotelid);

            hotelRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // DocumentSnapshot contains the data
                        String hotelName = document.getString("name");
                        // Add more fields as needed

                        // Update UI with hotel details
                        // Assuming you have a TextView with id 'hotelNameTextView'
                        hotelnametxtfld.setText(hotelName);
                    } else {
                        // Document does not exist
                        Toast.makeText(this, "Hotel document does not exist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Error getting document
                    Toast.makeText(this, "Error getting hotel document: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String reservationId = generateReservationId();
                    String status = "Pending";

                    // Create a Map with reservation details
                    Map<String, Object> reservationData = new HashMap<>();
                    reservationData.put("CustomerName", name.getText().toString());
                    reservationData.put("Address", add.getText().toString());
                    reservationData.put("Number", numbertxtfld.getText().toString());
                    reservationData.put("Email", emailtxtfld.getText().toString());
                    reservationData.put("HotelName", hotelnametxtfld.getText().toString());
                    reservationData.put("RoomName", roomtypeName.getText().toString());
                    reservationData.put("CheckIn", checkin.getText().toString());
                    reservationData.put("CheckOut", checkout.getText().toString());
                    reservationData.put("Expiration", expirationtxtfld.getText().toString());
                    reservationData.put("Status", status);

                    // Add the reservation to the "Hotel Reservation" collection with the generated ID
                    db.collection("Hotel Reservation").document(reservationId).set(reservationData)
                            .addOnSuccessListener(aVoid -> {
                                // Document successfully added
                                Toast.makeText(ConfirmationScreen.this, "Reservation successful", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                // Handle errors
                                Toast.makeText(ConfirmationScreen.this, "Error adding reservation: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });

                    Intent intent = new Intent(ConfirmationScreen.this, HotelReservationReceipt.class);

                    Intent intent1 = getIntent();
                    String hotelid = intent1.getStringExtra("HotelId");

                    intent.putExtra("Hotel ID", hotelid);


                    intent.putExtra("CustomerName", name.getText().toString());
                    intent.putExtra("Address", add.getText().toString());
                    intent.putExtra("Number", numbertxtfld.getText().toString());
                    intent.putExtra("Email", emailtxtfld.getText().toString());

                    intent.putExtra("HotelName", hotelnametxtfld.getText().toString());
                    intent.putExtra("RoomName", roomtypeName.getText().toString());
                    intent.putExtra("CheckIn", checkin.getText().toString());
                    intent.putExtra("CheckOut", checkout.getText().toString());
                    intent.putExtra("Expiration", expirationtxtfld.getText().toString());

                    startActivity(intent);
                }

                private String generateReservationId() {
                    // Logic to generate a reservation ID in the format "0000000001"
                    // You may want to query the database to find the latest reservation ID and increment it.
                    // For simplicity, let's assume you always start from 1.

                    int nextReservationNumber = 1;
                    return String.format(Locale.getDefault(), "%010d", nextReservationNumber);
                }
            });


        }
    }

}