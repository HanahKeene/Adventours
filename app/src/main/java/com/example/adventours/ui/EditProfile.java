package com.example.adventours.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditProfile extends AppCompatActivity {

    private Dialog loadingDialog;
    private FirebaseAuth auth;
    private FirebaseUser user;

    EditText fnametxtfld, lnametxtfld, unametxtfld, bdaytxtfld, citytxtfld, emailtxtfld, numbertxtfld;

    Button dp, updatebtn;

    private static final int IMAGE_PICK_CODE = 100;

    private DocumentReference userRef;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


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

        if (user != null) {
            // If the user is not null, get the UID
            String uid = user.getUid();
            userRef = FirebaseFirestore.getInstance().collection("users").document(uid);
        } else {
            // Handle the case where the user is null (not signed in)
            // You may want to redirect the user to the sign-in screen or take appropriate action.
        }
        fetchUserDataAndUpdateUI();
    }

    private void loadingscreen() {
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.prompt_loading_screen);

        ImageView loadingGif = loadingDialog.findViewById(R.id.loading);

        Glide.with(this)
                .load(R.drawable.loading) // Replace with your GIF resource
                .into(loadingGif);

        loadingDialog.show();
    }

    private void updateprofile() {
        loadingscreen();

        String firstName = fnametxtfld.getText().toString();
        String lastName = lnametxtfld.getText().toString();
        String city = citytxtfld.getText().toString();
        String number = numbertxtfld.getText().toString().substring(4);
        String bday = bdaytxtfld.getText().toString();
        String email = emailtxtfld.getText().toString();
        String username = unametxtfld.getText().toString();

        // Fetch the existing user data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(user.getUid());

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Create a User object with the existing data
                User existingUser = documentSnapshot.toObject(User.class);

                // Update only the non-null fields
                if (firstName != null) existingUser.setFirstName(firstName);
                if (lastName != null) existingUser.setLastName(lastName);
                if (city != null) existingUser.setCity(city);
                if (number != null) existingUser.setPhone(number);
                if (bday != null) existingUser.setBirthday(bday);
                if (email != null) existingUser.setEmail(email);
                if (username != null) existingUser.setUsername(username);

                // Update the document in Firestore with the modified user object
                userRef.set(existingUser)
                        .addOnSuccessListener(aVoid -> {
                            // Upload and set the image
                            uploadAndSetImage(existingUser);
                            loadingDialog.dismiss();
                        })
                        .addOnFailureListener(e -> {
                            loadingDialog.dismiss(); // Dismiss loading dialog
                            Toast.makeText(EditProfile.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        });
            } else {
                loadingDialog.dismiss(); // Dismiss loading dialog
                Toast.makeText(EditProfile.this, "User document does not exist", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            loadingDialog.dismiss(); // Dismiss loading dialog
            Toast.makeText(EditProfile.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
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

                fnametxtfld.setText(firstName);
                lnametxtfld.setText(lastName);
                citytxtfld.setText(city);
                numbertxtfld.setText(number);
                bdaytxtfld.setText(bday);
                emailtxtfld.setText(email);

                // Load and display the image
                loadAndDisplayImage(documentSnapshot.getString("imageUrl"));
            } else {
                // User document does not exist, handle accordingly
            }
        }).addOnFailureListener(e -> {
            // Handle failure
        });
    }

    private void uploadAndSetImage(User user) {
        // Get the image from the dp button
        BitmapDrawable bitmapDrawable = (BitmapDrawable) dp.getBackground();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        // Convert the image to bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        // Set the image URL in the User object
        String imagePath = "images/" + user.getUsername() + "_dp.jpg"; // Adjust the path as needed
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(imagePath);

        // Upload the image
        storageReference.putBytes(data)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL
                    storageReference.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                String newImageUrl = uri.toString();

                                // Update the 'imageUrl' field with the new URL
                                user.setImageUrl(newImageUrl);

                                // Update the User document with the new image URL
                                userRef.set(user)
                                        .addOnSuccessListener(aVoid -> {
                                            loadingDialog.dismiss(); // Dismiss loading dialog
                                            Toast.makeText(EditProfile.this, "Updated Successfully", Toast.LENGTH_SHORT).show();

                                            // Reload and display the new image
                                            loadAndDisplayImage(newImageUrl);
                                        })
                                        .addOnFailureListener(e -> {
                                            loadingDialog.dismiss(); // Dismiss loading dialog
                                            Toast.makeText(EditProfile.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                loadingDialog.dismiss(); // Dismiss loading dialog
                                Toast.makeText(EditProfile.this, "Failed to get new image URL", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    loadingDialog.dismiss(); // Dismiss loading dialog
                    Toast.makeText(EditProfile.this, "Failed to upload new image", Toast.LENGTH_SHORT).show();
                });
    }


    private void setdp() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    private void loadAndDisplayImage(String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .circleCrop()
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        // Convert the Drawable to a BitmapDrawable
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), ((BitmapDrawable) resource).getBitmap());

                        // Set the BitmapDrawable as the background of the Button
                        dp.setBackground(bitmapDrawable);

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Handle cleanup if needed
                    }
                });
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
