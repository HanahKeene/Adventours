package com.example.adventours.ui;
import com.example.adventours.ui.BitmapUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adventours.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class newitineraryplan extends AppCompatActivity {

    Button cover;

    TextInputEditText name, startdate, enddate;
    
    ImageButton startbtn, endbtn;
    Button create;

    private static final int IMAGE_PICK_CODE = 100; // You can choose any unique integer value


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newitineraryplan);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        cover = findViewById(R.id.edit_plan_cover);
        name = findViewById(R.id.itineraryName);
        startdate = findViewById(R.id.startdate);
        enddate = findViewById(R.id.enddate);
        startbtn = findViewById(R.id.startdatebtn);
        endbtn = findViewById(R.id.enddatebtn);
        create = findViewById(R.id.createbtn);

        cover.setOnClickListener(View -> openGallery());
        startbtn.setOnClickListener(View -> opencalendarstart());
        endbtn.setOnClickListener(View -> opencalendarend());
        create.setOnClickListener(View -> createitinerary());

    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                cover.setBackground(new BitmapDrawable(getResources(), bitmap)); // Set as background
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void opencalendarstart() {
        showDatePicker(startdate);
    }

    private void opencalendarend() {
        showDatePicker(enddate);
    }

    private void showDatePicker(final TextView dateTextView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Format the date as words (e.g., "June 12, 2023")
                String formattedDate = formatDateAsWords(year, month, day);

                // Set the formatted date to the TextView
                dateTextView.setText(formattedDate);
            }
            private String formatDateAsWords(int year, int month, int day) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                // Create a SimpleDateFormat with the desired format
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());

                // Format the date and return the result
                return sdf.format(calendar.getTime());
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private DocumentReference getUserProfileReference() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            return FirebaseFirestore.getInstance().collection("users").document(userId);
        }
        return null;
    }

    private void createitinerary() {
        String itineraryname = name.getText().toString();
        String start = startdate.getText().toString();
        String end = enddate.getText().toString();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference userProfileRef = getUserProfileReference();

        if (currentUser != null && userProfileRef != null) {
            // Create a unique document ID for the itinerary
            String itineraryId = userProfileRef.collection("itineraries").document().getId();

            // Create a new document in the "itineraries" subcollection
            DocumentReference itineraryRef = userProfileRef.collection("itineraries").document(itineraryId);

            // Convert Button's background to Bitmap using the utility method
            BitmapDrawable buttonBackgroundDrawable = (BitmapDrawable) cover.getBackground();
            Bitmap buttonBitmap = buttonBackgroundDrawable.getBitmap();

            // Create a data object to store the information
            Map<String, Object> itineraryData = new HashMap<>();
            itineraryData.put("name", itineraryname);
            itineraryData.put("start", start);
            itineraryData.put("end", end);

            // Upload image to Firebase Storage and set the image URL in Firestore
            uploadImageToStorage(buttonBitmap, itineraryRef, itineraryData, itineraryname, start, end);
        }
    }

    private void uploadImageToStorage(Bitmap bitmap, DocumentReference itineraryRef, Map<String, Object> itineraryData, String name, String start, String end) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a reference to the image location in Firebase Storage
        StorageReference imageRef = storageRef.child("itinerary_images").child(itineraryRef.getId() + ".png");

        // Convert Bitmap to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        // Upload image to Firebase Storage
        UploadTask uploadTask = imageRef.putBytes(data);

        // Continue with the task to get the download URL
        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            // Continue with the task to get the download URL
            return imageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Image uploaded successfully, get the download URL
                Uri downloadUri = task.getResult();
                String imageUrl = downloadUri.toString();

                // Store the download URL in Firestore
                itineraryData.put("image", imageUrl);

                // Store the data in the Firestore document
                itineraryRef.set(itineraryData)
                        .addOnSuccessListener(aVoid -> {
                            // Successfully stored the itinerary data
                            Toast.makeText(newitineraryplan.this, "Itinerary created successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, MyIterinaryActivity.class);
                            startActivity(intent);
                        })
                        .addOnFailureListener(e -> {
                            // Handle failure
                            Toast.makeText(newitineraryplan.this, "Failed to create itinerary", Toast.LENGTH_SHORT).show();
                        });
            } else {
                // Handle unsuccessful image upload
                Toast.makeText(newitineraryplan.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });
    }


}