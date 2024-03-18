package com.example.adventours.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.adventours.R;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

public class CustomCalendarAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<String> days;
    private final LayoutInflater inflater;
    private final int currentDay;
    private OnDateClickListener mOnDateClickListener;

    public CustomCalendarAdapter(Context context, ArrayList<String> days) {
        this.context = context;
        this.days = days;
        inflater = LayoutInflater.from(context);
        currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public void setOnDateClickListener(OnDateClickListener listener) {
        mOnDateClickListener = listener;
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int position) {
        return days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.calendar_item, parent, false);
        }

        TextView dayTextView = convertView.findViewById(R.id.dayTextView);
        String day = days.get(position);
        dayTextView.setText(day);

        // Get the current day and month
        Calendar currentCalendar = Calendar.getInstance();
        int currentDayOfMonth = currentCalendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = currentCalendar.get(Calendar.MONTH);

        // Check if the current position represents the current day of the current month
        if (!day.isEmpty() && Integer.parseInt(day) == currentDayOfMonth &&
                currentCalendar.get(Calendar.MONTH) == currentMonth) {
            // Change the background color or apply other styling for the current day
            convertView.setBackground(ContextCompat.getDrawable(context, R.drawable.buttonwborder));
        } else {
            // Reset the background color or apply default styling for other days
            convertView.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        }

        // Set click listener for each date cell
        if (!day.isEmpty()) {
            final String dayString = day; // Capture the day value for click listener

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnDateClickListener != null) {
                        // Pass the clicked date to the listener
                        mOnDateClickListener.onDateClick(dayString);

                        // Extract and toast the displayed month
                        Calendar displayedCalendar = Calendar.getInstance();
                        int displayedMonth = displayedCalendar.get(Calendar.MONTH);
                        String monthName = new DateFormatSymbols().getMonths()[displayedMonth];

                        // Toast the month and clicked date
                        Toast.makeText(context, monthName + " " + dayString, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        return convertView;
    }


    public interface OnDateClickListener {
        void onDateClick(String date);
    }
}
