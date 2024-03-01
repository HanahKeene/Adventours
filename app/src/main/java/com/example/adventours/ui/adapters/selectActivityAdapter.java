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
import android.widget.CheckBox;
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

public class selectActivityAdapter extends RecyclerView.Adapter<selectActivityAdapter.ViewHolder> {

    private Context context;
    private List<selectActivityModel> selectActivityModelList;
    private List<selectActivityModel> checkedItems = new ArrayList<>();

    private OnActivityItemClickListener listener;

    public interface OnActivityItemClickListener {
        void onCheckboxClick(selectActivityModel item, boolean isChecked);
    }

    public selectActivityAdapter(Context context, List<selectActivityModel> selectActivityModelList, OnActivityItemClickListener listener) {
        this.context = context;
        this.selectActivityModelList = selectActivityModelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.select_activity_format, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        selectActivityModel item = selectActivityModelList.get(position);
        holder.checkbox.setText(item.getName());
        holder.checkbox.setChecked(item.isChecked());
        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setChecked(isChecked);
            listener.onCheckboxClick(item, isChecked);
            if (isChecked) {
                checkedItems.add(item);
            } else {
                checkedItems.remove(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectActivityModelList.size();
    }

    public List<selectActivityModel> getCheckedItems() {
        return checkedItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.activity);
        }
    }
}
