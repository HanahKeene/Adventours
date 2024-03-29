package com.example.adventours.ui;
import static androidx.fragment.app.FragmentManager.TAG;

import com.bumptech.glide.Glide;
import com.example.adventours.ui.BitmapUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class newitineraryplan extends AppCompatActivity {
    private static final String TAG = "New Itinerary";
    Button cover;

    TextInputEditText name, startdate, enddate;
    
    ImageButton startbtn, endbtn;
    Button create;

    Dialog loadingDialog;

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

        if (cover.getBackground() == null || TextUtils.isEmpty(name.getText()) || TextUtils.isEmpty(startdate.getText()) || TextUtils.isEmpty(enddate.getText())) {
                Toast.makeText(this, "Please fill in all fields and select a cover image for the itinerary", Toast.LENGTH_SHORT).show();
        } else {

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.prompt_loading_screen);
        loadingDialog.setCancelable(false);
        Window dialogWindow = loadingDialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        loadingDialog.show();

        ImageView loadingImageView = loadingDialog.findViewById(R.id.loading);
        Glide.with(this)
                .asGif()
                .load(R.drawable.loading)
                .into(loadingImageView);

        String itineraryName = name.getText().toString();
        String start = startdate.getText().toString();
        String end = enddate.getText().toString();



        LocalDate startDate = LocalDate.parse(start, DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.getDefault()));
        LocalDate endDate = LocalDate.parse(end, DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.getDefault()));

        // Calculate the number of days between the start and end dates
        long daysCount = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1; // Add 1 to include both start and end dates

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
            itineraryData.put("name", itineraryName);
            itineraryData.put("start", start);
            itineraryData.put("end", end);
            itineraryData.put("days", daysCount);

            for (int i = 1; i <= daysCount; i++) {

                Map<String, Object> dayData = new HashMap<>();
                LocalDate currentDate = startDate.plusDays(i - 1);
                String formattedDate = formatDateAsWords(currentDate);
                dayData.put("date", formattedDate.toString());


                // Add day document to days collection
                int finalI = i;
                itineraryRef.collection("days").document("Day " + i)
                        .set(dayData)
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "Day " + finalI + " created");

                            // Once the day document is successfully created, create the "activities" subcollection
                            createActivitiesSubcollection(itineraryRef.collection("days").document("Day " + finalI));
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to create day " + finalI, Toast.LENGTH_SHORT).show();
                        });
            }
            // Upload image to Firebase Storage and set the image URL in Firestore
            uploadImageToStorage(buttonBitmap, itineraryRef, itineraryData, itineraryName, start, end);
            }
        }
    }

    // Method to create "activities" subcollection within the specified day document
    private void createActivitiesSubcollection(DocumentReference dayDocumentRef) {
        dayDocumentRef.collection("activities")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e(TAG, "Error creating activities subcollection", error);
                        return;
                    }

                    Log.d(TAG, "Activities subcollection created");
                });
    }


    private String formatDateAsWords(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
        return date.format(formatter);
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
                            loadingDialog.dismiss();

                            Dialog successDialog = new Dialog(this);
                            successDialog.setContentView(R.layout.prompt_success);

                            successDialog.show();


                            new Handler().postDelayed(() -> {
                                        successDialog.dismiss();

                                Toast.makeText(newitineraryplan.this, "Itinerary created successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(this, MyIterinaryActivity.class);
                                startActivity(intent);
                                finish();
                            }, 1000);
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