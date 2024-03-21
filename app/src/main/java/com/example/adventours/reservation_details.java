package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.adventours.ui.check_reservation;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class reservation_details extends AppCompatActivity {

    Button cancelbutton;

    TextView place, name, address, number, email, roomlabel, guestslabel, quantitylabel, reservationnumber, reservationDate, quantityvalue, roomvalue, checkinlbl, checkoutlbl, checkin, checkout, expiration ;

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
        roomlabel = findViewById(R.id.roomlabel);
        roomvalue = findViewById(R.id.roomvalue);
        quantitylabel = findViewById(R.id.quantitylabel);
        quantityvalue = findViewById(R.id.quantityvalue);
        checkinlbl = findViewById(R.id.checkinlbl);
        checkoutlbl = findViewById(R.id.checkoutlbl);
        checkin = findViewById(R.id.checkinfield);
        checkout = findViewById(R.id.checkoutfield);
        expiration = findViewById(R.id.expiration);

        cancelbutton = findViewById(R.id.cancelbutton);

        Intent intent = getIntent();
        String reservationid = intent.getStringExtra("reservation_id");
        String reservationType = intent.getStringExtra("reservationType");

        cancelbutton.setOnClickListener(View -> confirmcancelreservation(reservationType));

        if ("Hotel Reservation".equals(reservationType)) {

            fetchhotelreservationDetails(reservationid);

        } else if ("Restaurant Reservation".equals(reservationType)) {

            roomlabel.setVisibility(View.GONE);
            roomvalue.setVisibility(View.GONE);
            quantitylabel.setText("Guests");

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
                String guests = documentSnapshot.getString("Guests");
                String out = documentSnapshot.getString("Time");
                String in = documentSnapshot.getString("Date");
                String expirationdate =  documentSnapshot.getString("Expiration");

                place.setText(placeText);
                name.setText(nameText);
                address.setText(addressText);
                number.setText(numberText);
                email.setText(emailText);
                reservationnumber.setText(reservation);
                quantityvalue.setText(guests);
                checkin.setText(in);
                checkout.setText(out);
                expiration.setText(expirationdate);
                checkinlbl.setText("Date");
                checkoutlbl.setText("Time");

                com.google.firebase.Timestamp timestamp = documentSnapshot.getTimestamp("Timestamp");
                if (timestamp != null) {
                    String reservationdate = timestamp.toDate().toString();
                    reservationDate.setText(reservationdate);
                }

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
                String quantityroom = documentSnapshot.getString("Quantity");
                String roomType = documentSnapshot.getString("RoomName");
                String expirationdate =  documentSnapshot.getString("Expiration");
                String out = documentSnapshot.getString("CheckOut");
                String in = documentSnapshot.getString("CheckOut");

                place.setText(placeText);
                name.setText(nameText);
                address.setText(addressText);
                number.setText(numberText);
                email.setText(emailText);
                reservationnumber.setText(reservation);
                quantityvalue.setText(quantityroom);
                roomvalue.setText(roomType);
                checkin.setText(in);
                checkout.setText(out);
                expiration.setText(expirationdate);

                // Convert the Timestamp to a formatted string if needed
                com.google.firebase.Timestamp timestamp = documentSnapshot.getTimestamp("Timestamp");
                if (timestamp != null) {
                    String reservationdate = timestamp.toDate().toString();
                    reservationDate.setText(reservationdate);
                }

            } else {
                // Document does not exist
                // Handle the case where the document does not exist
            }
        }).addOnFailureListener(e -> {
            // Error occurred while fetching the document
            // Handle the error
        });
    }

    private void confirmcancelreservation(String reservationType) {

        Dialog firstDialog = new Dialog(this);
        firstDialog.setContentView(R.layout.prompt_cancel_reservation);
        firstDialog.show();

        Button insideDialogBtn = firstDialog.findViewById(R.id.confirm);
        insideDialogBtn.setOnClickListener(View -> cancelreservation(reservationType));

    }

    private void cancelreservation(String reservationType) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get the reservation ID
        String reservationid = reservationnumber.getText().toString();

        // Get the check-in date and current date
        Date checkinDate = parseDateString(checkin.getText().toString());
        Date currentDate = new Date();

        // Calculate the difference in milliseconds between the check-in date and current date
        long timeDiffInMillis = checkinDate.getTime() - currentDate.getTime();

        // Calculate the difference in days
        long daysDiff = timeDiffInMillis / (1000 * 60 * 60 * 24);

        // Check if the cancellation is applicable (3 days before check-in)
        if (daysDiff >= 3) {
            // Update the status field to "Cancelled" in the database
            db.collection(reservationType)
                    .document(reservationid)
                    .update("status", "Cancelled")
                    .addOnSuccessListener(aVoid -> {
                        // If the update is successful, show a success message
                        Toast.makeText(this, "Reservation Cancelled", Toast.LENGTH_SHORT).show();

                        // Redirect the user to the check_reservation activity
                        Intent intent = new Intent(this, check_reservation.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        // If the update fails, show an error message
                        Toast.makeText(this, "Failed to cancel reservation: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {

            Dialog firstDialog = new Dialog(this);
            firstDialog.setContentView(R.layout.prompt_cancellation_denied);
            firstDialog.show();
        }
    }

    private Date parseDateString(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showavailableroomlist(){
        String hotelname = place.getText().toString();
        String roomtype = roomvalue.getText().toString();
        String reservationId = reservationnumber.getText().toString();
        Intent intent = new Intent(reservation_details.this, changeroomlist.class);
        intent.putExtra("roomname", roomtype);
        intent.putExtra("HotelName", hotelname);
        intent.putExtra("reservationId", reservationId);
        Toast.makeText(this, "Room Name" + roomtype, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "HotelName" + hotelname, Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

}