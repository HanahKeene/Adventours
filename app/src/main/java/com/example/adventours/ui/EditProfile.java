package com.example.adventours.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.drawable.Drawable;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.adventours.R;
import com.example.adventours.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditProfile extends AppCompatActivity {

    private Dialog loadingDialog;

    private FirebaseAuth auth;
    private FirebaseUser user;

    EditText fnametxtfld, lnametxtfld, unametxtfld, bdaytxtfld, citytxtfld, emailtxtfld, numbertxtfld;

    Button dp, updatebtn;

    private static final int IMAGE_PICK_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        dp = findViewById(R.id.dp);
        fnametxtfld = findViewById(R.id.fname);
        lnametxtfld = findViewById(R.id.lname);
        unametxtfld = findViewById(R.id.username);
        bdaytxtfld = findViewById(R.id.bday);
        citytxtfld = findViewById(R.id.city);
        emailtxtfld = findViewById(R.id.email);
        numbertxtfld = findViewById(R.id.number);
        updatebtn = findViewById(R.id.updatebtn);

        dp.setOnClickListener(view -> setdp());
        updatebtn.setOnClickListener(view -> updateprofile());

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        fetchUserDataAndUpdateUI();
    }

    private void loadingscreen() {
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_screen);

        ImageView loadingGif = loadingDialog.findViewById(R.id.loading);

        Glide.with(this)
                .load(R.drawable.loading) // Replace with your GIF resource
                .into(loadingGif);

        loadingDialog.show();
    }

    private void updateprofile() {
        loadingscreen();

        // Update user details
        String firstName = fnametxtfld.getText().toString();
        String lastName = lnametxtfld.getText().toString();
        String city = citytxtfld.getText().toString();
        String number = numbertxtfld.getText().toString().substring(4); // Remove the "+639" prefix
        String bday = bdaytxtfld.getText().toString();
        String email = emailtxtfld.getText().toString();
        String username = unametxtfld.getText().toString();

        // Create a new User object or update the existing user details
        User updatedUser = new User(firstName, lastName, city, number, bday, email, username);

        // Update the user details in Firestore
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("profile_images").child(user.getUid() + ".jpg");

        // Get the Bitmap from the ImageView
        Bitmap bitmap = ((BitmapDrawable) dp.getBackground()).getBitmap();

        // Convert the Bitmap to bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        // Upload the image to Firebase Storage
        UploadTask uploadTask = storageRef.putBytes(data);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Image uploaded successfully, get the download URL
            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();

                // Update the user details in Firestore including the image URL
                User updatedUserWithImage = new User(firstName, lastName, city, number, bday, email, username, imageUrl);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference userRef = db.collection("users").document(user.getUid());

                userRef.set(updatedUserWithImage)
                        .addOnSuccessListener(aVoid -> {
                            // Load and display the profile image using Glide
                            Glide.with(this).load(imageUrl).into(new CustomTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    // Set the loaded image as the background of the Button
                                    dp.setBackground(resource);

                                    // Hide loading screen
                                    loadingDialog.dismiss();

                                    // Display success message
                                    Toast.makeText(EditProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                                    // Delay for 5 seconds and then finish the activity
                                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                        // Your code to navigate or finish the activity
                                        finish();
                                    }, 5000);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                    // Handle the case where the resource is cleared
                                }
                            });
                        })
                        .addOnFailureListener(e -> {
                            // Hide loading screen
                            loadingDialog.dismiss();

                            // Display failure message
                            Toast.makeText(EditProfile.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        });
            });
        }).addOnFailureListener(e -> {
            // Hide loading screen
            loadingDialog.dismiss();

            // Display failure message
            Toast.makeText(EditProfile.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
        });
    }
    private void fetchUserDataAndUpdateUI() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(user.getUid());

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {

                String firstName = documentSnapshot.getString("firstName");
                String lastName = documentSnapshot.getString("lastName");
                String city = documentSnapshot.getString("city");
                String number = documentSnapshot.getString("phone");
                String bday = documentSnapshot.getString("birthday");
                String email = documentSnapshot.getString("email");
                String username = documentSnapshot.getString("username");

                fnametxtfld.setText(firstName);
                lnametxtfld.setText(lastName);
                unametxtfld.setText(username);
                citytxtfld.setText(city);
                numbertxtfld.setText("+639" + number);
                bdaytxtfld.setText(bday);
                emailtxtfld.setText(email);

                // Load profile image using Glide or any other image loading library
                // For example, if you have a field in User class for profile image URL
                // Glide.with(this).load(userData.getProfileImageUrl()).into(dp);
            } else {
                // User document does not exist, handle accordingly
            }
        }).addOnFailureListener(e -> {
            // Handle failure
        });
    }

    private void setdp() {
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
                dp.setBackground(new BitmapDrawable(getResources(), bitmap)); // Set as background
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
