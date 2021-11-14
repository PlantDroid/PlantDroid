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

    LiveData<List<Plant>> getAllPlantsLive(){
        return plantDroidRepository.getAllPlantList();
    }

    void insertPlants(Plant... plants){
        plantDroidRepository.insertPlants(plants);
    }
    void updatePlants(Plant... plants){
        plantDroidRepository.updatePlants(plants);
    }
    void deletePlants(Plant... plants){
        plantDroidRepository.deletePlants(plants);
    }
    void deleteAllPlants(){
        plantDroidRepository.deleteAllPlants();
    }


}
