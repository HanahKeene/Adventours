package com.example.adventours.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.example.adventours.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.io.IOException;

public class EditProfile extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;

    EditText fnametxtfld, lnametxtfld, unametxtfld, bdaytxtfld, citytxtfld, emailtxtfld, numbertxtfld;

    Button dp, updatebtn;

    private static final int IMAGE_PICK_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        dp = findViewById(R.id.dp);
        fnametxtfld = findViewById(R.id.fname);
        lnametxtfld = findViewById(R.id.lname);
        unametxtfld = findViewById(R.id.username);
        bdaytxtfld = findViewById(R.id.bday);
        citytxtfld = findViewById(R.id.city);
        emailtxtfld = findViewById(R.id.email);
        numbertxtfld = findViewById(R.id.number);
        updatebtn = findViewById(R.id.updatebtn);

        dp.setOnClickListener(View -> setdp());
        updatebtn.setOnClickListener(View -> loadingscreen());

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        fetchUserDataAndUpdateUI();


    }

    private void fetchUserDataAndUpdateUI() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(user.getUid());

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {

                String firstName = documentSnapshot.getString("firstName");
                String lastName = documentSnapshot.getString("lastName");
                String city= documentSnapshot.getString("city");
                String number = documentSnapshot.getString("phone");
                String bday = documentSnapshot.getString("birthday");
                String email = documentSnapshot.getString("email");
                String username = documentSnapshot.getString("username");

                fnametxtfld.setText(firstName);
                lnametxtfld.setText(lastName);
                unametxtfld.setText(username);
                citytxtfld.setText(city);
                numbertxtfld.setText("+639"+number);
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

    private void loadingscreen() {
        Dialog loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_screen);

        ImageView loadingGif = loadingDialog.findViewById(R.id.loading);


        Glide.with(this)
                .load(R.drawable.loading) // Replace with your GIF resource
                .into(loadingGif);

        loadingDialog.show();
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