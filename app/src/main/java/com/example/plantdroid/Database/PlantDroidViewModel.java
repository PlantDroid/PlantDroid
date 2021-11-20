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

    //only for Android test! Do not call this function in any activity!
    public PlantDroidRepository testRp(){
        return plantDroidRepository;
    }


    /**
     * method to get all Plants in Database
     * @return a LiveData<List<Plant>>
     */
    public LiveData<List<Plant>> getAllPlantsLive(){
        return plantDroidRepository.getAllPlantList();
    }

    /**
     *method to insert plants to database, can add more than one instance by one call
     *raise error if plant already exist
     * @param plants Pants to be added
     */
    public void insertPlants(Plant... plants){
        plantDroidRepository.insertPlants(plants);
    }

    /**
     *method to update plants in database, can add more than one instance by one call
     * @param plants Pants to be updated
     */
    public void updatePlants(Plant... plants){
        plantDroidRepository.updatePlants(plants);
    }


    /**
     *method to delete plants to database, can add more than one instance by one call
     * @param plants Pants to be delete
     */
    public void deletePlants(Plant... plants){
        plantDroidRepository.deletePlants(plants);
    }

    /**
     * method to delete all plants in the database
     */
    public void deleteAllPlants(){
        plantDroidRepository.deleteAllPlants();
    }

    /**
     * method to get all Plants with provided phylum in Database
     * @param name phylum name
     * @return a LiveData<List<Plant>>
     */
    public LiveData<List<Plant>> getPlantListByPhylum(String name){
       return plantDroidRepository.getPlantListByPhylum(name);
    }

    /**
     * method to get all Plants matching the common name and plant name in Database
     * @param name name to match
     * @return a LiveData<List<Plant>>
     */
    public LiveData<List<Plant>> searchPlantsByName(String name){
       return plantDroidRepository.getSearchPlantByName(name);
    }


    /**
     * method to get all Plants with provided plant in Database
     * @param name plant name
     * @return a LiveData<List<Plant>>
     */
    public LiveData<List<Plant>> getPlantByName(String name){
       return plantDroidRepository.getPlantByName(name);
    }

    /**
     * method to get all Plants with provided plant in Database
     * @param id plant id
     * @return a LiveData<List<Plant>>
     */
    public LiveData<List<Plant>> getPlantById(int id){
       return plantDroidRepository.getPlantById(id);
    }


    /**
     * method to get all Discovered Plants in Database
     * @return a LiveData<List<Plant>>
     */
    public LiveData<List<DiscoveredPlant>> getAllDiscoveredPlantsLive(){
        return plantDroidRepository.getAllDiscoveredPlantList();
    }

    /**
     * method to insert Discovered plants to database, can add more than one instance by one call
     * raise error if plant already exist
     * @param discoveredPlants discoveredPlants to insert
     */
    public void insertDiscoveredPlants(DiscoveredPlant... discoveredPlants){
        plantDroidRepository.insertDiscoveredPlants(discoveredPlants);
    }

    /**
     * method to update Discovered plants in database, can add more than one instance by one call
     * @param discoveredPlants discoveredPlants to be updated
     */
    public void updateDiscoveredPlants(DiscoveredPlant... discoveredPlants){
        plantDroidRepository.updateDiscoveredPlants(discoveredPlants);
    }

    /**
     * method to delete Discovered plants to database, can add more than one instance by one call
     * @param discoveredPlants discoveredPlants to be deleted
     */
    public void deleteDiscoveredPlants(DiscoveredPlant... discoveredPlants){
        plantDroidRepository.deleteDiscoveredPlants(discoveredPlants);
    }

    /**
     * method to delete all Discovered plants
     */
    public void deleteAllDiscoveredPlants(){
        plantDroidRepository.deleteAllDiscoveredPlants();
    }


    /**
     * method to get all Discovered plants matching the given plant_id in Database
     * @param plant_id plant id
     * @return a LiveData<List<Plant>>
     */
    public LiveData<List<DiscoveredPlant>> getDiscoveredPlantsByPlantID(int plant_id){
        return plantDroidRepository.getDiscoveredPlantsByPlantID(plant_id);
    }

}
