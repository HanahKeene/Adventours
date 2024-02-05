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
import android.util.Log;
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

    private String firstName;
    private String lastName;
    private String city;
    private String number;
    private String bday;
    private String email;
    private String username;

    private String age;

    private String gender;

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

        String firstName = fnametxtfld.getText().toString();
        String lastName = lnametxtfld.getText().toString();
        String city = citytxtfld.getText().toString();
        String number = numbertxtfld.getText().toString().substring(4);
        String bday = bdaytxtfld.getText().toString();
        String email = emailtxtfld.getText().toString();
        String username = unametxtfld.getText().toString();
        String age = "";   // Get the age from the user input
        String gender = ""; // Get the gender from the user input

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
                // Update age and gender as needed

                // Update the document in Firestore with the modified user object
                userRef.set(existingUser)
                        .addOnSuccessListener(aVoid -> {
                            // Upload and set the image
                            uploadAndSetImage(existingUser);
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

    private void uploadAndSetImage(User existingUser) {

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
                                String imageUrl = uri.toString();
                                user.setImageUrl(imageUrl);

                                // Update the User document with the image URL
                                FirebaseFirestore.getInstance().collection("users")
                                        .document(user.getUid())
                                        .set(user, SetOptions.merge())
                                        .addOnSuccessListener(aVoid -> {
                                            loadingDialog.dismiss(); // Dismiss loading dialog
                                            Toast.makeText(EditProfile.this, "Image uploaded and profile updated", Toast.LENGTH_SHORT).show();

                                            // Update the UI with the latest data
                                            fetchUserDataAndUpdateUI();
                                        })
                                        .addOnFailureListener(e -> {
                                            loadingDialog.dismiss(); // Dismiss loading dialog
                                            Toast.makeText(EditProfile.this, "Failed to update profile with image", Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                loadingDialog.dismiss(); // Dismiss loading dialog
                                Toast.makeText(EditProfile.this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    loadingDialog.dismiss(); // Dismiss loading dialog
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
                String age = documentSnapshot.getString("age");

                fnametxtfld.setText(firstName);
                lnametxtfld.setText(lastName);
                unametxtfld.setText(username);
                citytxtfld.setText(city);
                numbertxtfld.setText("+639" + number);
                bdaytxtfld.setText(bday);
                emailtxtfld.setText(email);

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
