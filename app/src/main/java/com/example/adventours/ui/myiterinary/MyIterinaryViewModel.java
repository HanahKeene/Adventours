package com.example.adventours.ui.myiterinary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyIterinaryViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MyIterinaryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}