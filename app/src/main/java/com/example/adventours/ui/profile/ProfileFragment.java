package com.example.adventours.ui.profile;

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
    private Button button;
    private TextView textView;

    LinearLayout myitinerarybtn, settingsbtn, helpcenterbtn, aboutusbtn, rateusbtn;

    ImageButton editprofile;


    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        auth = FirebaseAuth.getInstance();
        button = root.findViewById(R.id.button3);
        textView = root.findViewById(R.id.user_fullname);
        myitinerarybtn = root.findViewById(R.id.myitinerarybtn);
        settingsbtn = root.findViewById(R.id.settingsbtn);
        helpcenterbtn = root.findViewById(R.id.helpcenterbtn);
        aboutusbtn = root.findViewById(R.id.aboutusbtn);
        rateusbtn = root.findViewById(R.id.rateusbtn);
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
                Intent intent = new Intent(getActivity(), rate_us.class);
                startActivity(intent);
            }
        });


        helpcenterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HelpCenterActivity.class);
                startActivity(intent);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
//                getActivity().finish();
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




