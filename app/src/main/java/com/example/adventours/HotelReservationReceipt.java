package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adventours.ui.ConfirmationScreen;
import com.example.adventours.ui.RoomDetails;
import com.example.adventours.ui.home.HomeFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.OutputStream;

public class
HotelReservationReceipt extends AppCompatActivity {

    TextView namefield, addfield, numberfield, emailfield, hotelnamefield, reserveIdNumbertxtfld, roomtypefield, numberofPeoplefield, checkinfield, checkoutfield, expirationfield;

    Button backtohomepage, saveas;

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_reservation_receipt);

        namefield = findViewById(R.id.name);
        addfield = findViewById(R.id.add);
        numberfield = findViewById(R.id.number);
        emailfield = findViewById(R.id.email);

        hotelnamefield = findViewById(R.id.hotelname);
        reserveIdNumbertxtfld = findViewById(R.id.reservationNumber);
        roomtypefield = findViewById(R.id.roomtypeTextView);
        checkinfield = findViewById(R.id.checkinfield);
        checkoutfield = findViewById(R.id.checkoutfield);
        expirationfield = findViewById(R.id.expiration);
        saveas = findViewById(R.id.saveasimage);


        backtohomepage = findViewById(R.id.home);


//        generateAndDisplayReservationNumber();

        Intent intent = getIntent();
        Intent intent1 = getIntent();

        String reservationID = intent.getStringExtra("ReservatioNumber");

        String hotelId = intent1.getStringExtra("HotelId");
        String roomId = intent1.getStringExtra("RoomId");

        String name = intent.getStringExtra("CustomerName");
        String add = intent.getStringExtra("Address");
        String num = intent.getStringExtra("Number");
        String email = intent.getStringExtra("Email");

//        intent.putExtra("No.ofRooms", qtytxtfld.getText().toString());
//        intent.putExtra("Downpayment", dppayment.getText().toString());
//        intent.putExtra("Total Cost", total.getText().toString());
        String hotelname = intent.getStringExtra("HotelName");
        String roomname = intent.getStringExtra("RoomName");
        String checkin = intent.getStringExtra("CheckIn");
        String checkout = intent.getStringExtra("CheckOut");
        String expiration = intent.getStringExtra("Expiration");

        namefield.setText(name);
        addfield.setText(add);
        numberfield.setText(num);
        emailfield.setText(email);

        hotelnamefield.setText(hotelname);
        roomtypefield.setText(roomname);
        numberofPeoplefield.setText(roomname);
        checkinfield.setText(checkin);
        checkoutfield.setText(checkout);
        expirationfield.setText(expiration);

        reserveIdNumbertxtfld.setText(reservationID);

        Toast.makeText(HotelReservationReceipt.this, "Hotel Id:" + hotelId + "Room Id" + roomId , Toast.LENGTH_SHORT).show();

        backtohomepage.setOnClickListener(View -> backtohomepage());
        saveas.setOnClickListener(View -> saveasimage());

    }

    private void saveasimage() {
        // Get the root view of the layout
        View rootView = getWindow().getDecorView().getRootView();

        // Create a Bitmap of the layout
        rootView.setDrawingCacheEnabled(true);
        Bitmap screenshotBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);

        // Save the Bitmap as an image
        try {
            // Define the file path and name
            String fileName = "reservation_receipt.png";

            // Create a ContentValues object to store image metadata
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

            // Get the content resolver
            ContentResolver resolver = getContentResolver();

            // Insert the image into the MediaStore database
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            // Open an output stream to write the image data
            OutputStream outputStream = resolver.openOutputStream(imageUri);

            // Compress the Bitmap and write it to the output stream
            screenshotBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            // Close the output stream
            outputStream.close();

            // Notify the user
            Toast.makeText(this, "Receipt saved in gallery", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
        }
    }

    private void backtohomepage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

//    private void generateAndDisplayReservationNumber() {
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db = FirebaseFirestore.getInstance();
//
//        // Reference to the "Hotel Reservation" collection
//        CollectionReference reservationCollectionRef = db.collection("Hotel Reservations");
//
//        // Placeholder for the reservation number
//        String reservationNumber = null;
//
//        // Loop to find the first available reservation number
//        for (int i = 1; i <= 1000000000; i++) {
//            // Format the count as a reservation number (e.g., 0000000001)
//            reservationNumber = String.format("%010d", i);
//
//            // Check if the reservation number already exists
//            DocumentReference reservationRef = reservationCollectionRef.document(reservationNumber);
//
//            boolean reservationExists = checkReservationExists(reservationRef);
//
//            if (!reservationExists) {
//                // Reservation number does not exist, break out of the loop
//                break;
//            }
//        }
//
//        // Display the reservation number
//        reserveIdNumbertxtfld.setText(reservationNumber);
//    }

//    private boolean checkReservationExists(DocumentReference reservationRef) {
//        // Check if the reservation document exists
//        try {
//            DocumentSnapshot document = reservationRef.get().getResult();
//            return document.exists();
//        } catch (Exception e) {
//            // Handle exceptions
//            return false;
//        }
//    }


}