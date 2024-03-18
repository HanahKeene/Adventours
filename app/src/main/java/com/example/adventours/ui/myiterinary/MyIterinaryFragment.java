package com.example.adventours.ui.myiterinary;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
        activitiesRecyclerView = root.findViewById(R.id.recyclerView);

        calendar = Calendar.getInstance();
        monthYearFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        updateCalendar();

        prev.setOnClickListener(View -> prevmonth());
        next.setOnClickListener(View -> nextMonth());
        displayCurrentMonthYearToast();

        calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedDate = (String) parent.getItemAtPosition(position);
                if (!clickedDate.isEmpty()) {
                    // Get the current month and year
                    int currentMonth = calendar.get(Calendar.MONTH);
                    int currentYear = calendar.get(Calendar.YEAR);

                    // Format the month using SimpleDateFormat
                    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
                    String monthName = monthFormat.format(calendar.getTime());

                    // Show a toast with the clicked month and date
                    Toast.makeText(getContext(), "Clicked Date: " + monthName + " " + clickedDate + ", " + currentYear, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    private void displayCurrentMonthYearToast() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        int displayedMonth = calendar.get(Calendar.MONTH);
        int displayedYear = calendar.get(Calendar.YEAR);
        String monthName = new DateFormatSymbols().getMonths()[displayedMonth];
        Toast.makeText(getContext(), monthName + " " + displayedYear, Toast.LENGTH_SHORT).show();
        String userID = currentUser.getUid(); // Replace with the actual userID
        fetchReservations(userID, monthName, displayedYear);
    }

    private void fetchReservations(String userID, String displayedMonth, int displayedYear) {
        // Define the Firestore query to fetch reservations for the displayed month and year
        db.collection("Hotel Reservation")
                .whereEqualTo("UserID", userID) // Add condition to filter by userID
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<MyItineraryCalendarModel> reservations = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Extract CheckIn and CheckOut dates from Firestore document
                            String checkInDate = document.getString("CheckIn");
                            String checkOutDate = document.getString("CheckOut");

                            // Extract month and year from CheckIn date string
                            String[] checkInParts = checkInDate.split(" ");
                            String checkInMonth = checkInParts[0]; // Month name
                            int checkInDay = Integer.parseInt(checkInParts[1].replace(",", "")); // Remove the comma
                            int checkInYear = Integer.parseInt(checkInParts[2]);

                            // Extract month and year from CheckOut date string
                            String[] checkOutParts = checkOutDate.split(" ");
                            String checkOutMonth = checkOutParts[0]; // Month name
                            int checkOutDay = Integer.parseInt(checkOutParts[1].replace(",", "")); // Remove the comma
                            int checkOutYear = Integer.parseInt(checkOutParts[2]);


                            // Check if the reservation belongs to the displayed month and year
                            if ((checkInMonth.equalsIgnoreCase(displayedMonth) && checkInYear == displayedYear)
                                    || (checkOutMonth.equalsIgnoreCase(displayedMonth) && checkOutYear == displayedYear)) {
                                // Convert Firestore document to Reservation object
                                MyItineraryCalendarModel reservation = document.toObject(MyItineraryCalendarModel.class);
                                reservations.add(reservation);
                            }
                        }
                        // Update RecyclerView with fetched reservations
                        updateRecyclerView(reservations);
                    } else {
                        // Handle errors
                        Toast.makeText(getContext(), "Failed to fetch reservations", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateRecyclerView(List<MyItineraryCalendarModel> reservations) {
        MyItineraryCalendarAdapter adapter = new MyItineraryCalendarAdapter(reservations);
        activitiesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        activitiesRecyclerView.setAdapter(adapter);
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

    private void nextMonth() {
        calendar.add(Calendar.MONTH, 1);
        updateCalendar();
    }

    private void prevmonth() {
        calendar.add(Calendar.MONTH, -1);
        updateCalendar();
    }
}
