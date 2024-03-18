package com.example.adventours.ui.myiterinary;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventours.databinding.FragmentMyiterinaryBinding;
import com.example.adventours.ui.adapters.CustomCalendarAdapter;
import com.example.adventours.ui.adapters.MyItineraryCalendarAdapter;
import com.example.adventours.ui.models.MyItineraryCalendarModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.protobuf.Timestamp;
import com.example.adventours.R;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MyIterinaryFragment extends Fragment {

    private FragmentMyiterinaryBinding binding;
    private SharedPreferences sharedPreferences;

    private RecyclerView activitiesRecyclerView;
    private FirebaseFirestore db;

    private GridView calendarGridView;
    private CustomCalendarAdapter calendarAdapter;
    private Calendar calendar;
    private Button prev, next;
    private TextView monthYearTextView;
    private SimpleDateFormat monthYearFormat;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyiterinaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedPreferences = getActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        db = FirebaseFirestore.getInstance();

        calendarGridView = root.findViewById(R.id.calendarGridView);
        prev = root.findViewById(R.id.prevButton);
        next = root.findViewById(R.id.nextButton);
        monthYearTextView = root.findViewById(R.id.monthYearTextView);

        calendar = Calendar.getInstance();
        monthYearFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        updateCalendar();

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                updateCalendar();

                // Extract and toast the displayed month
                Calendar displayedCalendar = Calendar.getInstance();
                displayedCalendar.setTime(calendar.getTime());
                int displayedMonth = displayedCalendar.get(Calendar.MONTH);
                String monthName = new DateFormatSymbols().getMonths()[displayedMonth];
                Toast.makeText(getContext(), monthName, Toast.LENGTH_SHORT).show();
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                updateCalendar();

                // Extract and toast the displayed month
                Calendar displayedCalendar = Calendar.getInstance();
                displayedCalendar.setTime(calendar.getTime());
                int displayedMonth = displayedCalendar.get(Calendar.MONTH);
                String monthName = new DateFormatSymbols().getMonths()[displayedMonth];
                Toast.makeText(getContext(), monthName, Toast.LENGTH_SHORT).show();
            }
        });


        return root;
    }

    private void updateCalendar() {
        ArrayList<String> days = new ArrayList<>();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int startDay = calendar.get(Calendar.DAY_OF_WEEK);

        for (int i = 1; i < startDay; i++) {
            days.add("");
        }

        for (int i = 1; i <= maxDay; i++) {
            days.add(String.valueOf(i));
        }

        monthYearTextView.setText(monthYearFormat.format(calendar.getTime()));

        calendarAdapter = new CustomCalendarAdapter(getContext(), days);
        calendarGridView.setAdapter(calendarAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
