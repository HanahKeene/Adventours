package com.example.adventours;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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

public class RestauConfirmationScreen extends AppCompatActivity {
    TextView back, restauname, name, address, contact, emailfld, guests, checkin, checkout, notefld, expiration, account_name, accountnumber, filename;
    Button submit, choosefile;

    ImageButton gcash, maya;
    SharedPreferences sharedPreferences;

    private static final int IMAGE_PICK_CODE = 100;
    private Uri selectedImageUri;
    String mop;

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
        gcash = findViewById(R.id.gcash);
        maya = findViewById(R.id.maya);
        account_name = findViewById(R.id.account_name);
        accountnumber = findViewById(R.id.account_number);
        choosefile = findViewById(R.id.choosefile);
        filename = findViewById(R.id.filename);
        submit = findViewById(R.id.submit);

        back.setOnClickListener(v -> finish());
        choosefile.setOnClickListener(v -> openGallery());

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

        gcash.setOnClickListener(v -> enablegcash(restauid));
        maya.setOnClickListener(v -> enablemaya(restauid));

        guests.setText(adult + " Adults and " + child + " child");
        checkin.setText(start);
        checkout.setText(end);
        notefld.setText(note);
        expiration.setText(expirationDateString);

        enablegcash(restauid);

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
                        contact.setText(number);
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

            DocumentReference restauRef = db.collection("Restaurants").document(restauid);

            restauRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
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

            submit.setOnClickListener(v -> checkreceipt());
        }
    }

    private void checkreceipt() {

        if (selectedImageUri == null) {
            Toast.makeText(RestauConfirmationScreen.this, "Please select a receipt image", Toast.LENGTH_SHORT).show();
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
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        Map<String, Object> reservationData = new HashMap<>();
        String status = "Pending Approval";
        reservationData.put("status", status);
        reservationData.put("reservationId", reservationId);
        reservationData.put("UserID", userId.toString());
        reservationData.put("CustomerName", name.getText().toString());
        reservationData.put("Address", address.getText().toString());
        reservationData.put("Number", contact.getText().toString());
        reservationData.put("Email", emailfld.getText().toString());
        reservationData.put("RestaurantName", restauname.getText().toString());
        reservationData.put("CheckIn", checkin.getText().toString());
        reservationData.put("CheckOut", checkout.getText().toString());
        reservationData.put("Guests", guests.getText().toString());
        reservationData.put("Expiration", expiration.getText().toString());
        reservationData.put("Timestamp", FieldValue.serverTimestamp());
        reservationData.put("MOP", mop);

        // Upload image to Firebase Storage and get the download URL
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
                    Toast.makeText(RestauConfirmationScreen.this, "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }).addOnFailureListener(e -> {
                // Handle failure to upload image
                Toast.makeText(RestauConfirmationScreen.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            // No image selected, add reservation data without image URL
            addReservationDataToFirestore(reservationId, reservationData);
        }
    }

    private void addReservationDataToFirestore(String reservationId, Map<String, Object> reservationData) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Restaurant Reservation").document(reservationId).set(reservationData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RestauConfirmationScreen.this, "Reservation successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RestauConfirmationScreen.this, RestaurantReservationReceipt.class);
                    String restaurantId = intent.getStringExtra("RestaurantId");
                    String timestamp = FieldValue.serverTimestamp().toString();
                    intent.putExtra("Restaurant ID", restaurantId);
                    intent.putExtra("ReservationNumber", reservationId);
                    intent.putExtra("CustomerName", name.getText().toString());
                    intent.putExtra("Address", address.getText().toString());
                    intent.putExtra("Number", contact.getText().toString());
                    intent.putExtra("Email", emailfld.getText().toString());
                    intent.putExtra("Guests", guests.getText().toString());
                    intent.putExtra("RestaurantName", restauname.getText().toString());
                    intent.putExtra("CheckIn", checkin.getText().toString());
                    intent.putExtra("CheckOut", checkout.getText().toString());
                    intent.putExtra("Expiration", expiration.getText().toString());
                    intent.putExtra("TimeStamp", timestamp);
                    intent.putExtra("MOP", mop);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Toast.makeText(RestauConfirmationScreen.this, "Error adding reservation: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void enablemaya(String restauid) {
        gcash.setBackgroundResource(R.drawable.buttonwborder);
        maya.setBackgroundResource(R.drawable.button_cyan);
        mop = "Maya";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Restaurants").document(restauid);

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
        DocumentReference userRef = db.collection("Restaurants").document(restauid);

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
