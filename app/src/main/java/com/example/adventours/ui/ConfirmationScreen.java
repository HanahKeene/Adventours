package com.example.adventours.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adventours.HotelReservationReceipt;
import com.example.adventours.R;
import com.example.adventours.RestauConfirmationScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ConfirmationScreen extends AppCompatActivity {

    private FirebaseFirestore db;
    TextView roomtypeName, checkin, checkout, name, add, numbertxtfld, emailtxtfld, hotelnametxtfld, qtytxtfld, expirationtxtfld, reserveNumberm, total, dppayment, account_name, accountnumber, filename;
    ImageButton gcash, maya;
    Button submit, choosefile;
    SharedPreferences sharedPreferences;
    private static final int IMAGE_PICK_CODE = 100;
    private Uri selectedImageUri;
    String mop;
    Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_screen);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);

        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


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
        String userid = intent.getStringExtra("UserID");


        roomtypeName = findViewById(R.id.roomtypeTextView);
        checkin = findViewById(R.id.checkin);
        checkout = findViewById(R.id.checkout);
        name = findViewById(R.id.name);
        add = findViewById(R.id.add);
        numbertxtfld = findViewById(R.id.number);
        emailtxtfld = findViewById(R.id.email);
        hotelnametxtfld = findViewById(R.id.hotelname);
        qtytxtfld = findViewById(R.id.qty);
        expirationtxtfld = findViewById(R.id.expiration);
        total = findViewById(R.id.totalprice);
        dppayment = findViewById(R.id.dpprice);
        submit = findViewById(R.id.submit);

        qtytxtfld.setText(roomquantity + " Room/s");
        roomtypeName.setText(roomtype);
        checkin.setText(check_in);
        checkout.setText(check_out);
        expirationtxtfld.setText(expirationDateString);

        gcash = findViewById(R.id.gcash);
        maya = findViewById(R.id.maya);
        account_name = findViewById(R.id.account_name);
        accountnumber = findViewById(R.id.account_number);
        choosefile = findViewById(R.id.choosefile);
        filename = findViewById(R.id.filename);

        gcash.setOnClickListener(v -> enablegcash(hotelid));
        maya.setOnClickListener(v -> enablemaya(hotelid));
        submit.setOnClickListener(View ->  checkreceipt());
        choosefile.setOnClickListener(View -> openGallery());

        enablegcash(hotelid);

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
                        String city = document.getString("city");
                        String number = document.getString("phone");
                        String email = document.getString("email");
                        // Add more fields as needed

                        // Update UI with user details
                        name.setText(firstName + " " + lastName);
                        add.setText(city);
                        numbertxtfld.setText("+639" + number);
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
        }
    }

    private void checkreceipt() {

        if (selectedImageUri == null) {
            Toast.makeText(ConfirmationScreen.this, "Please select a receipt image", Toast.LENGTH_SHORT).show();
        } else {
            generateReservationIdAndAddToFirestore();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null) {
            selectedImageUri = data.getData();
            String fileName = getFileName(selectedImageUri);
            filename.setText(fileName);
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        if (displayNameIndex != -1) {
                            result = cursor.getString(displayNameIndex);
                        }
                    }
                } finally {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void generateReservationIdAndAddToFirestore() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reservationsRef = db.collection("Hotel Reservation");

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
                        Toast.makeText(ConfirmationScreen.this, "Error getting reservation IDs: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void addReservationToFirestore(String reservationId) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        Map<String, Object> reservationData = new HashMap<>();
        String status = "Pending Approval";
        reservationData.put("status", status);
        reservationData.put("reservationId", reservationId);
        reservationData.put("CustomerName", name.getText().toString());
        reservationData.put("Address", add.getText().toString());
        reservationData.put("Number", numbertxtfld.getText().toString());
        reservationData.put("Email", emailtxtfld.getText().toString());
        reservationData.put("HotelName", hotelnametxtfld.getText().toString());
        reservationData.put("RoomName", roomtypeName.getText().toString());
        reservationData.put("Quantity", qtytxtfld.getText().toString());
        reservationData.put("CheckIn", checkin.getText().toString());
        reservationData.put("CheckOut", checkout.getText().toString());
        reservationData.put("Expiration", expirationtxtfld.getText().toString());
        reservationData.put("Timestamp", FieldValue.serverTimestamp());
        reservationData.put("UserID", userId.toString());

        if (selectedImageUri != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("receipts").child(reservationId);
            UploadTask uploadTask = storageRef.putFile(selectedImageUri);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Image uploaded successfully, get the download URL
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    // Add the download URL to the reservation data
                    reservationData.put("receipt", imageUrl);
                    // Add the reservation to Firestore
                    addReservationDataToFirestore(reservationId, reservationData);
                }).addOnFailureListener(e -> {
                    // Handle failure to get download URL
                    Toast.makeText(ConfirmationScreen.this, "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }).addOnFailureListener(e -> {
                // Handle failure to upload image
                Toast.makeText(ConfirmationScreen.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            // No image selected, add reservation data without image URL
            addReservationDataToFirestore(reservationId, reservationData);
        }
    }

    private void addReservationDataToFirestore(String reservationId, Map<String, Object> reservationData) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Hotel Reservation").document(reservationId).set(reservationData)
                .addOnSuccessListener(aVoid -> {
                        Intent intent = new Intent(ConfirmationScreen.this, HotelReservationReceipt.class);
                        Intent intent1 = getIntent();
                        String hotelid = intent1.getStringExtra("HotelId");
                        String timestamp = FieldValue.serverTimestamp().toString();
                        intent.putExtra("Hotel ID", hotelid);
                        intent.putExtra("ReservationNumber", reservationId);
                        intent.putExtra("CustomerName", name.getText().toString());
                        intent.putExtra("Address", add.getText().toString());
                        intent.putExtra("Number", numbertxtfld.getText().toString());
                        intent.putExtra("Email", emailtxtfld.getText().toString());
                        intent.putExtra("HotelName", hotelnametxtfld.getText().toString());
                        intent.putExtra("RoomName", roomtypeName.getText().toString());
                        intent.putExtra("CheckIn", checkin.getText().toString());
                        intent.putExtra("CheckOut", checkout.getText().toString());
                        intent.putExtra("Expiration", expirationtxtfld.getText().toString());
                        intent.putExtra("Timestamp", timestamp);
                        intent.putExtra("MOP", mop);
                        startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    errorDialog();
//                    Toast.makeText(ConfirmationScreen.this, "Error adding reservation: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });


    }
    private void errorDialog() {

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.prompt_reservation_error);

        loadingDialog.show();
    }

//    private void getRoomPriceFromFirebase(String hotelid, String roomid, String roomquantity) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference roomRef = db.collection("Hotels").document(hotelid)
//                .collection("Rooms").document(roomid);
//
//        roomRef.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                DocumentSnapshot document = task.getResult();
//                if (document.exists()) {
//                    double roomPrice = document.getDouble("price");
//                    double totalCost = roomPrice * Integer.parseInt(qtytxtfld.toString());
//
//                    // Set the total cost in the TextView
//                    total.setText(String.valueOf(totalCost));
//
//                    double downpaymentAmount = 0.2 * totalCost;
//                    dppayment.setText(String.valueOf(downpaymentAmount));
//                } else {
//                    // Document does not exist
//                    Toast.makeText(ConfirmationScreen.this, "Room document does not exist", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                // Error getting document
//                Toast.makeText(ConfirmationScreen.this, "Error getting room document: " + task.getException(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void enablemaya(String restauid) {
        gcash.setBackgroundResource(R.drawable.buttonwborder);
        maya.setBackgroundResource(R.drawable.button_cyan);
        mop = "Maya";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Hotels").document(restauid);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String AccountName = document.getString("maya_account_name");
                    String AccountNumber = document.getString("maya_account_number");

                    account_name.setText(AccountName);
                    accountnumber.setText(AccountNumber);
                } else {
                    // Document does not exist
                    Toast.makeText(this, "User document does not exist", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Error getting document
                Toast.makeText(this, "Error getting user document: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void enablegcash(String restauid) {
        maya.setBackgroundResource(R.drawable.buttonwborder);
        gcash.setBackgroundResource(R.drawable.button_cyan);
        mop = "GCash";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Hotels").document(restauid);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String AccountName = document.getString("gcash_account_name");
                    String AccountNumber = document.getString("gcash_account_number");
                    account_name.setText(AccountName);
                    accountnumber.setText(AccountNumber);
                } else {
                    // Document does not exist
                    Toast.makeText(this, "User document does not exist", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Error getting document
                Toast.makeText(this, "Error getting user document: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}