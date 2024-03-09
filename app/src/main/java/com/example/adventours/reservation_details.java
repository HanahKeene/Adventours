package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class reservation_details extends AppCompatActivity {

    TextView place, name, address, number, email, guestslabel, quantitylabel, reservationnumber, reservationDate, quantityvalue, reservationobject, checkin, checkout, expiration ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation_details);

        place = findViewById(R.id.place);
        name = findViewById(R.id.name);
        address = findViewById(R.id.add);
        number = findViewById(R.id.number);
        email = findViewById(R.id.email);
        reservationnumber = findViewById(R.id.reservationNumber);
        reservationDate = findViewById(R.id.reservationdate);
        reservationobject = findViewById(R.id.objecttxtfld);
        quantityvalue = findViewById(R.id.qtyofroom);
        checkin = findViewById(R.id.checkinfield);
        checkout = findViewById(R.id.checkoutfield);
        expiration = findViewById(R.id.expiration);
        guestslabel = findViewById(R.id.guests);
        quantitylabel = findViewById(R.id.quatity);

        Intent intent = getIntent();
        String reservationid = intent.getStringExtra("reservation_id");
        String reservationType = intent.getStringExtra("reservationType");

        if ("Hotel Reservation".equals(reservationType)) {

            fetchhotelreservationDetails(reservationid);

        } else if ("Restaurant Reservation".equals(reservationType)){

            quantitylabel.setVisibility(View.GONE);
            quantityvalue.setVisibility(View.GONE);
            guestslabel.setText("Guests");
            fetchrestaurantreservationDetails(reservationid);

        } else {
            finish();
        }
    }

    private void fetchrestaurantreservationDetails(String reservationid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference reservationRef = db.collection("Restaurant Reservation").document(reservationid);

        reservationRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Retrieve the data and set it to the corresponding TextViews
                String placeText = documentSnapshot.getString("RestaurantName");
                String nameText = documentSnapshot.getString("CustomerName");
                String addressText = documentSnapshot.getString("Address");
                String numberText = documentSnapshot.getString("Number");
                String emailText = documentSnapshot.getString("Email");
                String reservation = documentSnapshot.getString("reservationId");
                String reservationdate = documentSnapshot.getString("Timestamp");
                String guests = documentSnapshot.getString("Guests");
                String out = documentSnapshot.getString("CheckOut");
                String in = documentSnapshot.getString("CheckOut");
                String expirationdate =  documentSnapshot.getString("Expiration");


                place.setText(placeText);
                name.setText(nameText);
                address.setText(addressText);
                number.setText(numberText);
                email.setText(emailText);
                reservationnumber.setText(reservation);
                reservationDate.setText(reservationdate);
                quantityvalue.setText(guests);
                checkin.setText(in);
                checkout.setText(out);
                expiration.setText(expirationdate);

            } else {
                // Document does not exist
                // Handle the case where the document does not exist
            }
        }).addOnFailureListener(e -> {
            // Error occurred while fetching the document
            // Handle the error
        });
    }

    private void fetchhotelreservationDetails(String reservationid) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference reservationRef = db.collection("Hotel Reservation").document(reservationid);

        reservationRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Retrieve the data and set it to the corresponding TextViews
                String placeText = documentSnapshot.getString("HotelName");
                String nameText = documentSnapshot.getString("CustomerName");
                String addressText = documentSnapshot.getString("Address");
                String numberText = documentSnapshot.getString("Number");
                String emailText = documentSnapshot.getString("Email");
                String reservation = documentSnapshot.getString("reservationId");
                String reservationdate = documentSnapshot.getString("Timestamp");
                String quantityroom = documentSnapshot.getString("Quantity");
                String roomType = documentSnapshot.getString("RoomName");
                String out = documentSnapshot.getString("CheckOut");
                String in = documentSnapshot.getString("CheckOut");
                String expirationdate =  documentSnapshot.getString("Expiration");


                place.setText(placeText);
                name.setText(nameText);
                address.setText(addressText);
                number.setText(numberText);
                email.setText(emailText);
                reservationnumber.setText(reservation);
                reservationDate.setText(reservationdate);
                quantityvalue.setText(quantityroom);
                reservationobject.setText(roomType);

                checkin.setText(in);
                checkout.setText(out);
                expiration.setText(expirationdate);

            } else {
                // Document does not exist
                // Handle the case where the document does not exist
            }
        }).addOnFailureListener(e -> {
            // Error occurred while fetching the document
            // Handle the error
        });
    }
}