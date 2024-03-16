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

    TextView namefield, addfield, numberfield, emailfield, hotelnamefield, reserveIdNumbertxtfld, roomtypefield, checkinfield, checkoutfield, expirationfield, totalfield, downpaymentfield;

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
        downpaymentfield = findViewById(R.id.downpayment);
        totalfield = findViewById(R.id.totalcost);
        saveas = findViewById(R.id.saveasimage);
        backtohomepage = findViewById(R.id.home);

        Intent intent = getIntent();
        Intent intent1 = getIntent();

        String reservationID = intent.getStringExtra("ReservationNumber");

        String name = intent.getStringExtra("CustomerName");
        String add = intent.getStringExtra("Address");
        String num = intent.getStringExtra("Number");
        String email = intent.getStringExtra("Email");
        String downpayment = intent.getStringExtra("Downpayment");
        String totalcost = intent.getStringExtra("TotalCost");
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
        checkinfield.setText(checkin);
        checkoutfield.setText(checkout);
        expirationfield.setText(expiration);
        reserveIdNumbertxtfld.setText(reservationID);
        downpaymentfield.setText(downpayment);
        totalfield.setText(totalcost);


        backtohomepage.setOnClickListener(View -> home());
        saveas.setOnClickListener(View -> saveasimage());

    }

    private void saveasimage() {

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

    private void home() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}