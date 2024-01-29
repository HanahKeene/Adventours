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

public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private Button logoutbtn;
    private TextView textView;

    TextView myitinerarybtn, settingsbtn, helpcenterbtn, aboutusbtn, rateusbtn;

    ImageButton editprofile;


    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        auth = FirebaseAuth.getInstance();
        logoutbtn = root.findViewById(R.id.logout);
        textView = root.findViewById(R.id.user_fullname);
        myitinerarybtn = root.findViewById(R.id.myitirinary);
        settingsbtn = root.findViewById(R.id.settings);
        helpcenterbtn = root.findViewById(R.id.helpcenter);
        aboutusbtn = root.findViewById(R.id.aboutus);
        rateusbtn = root.findViewById(R.id.rateus);
        editprofile = root.findViewById(R.id.displaypicture);

        user = auth.getCurrentUser();

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}




