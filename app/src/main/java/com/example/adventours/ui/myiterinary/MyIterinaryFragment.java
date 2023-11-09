package com.example.adventours.ui.myiterinary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.adventours.databinding.FragmentNotifBinding;
import com.example.adventours.databinding.FragmentMyiterinaryBinding;

public class MyIterinaryFragment extends Fragment {

    private FragmentMyiterinaryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyIterinaryViewModel MyIterinaryViewModel =
                new ViewModelProvider(this).get( MyIterinaryViewModel .class);

        binding = FragmentMyiterinaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}