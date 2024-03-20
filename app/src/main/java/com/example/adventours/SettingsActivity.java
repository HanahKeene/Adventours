    package com.example.adventours;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.app.AppCompatDelegate;

    import android.app.AlertDialog;
    import android.app.Dialog;
    import android.content.Context;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.Window;
    import android.view.WindowManager;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.Switch;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.bumptech.glide.Glide;
    import com.example.adventours.ui.EnableBiometrics;
    import com.google.android.gms.tasks.OnFailureListener;
    import com.google.android.gms.tasks.OnSuccessListener;
    import com.google.firebase.auth.AuthCredential;
    import com.google.firebase.auth.EmailAuthProvider;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.auth.UserInfo;
    import com.google.firebase.firestore.FirebaseFirestore;

    public class SettingsActivity extends AppCompatActivity {

        private FirebaseAuth mAuth;
        Switch switcher;
        boolean nightMode;
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        private Dialog loadingDialog, firstDialog, authenticationDialog;

        TextView back, biometrics, push_notif, delete_account;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_settings);

            mAuth = FirebaseAuth.getInstance();

            back = findViewById(R.id.back);
    //        biometrics = findViewById(R.id.biometricsbtn);
    //        push_notif = findViewById(R.id.pushnotifbtn);
            delete_account = findViewById(R.id.deleteaccntbtn);
            switcher = findViewById(R.id.switch1);

            sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
            nightMode = sharedPreferences.getBoolean("night", false);

            if (nightMode)
            {
                switcher.setChecked(true);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }

            switcher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (nightMode) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        editor = sharedPreferences.edit();
                        editor.putBoolean("night", false);
                    }else{
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        editor = sharedPreferences.edit();
                        editor.putBoolean("night", true);
                    }
                    editor.apply();
                }
            });

            back.setOnClickListener(View -> finish());
    //        biometrics.setOnClickListener(View -> openbiomtrics());
    //        push_notif.setOnClickListener(View -> openpushnotif());
            delete_account.setOnClickListener(View -> deleteAccount());

        }

        private void deleteAccount() {
            firstDialog = new Dialog(this);
            firstDialog.setContentView(R.layout.prompt_delete_account);
            firstDialog.show();

            Button insideDialogBtn = firstDialog.findViewById(R.id.delete);
            Button insideDeleteDialog = firstDialog.findViewById(R.id.cancel_delete);

            insideDialogBtn.setOnClickListener(view -> {
                checkForExistingReservations();
            });

            insideDeleteDialog.setOnClickListener(view -> firstDialog.dismiss());
        }

        private void checkForExistingReservations() {

            firstDialog.dismiss();

            Log.d("Reservations", "Looking for reservations.");

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


            FirebaseFirestore db =  FirebaseFirestore.getInstance();
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String userId = currentUser.getUid();

            db.collection("Hotel Reservation")
                    .whereEqualTo("UserID", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                loadingDialog.dismiss();
                                showReservationWarning();
                                Log.d("Reservations", "Hotel reservations found");
                            } else {
                                checkOtherReservations();
                                Log.d("Reservations", "No hotel reservations found");
                            }
                        } else {
                            Log.e("Reservations", "Error fetching hotel reservations: " + task.getException().getMessage());
                        }
                    });

        }

        private void checkOtherReservations() {

            FirebaseFirestore db =  FirebaseFirestore.getInstance();
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String userId = currentUser.getUid();

            db.collection("Restaurant Reservation")
                    .whereEqualTo("UserID", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                loadingDialog.dismiss();
                                showReservationWarning();
                                Log.d("Reservations", "Restaurant reservations found");
                            } else {
                                loadingDialog.dismiss();
                                showReauthenticationDialog();
                                Log.d("Reservations", "No restaurant reservations found");
                            }
                        } else {
                            // Handle query error
                        }
                    });
        }

        private void showReauthenticationDialog() {

            authenticationDialog = new Dialog(this);
            authenticationDialog.setContentView(R.layout.prompt_reauthenticate);
            authenticationDialog.show();

            EditText editTextEmail = authenticationDialog.findViewById(R.id.email);
            EditText editTextPassword = authenticationDialog.findViewById(R.id.password);
            Button buttonConfirm = authenticationDialog.findViewById(R.id.confirm);

            buttonConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = editTextEmail.getText().toString().trim();
                    String password = editTextPassword.getText().toString().trim();

                    if (!email.isEmpty() && !password.isEmpty()) {
                        authenticationDialog.dismiss();
                        loadingDialog.show();
                        reauthenticateAndDelete(email, password);
                    } else {
                        Toast.makeText(SettingsActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                    }
                }

                private void reauthenticateAndDelete(String email, String password) {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null) {
                        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                        currentUser.reauthenticate(credential)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        deleteUserData();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        // Reauthentication failed, show error message
                                        Toast.makeText(SettingsActivity.this, "Reauthentication failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });

        }
        private void deleteUserData() {
            showReauthenticationDialog();
            FirebaseUser currentUser = mAuth.getCurrentUser(); if (currentUser != null) {
                String userid = currentUser.getUid();

                // Delete the user's account
                currentUser.delete()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Account deleted successfully
                                deleteUserDataFromFirestore(userid);
                            } else {
                                // Handle account deletion failure
                                Exception exception = task.getException();
                                if (exception instanceof FirebaseAuthRecentLoginRequiredException) {
                                    showReauthenticationDialog();
                                } else {
                                    // Other errors, show a generic error message
                                    Toast.makeText(SettingsActivity.this, "Failed to delete account: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }

        private void deleteUserDataFromFirestore(String userid) {

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users").document(userid)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        loadingDialog.dismiss();
                        navigateToSignInScreen();
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure to delete user document
                        Toast.makeText(SettingsActivity.this, "Failed to delete user data", Toast.LENGTH_SHORT).show();
                    });


        }


        private void navigateToSignInScreen() {
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }



        private void showReservationWarning() {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.prompt_you_have_active_reservation);
            dialog.show();
        }
    }