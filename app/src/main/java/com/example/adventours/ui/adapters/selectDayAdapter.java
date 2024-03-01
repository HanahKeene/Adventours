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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.adventours.R;
import com.example.adventours.select_activity;
import com.example.adventours.ui.itineraryplan;
import com.example.adventours.ui.models.FYPModel;
import com.example.adventours.ui.models.ItineraryModel;
import com.example.adventours.ui.models.selectActivityModel;
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

public class selectDayAdapter extends RecyclerView.Adapter<selectDayAdapter.ViewHolder> {

    private OnDayItemClickListener onDayItemClickListener;
    private Context context;
    private List<selectDayModel> selectDayModelList;

    private String spot_id;

    private String itinerary_id;

    public interface OnDayItemClickListener {
        void onDayItemClickListener(String Id);
    }

    public selectDayAdapter(Context context, List<selectDayModel> selectDayModelList, String itinerary_id, String spot_id, OnDayItemClickListener listener) {
        this.context = context;
        this.selectDayModelList = selectDayModelList;
        this.itinerary_id = itinerary_id;
        this.spot_id = spot_id;
        this.onDayItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.select_day_format, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(selectDayModelList.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return selectDayModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.daycount);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String id = selectDayModelList.get(position).getId();
                        if (id != null) {
                            Intent intent = new Intent(context, select_activity.class);
                            intent.putExtra("SpotId", itinerary_id);
                            context.startActivity(intent);
//                            showConfirmationDialog(id);
                        } else {
                            // If the item ID is null, log an error message
                            Log.e("FYPAdapter", "Item ID is null");
                        }
                    } else {
                        // If the item position is equal to RecyclerView.NO_POSITION, log an error message
                        Log.e("FYPAdapter", "Item position is equal to RecyclerView.NO_POSITION");
                    }
                }

                private void showConfirmationDialog(final String dayId) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure you want to select this day?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // If user confirms, open selectDay activity
                            addSpotIdToActivities(dayId);
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // If user cancels, dismiss the dialog
                            dialog.dismiss();
                        }
                    });

                    builder.show();
                }

                private void addSpotIdToActivities(final String dayId) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String user = currentUser.getUid();

                    // Check for duplicate spot IDs in all days
                    Task<QuerySnapshot> queryTask = db.collection("users").document(user).collection("itineraries").document(spot_id)
                            .collection("days").whereEqualTo("spot_id", itinerary_id).get();

                    queryTask.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.isEmpty()) {
                                // No duplicate spot ID found, proceed to add the spot ID to the selected day
                                addSpotIdToSelectedDay(dayId);
                            } else {
                                // Duplicate spot ID found, show dialog to select another day
                                showDuplicateSpotIdDialog(dayId);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failed to check spot ID", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                private void showDuplicateSpotIdDialog(final String currentDayId) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Spot ID Exists");
                    builder.setMessage("The spot ID already exists in one of the days. Choose another day to add the spot ID.");

                    // Get the list of days excluding the current day
                    final List<String> otherDays = getOtherDaysExceptCurrent(currentDayId);

                    builder.setItems(otherDays.toArray(new String[0]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String selectedDay = otherDays.get(which);
                            addSpotIdToSelectedDay(selectedDay);
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.show();
                }

                private List<String> getOtherDaysExceptCurrent(String currentDayId) {
                    List<String> otherDays = new ArrayList<>();
                    for (selectDayModel dayModel : selectDayModelList) {
                        String dayId = dayModel.getId();
                        if (!dayId.equals(currentDayId)) {
                            otherDays.add(dayId);
                        }
                    }
                    return otherDays;
                }

                private void addSpotIdToSelectedDay(final String selectedDayId) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String user = currentUser.getUid();

                    Map<String, Object> data = new HashMap<>();
                    data.put("activity_id", itinerary_id);

                    db.collection("users").document(user).collection("itineraries").document(spot_id)
                            .collection("days").document(selectedDayId).collection("activities")
                            .add(data)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(context, "Spot ID added to activities of day " + selectedDayId, Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Failed to add spot ID to activities", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        }
    }
}
