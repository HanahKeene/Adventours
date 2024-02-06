package com.example.adventours.ui.myiterinary;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.adventours.databinding.FragmentNotifBinding;
import com.example.adventours.databinding.FragmentMyiterinaryBinding;

public class MyIterinaryFragment extends Fragment {

    private FragmentMyiterinaryBinding binding;
    SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyIterinaryViewModel MyIterinaryViewModel =
                new ViewModelProvider(this).get( MyIterinaryViewModel .class);

        binding = FragmentMyiterinaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sharedPreferences = getActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}