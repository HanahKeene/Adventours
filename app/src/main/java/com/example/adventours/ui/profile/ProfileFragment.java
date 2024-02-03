package com.example.adventours.ui.profile;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

    TextView myitinerarybtn, settingsbtn, helpcenterbtn, aboutusbtn, rateusbtn;

    ImageButton editprofile;


    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        logoutbtn = root.findViewById(R.id.logout);
        textView = root.findViewById(R.id.user_fullname);
        usernumber = root.findViewById(R.id.usernumber);
        myitinerarybtn = root.findViewById(R.id.myitirinary);
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
        } else {
            textView.setText(user.getEmail());

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

                Dialog firstDialog = new Dialog(getActivity());
                firstDialog.setContentView(R.layout.activity_rate_us);
                firstDialog.show();

                Button insideDialogBtn = firstDialog.findViewById(R.id.submitrate);
                insideDialogBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        firstDialog.dismiss();
                        Dialog secondDialog = new Dialog(getActivity());
                        secondDialog.setContentView(R.layout.prompt_thanksfortherate);
                        secondDialog.show();

                        Button welcomebtn = secondDialog.findViewById(R.id.welcomebtn);
                        welcomebtn.setOnClickListener(View -> secondDialog.dismiss());
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

                Dialog firstDialog = new Dialog(getActivity());
                firstDialog.setContentView(R.layout.prompt_logoutconfirmation);
                firstDialog.show();

                Button insideDialogBtn = firstDialog.findViewById(R.id.logoutbtn);
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

                textView.setText(firstName + " " + lastName);
                usernumber.setText("+639"+number);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}




