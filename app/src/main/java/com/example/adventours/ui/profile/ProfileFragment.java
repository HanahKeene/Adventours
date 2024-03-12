package com.example.adventours.ui.profile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.adventours.HelpCenterActivity;
import com.example.adventours.R;

import com.example.adventours.LoginActivity;
import com.example.adventours.SettingsActivity;
import com.example.adventours.databinding.FragmentProfileBinding;
import com.example.adventours.interestform;
import com.example.adventours.musttry_activity;
import com.example.adventours.ui.EditProfile;
import com.example.adventours.ui.MyIterinaryActivity;
import com.example.adventours.ui.about_us;
import com.example.adventours.ui.check_reservation;
import com.example.adventours.ui.rate_us;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private Button logoutbtn;
    private TextView textView, usernumber;

    TextView myitinerarybtn, myreservationsbtn, settingsbtn, helpcenterbtn, aboutusbtn, rateusbtn;

    ImageButton editprofile;

    SharedPreferences sharedPreferences;

    private FragmentProfileBinding binding;

    private Dialog rateDialog;
    private Dialog logoutDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sharedPreferences = getActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);

        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


        logoutbtn = root.findViewById(R.id.logout);
        textView = root.findViewById(R.id.user_fullname);
        usernumber = root.findViewById(R.id.usernumber);
        myitinerarybtn = root.findViewById(R.id.myitirinary);
        myreservationsbtn = root.findViewById(R.id.myreservatons);
        settingsbtn = root.findViewById(R.id.settings);
        helpcenterbtn = root.findViewById(R.id.helpcenter);
        aboutusbtn = root.findViewById(R.id.aboutus);
        rateusbtn = root.findViewById(R.id.rateus);
        editprofile = root.findViewById(R.id.displaypicture);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        fetchUserDataAndUpdateUI();

        if (user == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
            }
        });

        myitinerarybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyIterinaryActivity.class);
                startActivity(intent);
            }
        });

        myreservationsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), check_reservation.class);
                startActivity(intent);
            }
        });

        settingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        aboutusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), about_us.class);
                startActivity(intent);
            }
        });

        rateusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateDialog = new Dialog(getActivity());
                rateDialog.setContentView(R.layout.activity_rate_us);
                rateDialog.show();

                Button insideDialogBtn = rateDialog.findViewById(R.id.submitrate);
                insideDialogBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rateDialog.dismiss();
                        // Your code for the second dialog
                    }
                });
            }
        });



        helpcenterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HelpCenterActivity.class);
                startActivity(intent);
            }
        });


        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog = new Dialog(getActivity());
                logoutDialog.setContentView(R.layout.prompt_logoutconfirmation);
                logoutDialog.show();

                Button insideDialogBtn = logoutDialog.findViewById(R.id.logoutbtn);
                insideDialogBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
            }
        });

        return root;
    }

    private void fetchUserDataAndUpdateUI() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userRef = db.collection("users").document(currentUser.getUid());

            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String number = documentSnapshot.getString("phone");
                    String firstName = documentSnapshot.getString("firstName");
                    String lastName = documentSnapshot.getString("lastName");
                    String imageUrl = documentSnapshot.getString("imageUrl");

                    // Set the username and number
                    textView.setText(firstName + " " + lastName);
                    usernumber.setText(number);

                    // Load and display the image using Glide
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(requireContext())
                                .load(imageUrl)
                                .circleCrop() // Optional: Crop the image into a circle
                                .into(editprofile);
                    } else {
                        // If imageUrl is null or empty, you can set a default image or handle it accordingly
                        // For example, you can set a default drawable:
                        editprofile.setImageResource(R.drawable.baseline_add_photo_alternate_24);
                    }

                } else {
                    // User document does not exist, handle accordingly
                }
            }).addOnFailureListener(e -> {
                // Handle failure
            });
        } else {
            // User is not signed in, handle this scenario accordingly
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rateDialog != null && rateDialog.isShowing()) {
            rateDialog.dismiss();
        }
        if (logoutDialog != null && logoutDialog.isShowing()) {
            logoutDialog.dismiss();
        }
    }
}




