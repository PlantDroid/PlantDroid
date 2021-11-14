package com.example.plantdroid.Database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PlantDroidViewModel extends AndroidViewModel {
    private PlantDroidRepository plantDroidRepository;

    public PlantDroidViewModel(@NonNull Application application) {
        super(application);
        plantDroidRepository = new PlantDroidRepository(application);
    }

    public LiveData<List<Plant>> getAllPlantsLive(){
        return plantDroidRepository.getAllPlantList();
    }

    public void insertPlants(Plant... plants){
        plantDroidRepository.insertPlants(plants);
    }
    public void updatePlants(Plant... plants){
        plantDroidRepository.updatePlants(plants);
    }
    public void deletePlants(Plant... plants){
        plantDroidRepository.deletePlants(plants);
    }
    public void deleteAllPlants(){
        plantDroidRepository.deleteAllPlants();
    }


}
