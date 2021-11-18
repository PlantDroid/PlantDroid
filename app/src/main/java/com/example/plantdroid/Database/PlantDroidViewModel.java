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

    //only for test! Do not call this function in any activity!
    public PlantDroidRepository testRp(){
        return plantDroidRepository;
    }

    public LiveData<List<Plant>> getAllPlantsLive(){
        return plantDroidRepository.getAllPlantList();
    }
    public void insertPlants(Plant... plants){
        if (plants.length == 1 ){
            plantDroidRepository.getPlantByName(plants[0].getName());

            plantDroidRepository.insertPlants(plants[0]);

        }
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
    public void getPlantListByPhylum(String name){
        plantDroidRepository.getPlantListByPhylum(name);
    }
    public void searchPlantsByName(String name){
        plantDroidRepository.getSearchPlantByName(name);
    }
    public void getPlantByName(String name){
        plantDroidRepository.getPlantByName(name);
    }
    public void getPlantById(int id){
        plantDroidRepository.getPlantById(id);
    }




    public LiveData<List<DiscoveredPlant>> getAllDiscoveredPlantsLive(){
        return plantDroidRepository.getAllDiscoveredPlantList();
    }
    public void insertDiscoveredPlants(DiscoveredPlant... discoveredPlants){
        plantDroidRepository.insertDiscoveredPlants(discoveredPlants);
    }
    public void updateDiscoveredPlants(DiscoveredPlant... discoveredPlants){
        plantDroidRepository.updateDiscoveredPlants(discoveredPlants);
    }
    public void deleteDiscoveredPlants(DiscoveredPlant... discoveredPlants){
        plantDroidRepository.deleteDiscoveredPlants(discoveredPlants);
    }
    public void deleteAllDiscoveredPlants(){
        plantDroidRepository.deleteAllDiscoveredPlants();
    }


}
