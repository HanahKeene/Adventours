package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adventours.ui.ConfirmationScreen;
import com.example.adventours.ui.RoomDetails;
import com.example.adventours.ui.adapters.tourAdapter;
import com.example.adventours.ui.models.tourModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class toursinfo extends AppCompatActivity {

    ImageView cover;
    TextView tourname, accomodation, description, stay, pricefld;
    ImageButton in_btn, out_btn, roomnumdec, roomnumin;
    RecyclerView inclusionRecyclerView;
    Button addtoitinerary;
    ImageButton back;
    FirebaseFirestore db;
    TextView priceTxtView, paxTxtView, roomName, amenitiesTxtView, descTxtView, in_date, out_date, roomNumber;
    Button reservebtn, reservearoom;
    int currentRoomNumber = 1;
    final int MIN_ROOM_NUMBER = 1;
    final int MAX_ROOM_NUMBER = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toursinfo);

        cover = findViewById(R.id.tour_cover);
        tourname = findViewById(R.id.tourname);
        accomodation = findViewById(R.id.guestsaccomodation);
        pricefld = findViewById(R.id.price);
        description = findViewById(R.id.description);
        stay = findViewById(R.id.stay);
        addtoitinerary = findViewById(R.id.addtoitinerary);
        inclusionRecyclerView = findViewById(R.id.inclusions);

        back = findViewById(R.id.backbtn);

        back.setOnClickListener(View -> finish());
        addtoitinerary.setOnClickListener(View -> openDialog());

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String tour_id = intent.getStringExtra("tour_id");

        fetchtourDetails(tour_id);

    }

    private void openDialog() {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.reserve_details);

        in_date = findViewById(R.id.in_date);
        out_date = findViewById(R.id.out_date);
        in_btn = findViewById(R.id.in_btn);
        out_btn = findViewById(R.id.out_btn);
        roomnumin = findViewById(R.id.roomnumin);
        roomnumdec = findViewById(R.id.roomnumdec);
        reservearoom = findViewById(R.id.reservearoom);


        // Find ImageButtons within the dialog's layout
        ImageButton in_btn = dialog.findViewById(R.id.in_btn);
        ImageButton out_btn = dialog.findViewById(R.id.out_btn);
        ImageButton roomnumin = dialog.findViewById(R.id.roomnumin);
        ImageButton roomnumdec = dialog.findViewById(R.id.roomnumdec);

        TextView in_date = dialog.findViewById(R.id.in_date);
        TextView out_date = dialog.findViewById(R.id.out_date);
        TextView roomNumber = dialog.findViewById(R.id.room_num);

        Calendar currentDateTime = Calendar.getInstance();
        String currentDate = formatDateAsWords(currentDateTime.get(Calendar.YEAR), currentDateTime.get(Calendar.MONTH), currentDateTime.get(Calendar.DAY_OF_MONTH));
        in_date.setText(currentDate);
        out_date.setText(currentDate);

        Button reservearoom = dialog.findViewById(R.id.reservearoom);

        in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a custom popup window
                showDatePicker(in_date);
            }
        });

        out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a custom popup window
                showDatePicker(out_date);
            }
        });

        roomNumber.setText(String.valueOf(currentRoomNumber));

        // Decrease the room number when the decrement button is clicked
        roomnumdec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentRoomNumber > MIN_ROOM_NUMBER) {
                    currentRoomNumber--;
                    roomNumber.setText(String.valueOf(currentRoomNumber));
                }
            }
        });

        // Increase the room number when the increment button is clicked
        roomnumin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentRoomNumber < MAX_ROOM_NUMBER) {
                    currentRoomNumber++;
                    roomNumber.setText(String.valueOf(currentRoomNumber));
                }
            }
        });

        reservearoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(toursinfo.this, ConfirmationScreen.class);

                // Add any data you want to pass to the ConfirmationScreen activity
                // For example, you can use putExtra to pass room details
                Intent intent1 = getIntent();
                String hotelId = intent1.getStringExtra("HotelId");
                String roomId = intent1.getStringExtra("RoomId");

                intent.putExtra("HotelId", hotelId);
                intent.putExtra("Room ID", roomId);

                Toast.makeText(toursinfo.this, "Hotel ID: " + hotelId + " ROOM ID: " + roomId, Toast.LENGTH_SHORT).show();

                intent.putExtra("RoomQuantity", roomNumber.getText().toString());
                intent.putExtra("RoomName", roomName.getText().toString());
                intent.putExtra("Price", priceTxtView.getText().toString());
                intent.putExtra("CheckIn", in_date.getText().toString());
                intent.putExtra("CheckOut", out_date.getText().toString());

                // Add more data as needed

                startActivity(intent);

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if (currentUser != null) {
                    String userId = currentUser.getUid();

                    // Fetch user details from Firestore using the obtained userId
                    DocumentReference userRef = db.collection("Users").document(userId);

                    userRef.get().addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Retrieve user details
                            String firstName = documentSnapshot.getString("firstName");
                            String lastName = documentSnapshot.getString("lastName");
                            String city = documentSnapshot.getString("city");
                            String phone = documentSnapshot.getString("phone");

                            // Pass user details to the ConfirmationScreen activity
                            intent.putExtra("FirstName", firstName);
                            intent.putExtra("LastName", lastName);
                            intent.putExtra("City", city);
                            intent.putExtra("Phone", phone);
                            intent.putExtra("UserID", userId);


                            Toast.makeText(toursinfo.this, "User:" + userId, Toast.LENGTH_SHORT).show();
                            // Start the ConfirmationScreen activity
//                            startActivity(intent);
                        } else {
                            // Handle the case where the user document does not exist
                            Toast.makeText(toursinfo.this, "User details not found", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                        // Handle errors that occurred while fetching user details
                        Toast.makeText(toursinfo.this, "Error fetching user details", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    // Handle the case where the user is not authenticated
                    Toast.makeText(toursinfo.this, "User not authenticated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.gravity = Gravity.BOTTOM;

            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            layoutParams.windowAnimations = R.style.DialogAnimation;

            window.setAttributes(layoutParams);
        }

        dialog.show();
    }

    private void showDatePicker(TextView inDate) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Format the date as words (e.g., "June 12, 2023")
                String formattedDate = formatDateAsWords(year, month, day);

                // Set the formatted date to the TextView
                inDate.setText(formattedDate);
            }
        }, year, month, day);

        // Set the minimum date to the current date
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }

    private String formatDateAsWords(int year, int month, int day) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        // Create a SimpleDateFormat with the desired format
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());

        // Format the date and return the result
        return sdf.format(calendar.getTime());
    }

    private void fetchtourDetails(String tourId) {
        DocumentReference tourRef = db.collection("Tours").document(tourId);

        tourRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Retrieve basic spot details
                String name = documentSnapshot.getString("name");
                String promo_start = documentSnapshot.getString("promo_start");
                String promo_end = documentSnapshot.getString("promo_end");
                String desc = documentSnapshot.getString("desc");
                String spotImageUrl = documentSnapshot.getString("img_url");
                String days = documentSnapshot.getString("stay");
                Double price1 = documentSnapshot.getDouble("price");

                String priceString = price1.toString();

                // Set basic spot details to the corresponding views
                tourname.setText(name);
                accomodation.setText(promo_start + " - " +promo_end);
                description.setText(desc);
                stay.setText(days);
                pricefld.setText(priceString + " per pax");

                // Load the spot image into img_spot ImageView
                if (spotImageUrl != null) {
                    Glide.with(this)
                            .load(spotImageUrl)
                            .into(cover);
                }

                tourRef.collection("Inclusions").get().addOnSuccessListener(activitiesSnapshots -> {
                    ArrayList<tourModel> tourModelList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : activitiesSnapshots) {
                        String activityname = document.getString("name");
                        if (activityname != null) {
                            tourModel activityItem = new tourModel(activityname);
                            tourModelList.add(activityItem);
                        }
                    }

                    // Set up the RecyclerView with the activityModel objects using activityAdapter
                    tourAdapter adapter = new tourAdapter(this, tourModelList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    inclusionRecyclerView.setLayoutManager(layoutManager);
                    inclusionRecyclerView.setAdapter(adapter);
                }).addOnFailureListener(e -> {
                    // Handle errors that occurred while fetching data from the "Activities" sub-collection
                });

            } else {
                // Handle the case where the document does not exist
            }
        }).addOnFailureListener(e -> {
            // Handle errors that occurred while fetching spot details
        });
    }
}