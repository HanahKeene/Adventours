package com.example.adventours.ui.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.adventours.databinding.FragmentHomeBinding;
import com.example.adventours.databinding.FragmentWeatherBinding;
import com.example.adventours.ui.weather.WeatherViewModel;

public class WeatherFragment extends Fragment {

    private FragmentWeatherBinding binding;
//    private FragmentWeatherBinding binding;
//    private ProgressBar loadingPB;
//    private TextView cityNameTv, temperatureTV, conditionTV, windTV, cloudTV, humidityTV;
//    private ImageView iconIV, searchIv, countryFlag;
//    private WeatherViewModel weatherViewModel;
//    private String cityName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WeatherViewModel weatherViewModel =
                new ViewModelProvider(this).get(WeatherViewModel.class);

        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //API Key : 365ca09a41aa4fa12f50090fc4c4cef1

//        loadingPB = binding.LoadingId;
//        cityNameTv = binding.idTVCityName;
//        temperatureTV = binding.idTVTemperature;
//        conditionTV = binding.idTVCondition;
//        iconIV = binding.idIVIcon;
//        windTV = binding.idTVWindTextMetric;
//        cloudTV = binding.idTVCloudTextMetric;
//        humidityTV = binding.idTVCHumidTextMetric;
//        countryFlag = binding.idIVFlag;

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}