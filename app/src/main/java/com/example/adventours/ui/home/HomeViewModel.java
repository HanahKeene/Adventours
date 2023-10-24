package com.example.adventours.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    // Create LiveData variables for picture, place name, and location
    private MutableLiveData<String> imageUrl = new MutableLiveData<>();
    private MutableLiveData<String> placeName = new MutableLiveData<>();
    private MutableLiveData<String> location = new MutableLiveData<>();

    // Constructor, repository initialization, and data fetching methods go here

    public LiveData<String> getImageUrl() {
        return imageUrl;
    }

    public LiveData<String> getPlaceName() {
        return placeName;
    }

    public LiveData<String> getLocation() {
        return location;
    }

    // Method to fetch data from the database
    public void fetchDataFromDatabase() {
        // Assume you have a method in your repository to fetch data
        // DataModel data = repository.getDataFromDatabase();
        // imageUrl.setValue(data.getImageUrl());
        // placeName.setValue(data.getPlaceName());
        // location.setValue(data.getLocation());
    }
}
