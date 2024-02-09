package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

public class RestaurantReservationReceipt extends AppCompatActivity {

    TextView timestampfld, namefld, addressfld, contactfld, emailfld, restaunamefld, reservenumfld, guestfld, checkinfld, checkoutfld, notefld, expirationfld;

    Button home, saveasimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_reservation_receipt);

        timestampfld = findViewById(R.id.timestamp);
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
        expirationfld = findViewById(R.id.expiration);
        home = findViewById(R.id.home);
        saveasimage = findViewById(R.id.saveasimage);
        
        Intent intent = getIntent();
        
        String timestamp = intent.getStringExtra("Timestamp");
        String name = intent.getStringExtra("CustomerName");
        String add = intent.getStringExtra("Address");
        String contact = intent.getStringExtra("Number");
        String email = intent.getStringExtra("Email");
        String restauname = intent.getStringExtra("RestaurantName");
        String reservenumber = intent.getStringExtra("ReservationNumber");
        String checkin = intent.getStringExtra("CheckIn");
        String checkout = intent.getStringExtra("CheckOut");
        String guests = intent.getStringExtra("Guests");
        String expiration = intent.getStringExtra("Expiration");
        
        timestampfld.setText(timestamp);
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
        
        home.setOnClickListener(View -> backHome());
        saveasimage.setOnClickListener(View -> saveas());

    }

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
            String filePath = getExternalFilesDir(null) + "/" + fileName;

            // Create a file output stream
            FileOutputStream outputStream = new FileOutputStream(filePath);

            // Compress the Bitmap and save it
            screenshotBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            // Notify the user
            Toast.makeText(this, "Receipt saved as image", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
        }
    }


    private void backHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    
    
}