    package com.example.adventours.ui.adapters;

    import android.content.Context;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.adventours.R;
    import com.example.adventours.ui.models.individualitineraryModel;

    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    public class individualitineraryAdapter extends RecyclerView.Adapter<individualitineraryAdapter.ViewHolder> {

        private Context context;
        private List<individualitineraryModel> individualitineraryModellist;
        private Map<String, individualItineraryactivityAdapter> dayAdapters;

        public individualitineraryAdapter(Context context, List<individualitineraryModel> individualitineraryModellist) {
            this.context = context;
            this.individualitineraryModellist = individualitineraryModellist;
            this.dayAdapters = new HashMap<>();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_reservation_format_day, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            individualitineraryModel currentItem = individualitineraryModellist.get(position);
            holder.name.setText(currentItem.getId());
            holder.date.setText(currentItem.getDate());

            List<String> activities = currentItem.getActivities();
            if (activities != null && !activities.isEmpty()) {
                // If there are activities, set up the adapter and display them
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                holder.activities.setLayoutManager(layoutManager);
                individualItineraryactivityAdapter itineraryactivityAdapter = new individualItineraryactivityAdapter(context, activities);
                holder.activities.setAdapter(itineraryactivityAdapter);
                dayAdapters.put(currentItem.getId(), itineraryactivityAdapter);
                holder.activities.setVisibility(View.VISIBLE); // Show the RecyclerView
                holder.noActivities.setVisibility(View.GONE); // Hide the TextView for no activities
                Log.d("RecyclerView", "Activities for day " + currentItem.getId() + ": " + activities.size());
            } else {
                // If there are no activities, display a message
                holder.activities.setAdapter(null);
                holder.activities.setVisibility(View.GONE); // Hide the RecyclerView
                holder.noActivities.setVisibility(View.VISIBLE); // Show the TextView for no activities
                Log.d("RecyclerView", "No activities for day " + currentItem.getId());
            }
        }


        @Override
        public int getItemCount() {
            return individualitineraryModellist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView name, date, noActivities;
            RecyclerView activities;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.day);
                date = itemView.findViewById(R.id.date);
                activities = itemView.findViewById(R.id.activitiesrecyclerview);
                noActivities = itemView.findViewById(R.id.no_activities);
            }
        }

        // Method to get the adapter for a specific day
        public individualItineraryactivityAdapter getAdapterForDay(String dayId) {
            return dayAdapters.get(dayId);
        }
    }

