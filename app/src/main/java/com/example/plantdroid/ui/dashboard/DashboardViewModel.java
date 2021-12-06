package com.example.plantdroid.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.example.plantdroid.Database.PlantDroidViewModel;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DashboardViewModel() {

        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment55");
    }

    public LiveData<String> getText() {
        return mText;
    }
}