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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.example.adventours.databinding.FragmentWeatherBinding;
import com.example.adventours.ui.adapters.WeeklyForecast;
import com.example.adventours.ui.adapters.WeeklyForecastAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private TextView cityNameTv, temperatureTV, conditionTV, windTV, cloudTV, humidityTV, currentdate;
    private ImageView backIV, iconIV, searchIv, countryFlag;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private String cityName;
    private RecyclerView hourly, weekly;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WeatherViewModel weatherViewModel =
                new ViewModelProvider(this).get(WeatherViewModel.class);

        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        RequestQueue requestQueue = Volley.newRequestQueue(getContext(), new TLS12HurlStack());

        initUIComponents(root);
        checkAndRequestLocationPermission();
        getCurrentLocationAndWeather();
        getCurrentDate();
        Picasso.get();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initUIComponents(View root) {
        loadingPB = root.findViewById(R.id.Loading_id);
        cityNameTv = root.findViewById(R.id.location);
        temperatureTV = root.findViewById(R.id.idTVTemperature);
        conditionTV = root.findViewById(R.id.idTVCondition);
        backIV = root.findViewById(R.id.IdIVBack);
        iconIV = root.findViewById(R.id.idIVIcon);
        windTV = root.findViewById(R.id.idTVWindTextMetric);
        cloudTV = root.findViewById(R.id.idTVCloudTextMetric);
        humidityTV = root.findViewById(R.id.idTVCHumidTextMetric);
        homeRL = root.findViewById(R.id.idRLHome);
        currentdate = root.findViewById(R.id.currentdate);
    }

    private void getCurrentDate() {

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);
        currentdate.setText(formattedDate);
    }

    private void getCurrentWeatherInfo(String cityName) {
        String apiKey = "89852f15bebd043e42effdd09d6aef37";
        String currentWeatherUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKey + "&units=metric";
//        String hourlyForecastUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + cityName + "&appid=" + apiKey + "&units=metric";
//        String weeklyForecastUrl = "https://api.openweathermap.org/data/2.5/forecast/daily?q=" + cityName + "&appid=" + apiKey + "&units=metric&cnt=7";

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        JsonObjectRequest currentWeatherRequest = new JsonObjectRequest(Request.Method.GET, currentWeatherUrl, null,
                response -> {
                    try {
                        // Extract and update UI with weather information from the response
                        updateCurrentWeatherUI(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(requireContext(), "Error fetching weather data", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                });

//        JsonObjectRequest weeklyWeatherRequest = new JsonObjectRequest(Request.Method.GET, weeklyForecastUrl, null,
//                response -> {
//                    try {
//                        // Extract and update UI with weather information from the response
//                        updateWeeklyWeatherUI(response);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                },
//                error -> {
//                    Toast.makeText(requireContext(), "Error fetching weather data", Toast.LENGTH_SHORT).show();
//                    error.printStackTrace();
//                });


        requestQueue.add(currentWeatherRequest);
//        requestQueue.add(weeklyWeatherRequest);
//        requestQueue.add(hourlyWeatherRequest);
    }

//    private void updateWeeklyWeatherUI(JSONObject response) throws JSONException {
//
//        List<WeeklyForecast> weeklyForecastList = new ArrayList<>();
//
//        JSONArray forecastArray = response.getJSONArray("list");
//        for (int i = 0; i < forecastArray.length(); i++) {
//            JSONObject forecastObj = forecastArray.getJSONObject(i);
//            long timestamp = forecastObj.getLong("dt") * 1000; // Convert to milliseconds
//            Date date = new Date(timestamp);
//            SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.getDefault()); // Get day of the week (e.g., Mon, Tue, etc.)
//            String dayOfWeek = sdf.format(date);
//
//            JSONObject mainObj = forecastObj.getJSONObject("temp");
//            double minTemp = mainObj.getDouble("min");
//            double maxTemp = mainObj.getDouble("max");
//
//            JSONArray weatherArray = forecastObj.getJSONArray("weather");
//            JSONObject weatherObj = weatherArray.getJSONObject(0);
//            String conditionIcon = weatherObj.getString("icon");
//
//            // Create WeeklyForecast object
//            WeeklyForecast weeklyForecast = new WeeklyForecast(dayOfWeek, String.valueOf((int) minTemp) + "°C", String.valueOf((int) maxTemp) + "°C", "https://openweathermap.org/img/w/" + conditionIcon + ".png");
//            weeklyForecast.setDay(dayOfWeek);
//            weeklyForecast.setMinTemperature(String.valueOf((int) minTemp) + "°C");
//            weeklyForecast.setMaxTemperature(String.valueOf((int) maxTemp) + "°C");
//            weeklyForecast.setConditionIconUrl("https://openweathermap.org/img/w/" + conditionIcon + ".png");
//
//            weeklyForecastList.add(weeklyForecast);
//        }
//
//        // Now you have a list of WeeklyForecast objects containing the parsed data
//        // You can update your RecyclerView adapter with this data
//        // For example:
//        WeeklyForecastAdapter adapter = new WeeklyForecastAdapter(weeklyForecastList);
//        weekly.setAdapter(adapter);
//    }


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
            return;
        }
        LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (location != null) {
            String cityName = getCityName(location.getLongitude(), location.getLatitude());
            if (!TextUtils.isEmpty(cityName)) {
                getCurrentWeatherInfo(cityName);
            } else {
                Log.e("Location", "City name not available");
            }
        } else {
            Log.e("Location", "Location is null");
        }
    }

    private String getCityName(double longitude, double latitude) {
        String cityName = "Not Found";
        Geocoder gcd = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                cityName = addresses.get(0).getLocality();
                cityNameTv.setText(cityName + " City");
                Toast.makeText(requireContext(), "City: " + cityName, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "City not found", Toast.LENGTH_SHORT).show();
                Log.e("Location", "No addresses found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Location", "Geocoder error: " + e.getMessage());
        }
        return cityName;
    }

    private void updateCurrentWeatherUI(JSONObject response) throws JSONException {

        JSONObject mainObject = response.getJSONObject("main");
        String temperature = mainObject.getString("temp");
        double temperatureDouble = Double.parseDouble(temperature);
        int temperatureInteger = (int) Math.round(temperatureDouble);
        temperatureTV.setText(temperatureInteger + "°C");

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
            String cloudInfo = cloudPercentage + "%";
            cloudTV.setText(cloudInfo);
            double humidity = mainObject.getDouble("humidity");
            String humidityInfo = humidity + "%";
            humidityTV.setText(humidityInfo);
            String cityName = response.getString("name");
            cityNameTv.setText(cityName);

            String iconCode = weatherObject.getString("icon");
            String iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png";
            Log.d("WeatherLog", "iconUrl: " + iconUrl);
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
            } else {
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
    }
}
