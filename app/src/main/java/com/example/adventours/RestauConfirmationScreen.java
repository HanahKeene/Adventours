package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adventours.ui.ConfirmationScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RestauConfirmationScreen extends AppCompatActivity {
    TextView back, restauname, name, address, contact, emailfld, guests, checkin, checkout, notefld, expiration;
    Button submit;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restau_confirmation_screen);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);

        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        back = findViewById(R.id.back);
        restauname = findViewById(R.id.restauname);
        name = findViewById(R.id.customername);
        address = findViewById(R.id.address);
        contact = findViewById(R.id.contact);
        emailfld = findViewById(R.id.email);
        guests = findViewById(R.id.numguest);
        checkin = findViewById(R.id.checkin);
        checkout = findViewById(R.id.checkout);
        notefld = findViewById(R.id.note);
        expiration = findViewById(R.id.expiration);
        submit = findViewById(R.id.submit);

        back.setOnClickListener(View -> finish());

        Calendar currentDate = Calendar.getInstance();

        // Add three months to the current date
        Calendar expirationDate = (Calendar) currentDate.clone();
        expirationDate.add(Calendar.MONTH, 3);

        // Format the dates for display
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
        String expirationDateString = sdf.format(expirationDate.getTime());

        Intent intent = getIntent();
        String restauid = intent.getStringExtra("RestaurantID");
        String start = intent.getStringExtra("Start Date");
        String end = intent.getStringExtra("End Date");
        String adult = intent.getStringExtra("Adult");
        String child = intent.getStringExtra("Child");
        String note = intent.getStringExtra("Note");

        guests.setText(adult + " Adults and " + child + " child");
        checkin.setText(start);
        checkout.setText(end);
        notefld.setText(note);
        expiration.setText(expirationDateString);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userRef = db.collection("users").document(userId);

            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // DocumentSnapshot contains the data
                        String firstName = document.getString("firstName");
                        String lastName = document.getString("lastName");
                        String city = document.getString("city");
                        String number = document.getString("phone");
                        String email = document.getString("email");
                        // Add more fields as needed

                        // Update UI with user details
                        name.setText(firstName + " " + lastName);
                        address.setText(city);
                        contact.setText("+639"+number);
                        emailfld.setText(email);
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

            DocumentReference hotelRef = db.collection("Restaurants").document(restauid);

            hotelRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // DocumentSnapshot contains the data
                        String restauName = document.getString("name");
                        restauname.setText(restauName);
                    } else {
                        // Document does not exist,mjm
                        Toast.makeText(this, "Restaurant document does not exist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Error getting document
                    Toast.makeText(this, "Error getting hotel document: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    generateReservationIdAndAddToFirestore();
                }

                private void generateReservationIdAndAddToFirestore() {
                    // Access Firestore and get the latest reservation ID
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference reservationsRef = db.collection("Restaurant Reservation");

                    reservationsRef
                            .orderBy("reservationId", Query.Direction.DESCENDING)
                            .limit(1)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                        // Retrieve the latest reservation ID
                                        String latestReservationId = querySnapshot.getDocuments().get(0).getString("reservationId");

                                        // Increment the latest reservation ID
                                        int nextReservationNumber = Integer.parseInt(latestReservationId) + 1;
                                        String newReservationId = String.format(Locale.getDefault(), "%010d", nextReservationNumber);

                                        // Now, you can use the newReservationId for the current reservation
                                        addReservationToFirestore(newReservationId);
                                    } else {
                                        // No existing reservations, start from 1
                                        addReservationToFirestore("0000000001");
                                    }
                                } else {
                                    // Error getting reservation IDs
                                    Toast.makeText(RestauConfirmationScreen.this, "Error getting reservation IDs: " + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                private void addReservationToFirestore(String reservationId) {
                    // Create a Map with reservation details
                    Map<String, Object> reservationData = new HashMap<>();
                    String status = "Pending";
                    reservationData.put("status", status);
                    reservationData.put("reservationId", reservationId);
                    reservationData.put("CustomerName", name.getText().toString());
                    reservationData.put("Address", address.getText().toString());
                    reservationData.put("Number", contact.getText().toString());
                    reservationData.put("Email", emailfld.getText().toString());
                    reservationData.put("RestaurantName", restauname.getText().toString());
                    reservationData.put("CheckIn", checkin.getText().toString());
                    reservationData.put("CheckOut", checkout.getText().toString());
                    reservationData.put("Guests", guests.getText().toString());
                    reservationData.put("Expiration", expiration.getText().toString());

                    // Add the reservation to the "Hotel Reservation" collection with the generated ID
                    db.collection("Restaurant Reservation").document(reservationId).set(reservationData)
                            .addOnSuccessListener(aVoid -> {
                                // Document successfully added
                                Toast.makeText(RestauConfirmationScreen.this, "Reservation successful", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                // Handle errors
                                Toast.makeText(RestauConfirmationScreen.this, "Error adding reservation: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });

                    // Start the HotelReservationReceipt activity
                    Intent intent = new Intent(RestauConfirmationScreen.this, RestaurantReservationReceipt.class);
                    intent.putExtra("Restaurant ID", restauid);

                    Intent intent1 = getIntent();
                    String restaurantId = intent1.getStringExtra("RestaurantId");

                    intent.putExtra("Restaurant ID", restaurantId);

                    intent.putExtra("ReservatioNumber", reservationId);
                    intent.putExtra("CustomerName", name.getText().toString());
                    intent.putExtra("Address", address.getText().toString());
                    intent.putExtra("Number", contact.getText().toString());
                    intent.putExtra("Email", emailfld.getText().toString());
                    intent.putExtra("Guests", guests.getText().toString());
                    intent.putExtra("HotelName", restauname.getText().toString());
                    intent.putExtra("CheckIn", checkin.getText().toString());
                    intent.putExtra("CheckOut", checkout.getText().toString());
                    intent.putExtra("Expiration", expiration.getText().toString());

                    startActivity(intent);
                }

            });


        }
    }
}