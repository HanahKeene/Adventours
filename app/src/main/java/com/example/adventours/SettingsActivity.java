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
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Switch;
    import android.widget.TextView;
    import android.widget.Toast;

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
            Dialog firstDialog = new Dialog(this);
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


            FirebaseFirestore db =  FirebaseFirestore.getInstance();
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String userId = currentUser.getUid();

            db.collection("Hotel Reservation")
                    .whereEqualTo("UserID", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                showReservationWarning();
                            } else {
                                checkOtherReservations();
                            }
                        } else {

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
                                // User has other reservations, display warning message
                                showReservationWarning();
                            } else {

                                showReauthenticationDialog();
                            }
                        } else {
                            // Handle query error
                        }
                    });
        }

        private void showReauthenticationDialog() {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialogView = LayoutInflater.from(this).inflate(R.layout.prompt_reauthenticate, null);
            builder.setView(dialogView);

            EditText editTextEmail = dialogView.findViewById(R.id.email);
            EditText editTextPassword = dialogView.findViewById(R.id.password);
            Button buttonConfirm = dialogView.findViewById(R.id.confirm);

            AlertDialog alertDialog = builder.create();

            buttonConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = editTextEmail.getText().toString().trim();
                    String password = editTextPassword.getText().toString().trim();

                    if (!email.isEmpty() && !password.isEmpty()) {
                        reauthenticateAndDelete(email, password);
                        alertDialog.dismiss();
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
                                        deleteUserDataFromFirestore(currentUser);
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


        private void navigateToSignInScreen() {
            Intent intent = new Intent(SettingsActivity.this, SigninActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear all previous activities
            startActivity(intent);
            finish();
        }

        private void deleteUserDataFromFirestore(String userid) {

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users").document(userid)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // User document deleted successfully
                        Toast.makeText(SettingsActivity.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                        // Navigate back to sign-in screen
                        startActivity(new Intent(SettingsActivity.this, SigninActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure to delete user document
                        Toast.makeText(SettingsActivity.this, "Failed to delete user data", Toast.LENGTH_SHORT).show();
                    });


        }


        private void showReservationWarning() {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.prompt_you_have_active_reservation);
            dialog.show();
        }
    }