package com.example.adventours.ui.weather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.adventours.databinding.FragmentWeatherBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import com.example.adventours.R;


public class WeatherFragment extends Fragment {

    private FragmentWeatherBinding binding;
    private RelativeLayout homeRL;
    private ProgressBar loadingPB;
    private TextView cityNameTv, temperatureTV, conditionTV, windTV, cloudTV, humidityTV;
    private TextInputEditText CityEdit;
    private ImageView backIV, iconIV, searchIv, countryFlag;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private String cityName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WeatherViewModel weatherViewModel =
                new ViewModelProvider(this).get(WeatherViewModel.class);

        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize UI components
        initUIComponents(root);

        // Check and request location permission
        checkAndRequestLocationPermission();

        // Get current location and weather information
        getCurrentLocationAndWeather();

        // Set background based on time
//        setTimeBasedBackground();

        // Set up search button click listener
        setupSearchButtonListener();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initUIComponents(View root) {
        // Initialize UI components
        loadingPB = root.findViewById(R.id.Loading_id);
        cityNameTv = root.findViewById(R.id.idTVCityName);
        temperatureTV = root.findViewById(R.id.idTVTemperature);
        conditionTV = root.findViewById(R.id.idTVCondition);
        CityEdit = root.findViewById(R.id.idETCity);
        backIV = root.findViewById(R.id.IdIVBack);
        iconIV = root.findViewById(R.id.idIVIcon);
        searchIv = root.findViewById(R.id.idTVSearch);
        windTV = root.findViewById(R.id.idTVWindTextMetric);
        cloudTV = root.findViewById(R.id.idTVCloudTextMetric);
        humidityTV = root.findViewById(R.id.idTVCHumidTextMetric);
        countryFlag = root.findViewById(R.id.idIVFlag);
        homeRL = root.findViewById(R.id.idRLHome);
    }

    private void checkAndRequestLocationPermission() {
        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }
    }

    private void getCurrentLocationAndWeather() {
        // Check if the app has the necessary location permissions
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If permissions are not granted, request them
            // This should ideally be handled by showing a permission dialog to the user
            // and handling the result in onRequestPermissionsResult method
            // requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, YOUR_PERMISSION_REQUEST_CODE);
            return;
        }

        // Get the last known location
        LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (location != null) {
            // If location is not null, get the city name
            String cityName = getCityName(location.getLongitude(), location.getLatitude());

            // Check if cityName is not empty before proceeding to get weather info
            if (!TextUtils.isEmpty(cityName)) {
                getWeatherInfo(cityName);
            } else {
                // Handle the case where city name is not available
                // This could happen if geocoding fails or the location information is not accurate
                // You may want to show an error message or fallback to a default location
            }
        } else {
            // Handle the case where location is null
            // This could happen if location information is not available
            // You may want to show an error message or fallback to a default location
        }
    }


//    private void setTimeBasedBackground() {
//        Calendar calendar = Calendar.getInstance();
//        Date currentTime = calendar.getTime();
//        SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.getDefault());
//        String currentHourString = sdf.format(currentTime);
//        int currentHour = Integer.parseInt(currentHourString);
//
//        int dayImage = R.drawable.day;
//        int nightImage = R.drawable.night;
//        int backgroundResource;
//
//        if (currentHour >= 18 || currentHour < 6) {
//            // Night time (6 PM to 6 AM)
//            backgroundResource = nightImage;
//        } else {
//            // Day time (6 AM to 6 PM)
//            backgroundResource = dayImage;
//        }
//
//        backIV.setImageResource(backgroundResource);
//        backIV.setScaleType(ImageView.ScaleType.CENTER_CROP);
//    }

    private void setupSearchButtonListener() {
        searchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = CityEdit.getText().toString().trim();
                if (city.isEmpty()) {
                    Toast.makeText(requireContext(), "Please enter city name", Toast.LENGTH_SHORT).show();
                } else {
                    cityNameTv.setText(city);
                    getWeatherInfo(city);
                }
            }
        });
    }

    private String getCityName(double longitude, double latitude) {
        String cityName = "Not Found";
        Geocoder gcd = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 10);
            for (Address adr : addresses) {
                if (adr != null) {
                    String city = adr.getLocality();
                    if (city != null && !city.equals("")) {
                        cityName = city;
                    } else {
                        Toast.makeText(requireContext(), "User City Not Found..", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }

    private void getWeatherInfo(String cityName) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=89852f15bebd043e42effdd09d6aef37&units=metric";
        cityNameTv.setText(cityName);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingPB.setVisibility(View.GONE);
                homeRL.setVisibility(View.VISIBLE);

                try {
                    JSONObject mainObject = response.getJSONObject("main");
                    String temperature = mainObject.getString("temp");
                    temperatureTV.setText(temperature + "°C");

                    JSONArray weatherArray = response.getJSONArray("weather");
                    if (weatherArray.length() > 0) {
                        JSONObject weatherObject = weatherArray.getJSONObject(0);
                        String condition = weatherObject.getString("main");
                        String description = weatherObject.getString("description");
                        conditionTV.setText(condition + " (" + description + ")");
                        JSONObject windObject = response.getJSONObject("wind");
                        double windSpeed = windObject.getDouble("speed");
                        String windInfo = windSpeed + " m/s";
                        windTV.setText(windInfo);
                        JSONObject cloudObject = response.getJSONObject("clouds");
                        int cloudPercentage = cloudObject.getInt("all");
                        // Update the cloud information in your UI
                        String cloudInfo = cloudPercentage + "%";
                        cloudTV.setText(cloudInfo);
                        double humidity = mainObject.getDouble("humidity");
                        // Update the humidity information in your UI
                        String humidityInfo =  humidity + "%";
                        humidityTV.setText(humidityInfo);
                        String cityName = response.getString("name");
                        cityNameTv.setText(cityName);


                        JSONObject sysObject = response.getJSONObject("sys");
                        String countryCode = sysObject.getString("country");
                        String countryUrl = "https://flagcdn.com/144x108/" + countryCode.toLowerCase() + ".png";
                        Picasso.get().load(countryUrl).into(countryFlag);

                        String iconCode = weatherObject.getString("icon");
                        String iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png";
                        Picasso.get().load(iconUrl).into(iconIV);
                        int dayImage = R.drawable.day;
                        int sunny = R.drawable.sunny;
                        int fewCloudsDay = R.drawable.fewclouds;
                        int fewCloudsNight = R.drawable.fewcloudsnight;
                        int scatteredCloudDay = R.drawable.scatteredcloudday;
                        int scatteredCloudNight = R.drawable.scatteredcloudnight;
                        int nightClear = R.drawable.nigth2;
                        int hazeDay = R.drawable.haze;
                        int hazeNight = R.drawable.hazenight;
                        int cloudsDay = R.drawable.clouds;
                        int cloudsNight = R.drawable.cloudsnight;
                        int dayRain = R.drawable.rainday;
                        int nightRain = R.drawable.rainnight;
                        int dayThunder = R.drawable.thunderday;
                        int nightThunder = R.drawable.thundernight;
                        int ClearRainDay = R.drawable.rainclearday;
                        int ClearRainNight = R.drawable.rainclearnight;
                        int nightImage = R.drawable.night;
                        int backgroundResource;
                        Calendar calendar = Calendar.getInstance();
                        Date currentTime = calendar.getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.getDefault());
                        String currentHourString = sdf.format(currentTime);
                        int currentHour = Integer.parseInt(currentHourString);


                        //day and sunny
                        if (iconCode.equals("01d")) {
                            backgroundResource = sunny;
                        }
                        //night and clear
                        else if (iconCode.equals("01n")) {
                            backgroundResource = nightClear;
                        }
                        //day and few clouds
                        else if (iconCode.equals("02d")) {
                            backgroundResource = fewCloudsDay;
                        }
                        //night and few clouds
                        else if (iconCode.equals("02n")) {
                            backgroundResource = fewCloudsNight;
                        }
                        //day and scattered clouds
                        else if (iconCode.equals("03d")) {
                            backgroundResource = scatteredCloudDay;
                        }
                        //night and scattered clouds
                        else if (iconCode.equals("03n")) {
                            backgroundResource = scatteredCloudNight;
                        }
                        //cloud day
                        else if (iconCode.equals("04d")) {
                            backgroundResource = cloudsDay;
                        }//cloud night
                        else if (iconCode.equals("04n")) {
                            backgroundResource = cloudsNight;
                        }
                        //haze day
                        else if (iconCode.equals("50d")) {
                            backgroundResource = hazeDay;
                        }
                        //haze night
                        else if (iconCode.equals("50n")) {
                            backgroundResource = hazeNight;
                        }
                        // day rain
                        else if (iconCode.equals("13d")) {
                            backgroundResource = dayRain;
                        }
                        // night rain
                        else if (iconCode.equals("13n")) {
                            backgroundResource = nightRain;
                        }
                        //thunder day
                        else if (iconCode.equals("13d")) {
                            backgroundResource = dayThunder;
                        }
                        //thunder night
                        else if (iconCode.equals("13n")) {
                            backgroundResource = nightThunder;
                        }
                        //clear rain day
                        else if (iconCode.equals("10d")) {
                            backgroundResource = ClearRainDay;
                        }
                        //clear rain night
                        else if (iconCode.equals("10n")) {
                            backgroundResource = ClearRainNight;
                        }


                        else {
                            if (currentHour >= 18 || currentHour < 6) {
                                // Night time (6 PM to 6 AM)
                                backgroundResource = nightImage;
                            } else {
                                // Day time (6 AM to 6 PM)
                                backgroundResource = dayImage;
                            }
                        }

                        backIV.setImageResource(backgroundResource);
                        backIV.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Enter a valid city name..", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Please provide the permission", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
        }
    }
}
