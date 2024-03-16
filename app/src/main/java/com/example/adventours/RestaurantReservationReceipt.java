package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class RestaurantReservationReceipt extends AppCompatActivity {

    TextView timestampfld, namefld, addressfld, contactfld, emailfld, restaunamefld, reservenumfld, guestfld, checkinfld, checkoutfld, notefld, expirationfld, MOP, totalcostfld;

    Button home, saveasimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_reservation_receipt);

//        timestampfld = findViewById(R.id.timestamp);
        namefld = findViewById(R.id.customername);
        addressfld = findViewById(R.id.address);
        contactfld = findViewById(R.id.contact);
        emailfld = findViewById(R.id.email);
        restaunamefld = findViewById(R.id.restauname);
        reservenumfld = findViewById(R.id.reservation);
        guestfld = findViewById(R.id.numguest);
        checkinfld = findViewById(R.id.checkin);
        checkoutfld = findViewById(R.id.checkout);
        notefld = findViewById(R.id.note);
        totalcostfld = findViewById(R.id.totalcost);
        expirationfld = findViewById(R.id.expiration);
        home = findViewById(R.id.home);
        saveasimage = findViewById(R.id.saveasimage);
        MOP = findViewById(R.id.mop);
        
        Intent intent = getIntent();
        
//        String timestamp = intent.getStringExtra("TimeStamp");
        String name = intent.getStringExtra("CustomerName");
        String add = intent.getStringExtra("Address");
        String contact = intent.getStringExtra("Number");
        String email = intent.getStringExtra("Email");
        String restauname = intent.getStringExtra("RestaurantName");
        String reservenumber = intent.getStringExtra("ReservationNumber");
        String checkin = intent.getStringExtra("CheckIn");
        String checkout = intent.getStringExtra("CheckOut");
        String guests = intent.getStringExtra("Guests");
        String note = intent.getStringExtra("Note");
        String totalcost = intent.getStringExtra("TotalCost");
        String expiration = intent.getStringExtra("Expiration");
        String mop = intent.getStringExtra("MOP");

        namefld.setText(name);
        addressfld.setText(add);
        contactfld.setText(contact);
        emailfld.setText(email);
        restaunamefld.setText(restauname);
        reservenumfld.setText(reservenumber);
        checkinfld.setText(checkin);
        checkoutfld.setText(checkout);
        guestfld.setText(guests);
        expirationfld.setText(expiration);
        notefld.setText(note);
        totalcostfld.setText(totalcost);
        MOP.setText(mop);

        
        home.setOnClickListener(View -> backHome());
        saveasimage.setOnClickListener(View -> saveas());

//        retrieveTimestamp(reservenumber);
    }

//    private void retrieveTimestamp(String reservenumber) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference reservationRef = db.collection("Restaurant Reservation").document(reservenumber);
//
//        reservationRef.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                DocumentSnapshot document = task.getResult();
//                if (document.exists()) {
//                    // DocumentSnapshot contains the data
//                    Timestamp timestamp = document.getTimestamp("Timestamp");
//                    if (timestamp != null) {
//                        // Convert the timestamp to a formatted string
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//                        String formattedTimestamp = dateFormat.format(timestamp.toDate());
//
//                        // Update the UI with the formatted timestamp
//                        timestampfld.setText(formattedTimestamp);
//                    } else {
//                        // Timestamp is null
//                        Toast.makeText(this, "Timestamp is null", Toast.LENGTH_SHORT).show();
//                        Log.d("DocumentData", document.getData().toString());
//                    }
//                } else {
//                    // Document does not exist
//                    Toast.makeText(this, "Document does not exist", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                // Error getting document
//                Toast.makeText(this, "Error getting document: " + task.getException(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }



    private void saveas() {
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


    private void backHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    
    
}