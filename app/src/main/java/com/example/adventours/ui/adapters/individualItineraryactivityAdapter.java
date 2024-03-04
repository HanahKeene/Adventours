package com.example.adventours.ui.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adventours.R;
import com.example.adventours.ui.models.individualitineraryactivityModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class individualItineraryactivityAdapter extends RecyclerView.Adapter<individualItineraryactivityAdapter.ViewHolder>{


    private Context context;
    private List<individualitineraryactivityModel> individualitineraryactivityModelList;

    public individualItineraryactivityAdapter(Context context, List<individualitineraryactivityModel> activities) {
        this.context = context;
        this.individualitineraryactivityModelList = activities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_plan_activities, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        individualitineraryactivityModel activityModel = individualitineraryactivityModelList.get(position);
        holder.activity.setText(activityModel.getName());
        holder.place.setText(activityModel.getPlace());
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog(activityModel);
            }

            private void showConfirmationDialog(individualitineraryactivityModel activityModel) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm Removal");
                builder.setMessage("Are you sure you want to remove this activity?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove the activity from Firestore
                        removeActivityFromFirestore(activityModel);
                    }
                });
                builder.setNegativeButton("No", null);
                builder.create().show();
            }

            private void removeActivityFromFirestore(individualitineraryactivityModel activityModel) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get the current user ID

                db.collection("users").document(userId)
                        .collection("itineraries").document(activityModel.getItineraryId())
                        .collection("days").document(activityModel.getDayId())
                        .collection("activities").document(activityModel.getDocumentId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Activity removed successfully", Toast.LENGTH_SHORT).show();
                                // Remove the activity from the list and notify the adapter
                                individualitineraryactivityModelList.remove(activityModel);
                                notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Failed to remove activity: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        loadPlaceImage(activityModel.getPlace(), holder.imgplace);
    }

    private void loadPlaceImage(String placeName, ImageView imageView) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Load image from Tourist Spots collection using placeName
        db.collection("Tourist Spots").whereEqualTo("name", placeName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Retrieve the image URL from Firestore and load it into the ImageView using a library like Glide or Picasso
                        String imageUrl = queryDocumentSnapshots.getDocuments().get(0).getString("img_url");
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            // Use Glide or Picasso to load the image into the ImageView
                            Glide.with(context)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.placeholder) // Placeholder image while loading
                                    .error(R.drawable.error_image) // Error image if loading fails
                                    .into(imageView);
                        } else {
                            // If image URL is not available, try loading image from Hotels collection
                            loadHotelImage(placeName, imageView);
                        }
                    } else {
                        // If place is not found in Tourist Spots collection, try loading image from Hotels collection
                        loadHotelImage(placeName, imageView);
                    }
                })
                .addOnFailureListener(e -> {
                    // Failed to fetch image from Tourist Spots collection, try loading image from Hotels collection
                    loadHotelImage(placeName, imageView);
                });
    }

    private void loadHotelImage(String placeName, ImageView imageView) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Load image from Hotels collection using placeName
        db.collection("Hotels").whereEqualTo("name", placeName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Retrieve the image URL from Firestore and load it into the ImageView using a library like Glide or Picasso
                        String imageUrl = queryDocumentSnapshots.getDocuments().get(0).getString("img_url");
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            // Use Glide or Picasso to load the image into the ImageView
                            Glide.with(context)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.placeholder) // Placeholder image while loading
                                    .error(R.drawable.error_image) // Error image if loading fails
                                    .into(imageView);
                        } else {
                            // If image URL is not available, try loading image from Restaurants collection
                            loadRestaurantImage(placeName, imageView);
                        }
                    } else {
                        // If place is not found in Hotels collection, try loading image from Restaurants collection
                        loadRestaurantImage(placeName, imageView);
                    }
                })
                .addOnFailureListener(e -> {
                    // Failed to fetch image from Hotels collection, try loading image from Restaurants collection
                    loadRestaurantImage(placeName, imageView);
                });
    }

    private void loadRestaurantImage(String placeName, ImageView imageView) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Load image from Restaurants collection using placeName
        db.collection("Restaurants").whereEqualTo("name", placeName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Retrieve the image URL from Firestore and load it into the ImageView using a library like Glide or Picasso
                        String imageUrl = queryDocumentSnapshots.getDocuments().get(0).getString("img_url");
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            // Use Glide or Picasso to load the image into the ImageView
                            Glide.with(context)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.placeholder) // Placeholder image while loading
                                    .error(R.drawable.error_image) // Error image if loading fails
                                    .into(imageView);
                        } else {
                            // Handle case where image URL is not available
                            imageView.setImageResource(R.drawable.placeholder); // Set default image
                        }
                    } else {
                        // Handle case where place is not found in Restaurants collection
                        imageView.setImageResource(R.drawable.placeholder); // Set default image
                    }
                })
                .addOnFailureListener(e -> {
                    // Failed to fetch image from Restaurants collection
                    imageView.setImageResource(R.drawable.placeholder); // Set default image
                });
    }



    @Override
    public int getItemCount() {
        return individualitineraryactivityModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView remove, imgplace;
        TextView activity, place;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgplace = itemView.findViewById(R.id.imgplace);
            activity = itemView.findViewById(R.id.activity_name);
            place = itemView.findViewById(R.id.activity_place);
            remove = itemView.findViewById(R.id.removebtn);
        }
    }
}
