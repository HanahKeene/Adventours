package com.example.adventours;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.adventours.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve the FCM token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String token = task.getResult();
                        Log.d("FCM Token", token);
                        // Now you have the FCM token, you can send it to your server or use it as needed
                    } else {
                        Log.e("FCM Token", "Failed to get token");
                    }
                });

        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Listen for changes in the first collection where status field is 'pending'
        db.collection("Hotel Reservation")
                .whereEqualTo("status", "pending")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.e(TAG, "Listen failed for first collection: " + e);
                        return;
                    }

                    if (snapshot != null && !snapshot.isEmpty()) {
                        for (DocumentSnapshot doc : snapshot.getDocuments()) {
                            Log.d(TAG, "Document data: " + doc.getData());
                            // Trigger notification here for the first collection
                            sendNotification("Data updated", "Document in first collection has been updated.");
                        }
                    } else {
                        Log.d(TAG, "First collection: No documents with 'pending' status");
                    }
                });

        db.collection("Restaurant Reservation")
                .whereEqualTo("status", "approved")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.e(TAG, "Listen failed for second collection: " + e);
                        return;
                    }

                    if (snapshot != null && !snapshot.isEmpty()) {
                        for (DocumentSnapshot doc : snapshot.getDocuments()) {
                            Log.d(TAG, "Document data: " + doc.getData());
                            // Trigger notification here for the second collection
                            sendNotification("Data updated", "Document in second collection has been updated.");
                        }
                    } else {
                        Log.d(TAG, "Second collection: No documents with 'approved' status");
                    }
                });


        BottomNavigationView navView = findViewById(R.id.nav_view);
        if (navView != null) {
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home, R.id.navigation_myiterinary, R.id.navigation_weather,
                    R.id.navigation_profile, R.id.navigation_notif)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            NavigationUI.setupWithNavController(navView, navController);
        } else {
            Log.e("MainActivity", "BottomNavigationView is null");
        }
    }

    private void sendNotification(String title, String messageBody) {
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "channel_id")
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setSmallIcon(R.drawable.bluelogo)
                        .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(0, notificationBuilder.build());
    }

}
