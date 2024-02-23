    package com.example.adventours.ui.adapters;
    import static androidx.fragment.app.FragmentManager.TAG;

    import android.app.Activity;
    import android.app.AlertDialog;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.bumptech.glide.Glide;
    import com.bumptech.glide.request.RequestOptions;
    import com.bumptech.glide.request.target.Target;
    import com.example.adventours.R;
    import com.example.adventours.ui.itineraryplan;
    import com.example.adventours.ui.models.FYPModel;
    import com.example.adventours.ui.models.ItineraryModel;
    import com.example.adventours.ui.models.individualitineraryModel;
    import com.example.adventours.ui.models.selectDayModel;
    import com.example.adventours.ui.selectDay;
    import com.google.android.gms.tasks.OnFailureListener;
    import com.google.android.gms.tasks.OnSuccessListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.firestore.DocumentReference;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.firestore.QueryDocumentSnapshot;
    import com.google.firebase.firestore.QuerySnapshot;

    import java.util.ArrayList;
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

            // Set up the nested RecyclerView for activities
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            holder.activities.setLayoutManager(layoutManager);

            List<String> activities = currentItem.getActivities();
            if (activities != null) {
                individualItineraryactivityAdapter activityAdapter = new individualItineraryactivityAdapter(context, activities);
                holder.activities.setAdapter(activityAdapter);

                // Save the adapter in the map for later access
                dayAdapters.put(currentItem.getId(), activityAdapter);
            } else {
                holder.activities.setAdapter(null);
            }
        }

        @Override
        public int getItemCount() {
            return individualitineraryModellist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView name, date;
            RecyclerView activities;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.day);
                date = itemView.findViewById(R.id.date);
                activities = itemView.findViewById(R.id.activitiesrecyclerview);
            }
        }

        // Method to get the adapter for a specific day
        public individualItineraryactivityAdapter getAdapterForDay(String dayId) {
            return dayAdapters.get(dayId);
        }
    }
