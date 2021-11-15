package com.example.plantdroid.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PlantDroidRepository {
    private PlantDroidDao plantDroidDao;
    private LiveData<List<Plant>> allPlantList;

//    private LiveData<List<Plant>> PlantById;
//    private LiveData<List<Plant>> PlantByName;
//    private LiveData<List<Plant>> searchPlantByName;

    private LiveData<List<DiscoveredPlant>> allDiscoveredPlantList;


    public PlantDroidRepository(Context context) {
        PlantDroidDatabase plantDroidDatabase = PlantDroidDatabase.getDatabase(context.getApplicationContext());
        plantDroidDao = plantDroidDatabase.getPlantDroidDao();

        allPlantList = plantDroidDao.getAllPlantsLive();
        allDiscoveredPlantList = plantDroidDao.getAllDiscoveredPlantsLive();
    }

    public PlantDroidDao testDao(){
        return plantDroidDao;
    }

    LiveData<List<Plant>> getAllPlantList(){
        return allPlantList;
    }
    LiveData<List<Plant>> getPlantByName(String name){
        return plantDroidDao.getPlantByName(name);
    }
    LiveData<List<Plant>> getPlantById(int id){
        return plantDroidDao.getPlantById(id);
    }
    LiveData<List<Plant>> getSearchPlantByName(String name){
        return plantDroidDao.searchPlantByName(name);
    }
    LiveData<List<Plant>> getPlantListByPhylum(String phylum){
        return plantDroidDao.getPlantListByPhylum(phylum);
    }



    void insertPlants(Plant... plants){
        new InsertPlantAsyncTask(plantDroidDao).execute(plants);
    }
    void updatePlants(Plant... plants){
        new UpdatePlantAsyncTask(plantDroidDao).execute(plants);
    }
    void deletePlants(Plant... plants){
        new DeletePlantAsyncTask(plantDroidDao).execute(plants);
    }
    void deleteAllPlants(Plant... plants){
        new DeleteAllPlantAsyncTask(plantDroidDao).execute();
    }


    LiveData<List<DiscoveredPlant>> getAllDiscoveredPlantList(){
        return allDiscoveredPlantList;
    }
    void insertDiscoveredPlants(DiscoveredPlant... discoveredPlants){
        new InsertDiscoveredPlantAsyncTask(plantDroidDao).execute(discoveredPlants);
    }
    void updateDiscoveredPlants(DiscoveredPlant... discoveredPlants){
        new UpdateDiscoveredPlantAsyncTask(plantDroidDao).execute(discoveredPlants);
    }
    void deleteDiscoveredPlants(DiscoveredPlant... discoveredPlants){
        new DeleteDiscoveredPlantAsyncTask(plantDroidDao).execute(discoveredPlants);
    }
    void deleteAllDiscoveredPlants(DiscoveredPlant... plants){
        new DeleteAllDiscoveredPlantAsyncTask(plantDroidDao).execute();
    }



    static class InsertPlantAsyncTask extends AsyncTask<Plant, Void, Void> {
        private PlantDroidDao plantDroidDao;

        InsertPlantAsyncTask(PlantDroidDao plantDroidDao) {
            this.plantDroidDao = plantDroidDao;
        }

        @Override
        protected Void doInBackground(Plant... plants) {
            plantDroidDao.insertPlants(plants);
            return null;
        }
    }

    static class UpdatePlantAsyncTask extends AsyncTask<Plant, Void, Void> {
        private PlantDroidDao plantDroidDao;

        UpdatePlantAsyncTask(PlantDroidDao plantDroidDao) {
            this.plantDroidDao = plantDroidDao;
        }

        @Override
        protected Void doInBackground(Plant... plants) {
            plantDroidDao.updatePlants(plants);
            return null;
        }
    }

    static class DeletePlantAsyncTask extends AsyncTask<Plant, Void, Void> {
        private PlantDroidDao plantDroidDao;

        DeletePlantAsyncTask(PlantDroidDao plantDroidDao) {
            this.plantDroidDao = plantDroidDao;
        }

        @Override
        protected Void doInBackground(Plant... plants) {
            plantDroidDao.deletePlants(plants);
            return null;
        }
    }

    static class DeleteAllPlantAsyncTask extends AsyncTask<Void, Void, Void> {
        private PlantDroidDao plantDroidDao;

        DeleteAllPlantAsyncTask(PlantDroidDao plantDroidDao) {
            this.plantDroidDao = plantDroidDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            plantDroidDao.deleteAllPlants();
            return null;
        }
    }

    static class InsertDiscoveredPlantAsyncTask extends AsyncTask<DiscoveredPlant, Void, Void> {
        private PlantDroidDao plantDroidDao;

        InsertDiscoveredPlantAsyncTask(PlantDroidDao plantDroidDao) {
            this.plantDroidDao = plantDroidDao;
        }

        @Override
        protected Void doInBackground(DiscoveredPlant... discoveredPlants) {
            plantDroidDao.insertDiscoveredPlants(discoveredPlants);
            return null;
        }
    }

    static class UpdateDiscoveredPlantAsyncTask extends AsyncTask<DiscoveredPlant, Void, Void> {
        private PlantDroidDao plantDroidDao;

        UpdateDiscoveredPlantAsyncTask(PlantDroidDao plantDroidDao) {
            this.plantDroidDao = plantDroidDao;
        }

        @Override
        protected Void doInBackground(DiscoveredPlant... discoveredPlants) {
            plantDroidDao.updateDiscoveredPlants(discoveredPlants);
            return null;
        }
    }

    static class DeleteDiscoveredPlantAsyncTask extends AsyncTask<DiscoveredPlant, Void, Void> {
        private PlantDroidDao plantDroidDao;

        DeleteDiscoveredPlantAsyncTask(PlantDroidDao plantDroidDao) {
            this.plantDroidDao = plantDroidDao;
        }

        @Override
        protected Void doInBackground(DiscoveredPlant... discoveredPlants) {
            plantDroidDao.deleteDiscoveredPlants(discoveredPlants);
            return null;
        }
    }

    static class DeleteAllDiscoveredPlantAsyncTask extends AsyncTask<Void, Void, Void> {
        private PlantDroidDao plantDroidDao;

        DeleteAllDiscoveredPlantAsyncTask(PlantDroidDao plantDroidDao) {
            this.plantDroidDao = plantDroidDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            plantDroidDao.deleteAllDiscoveredPlants();
            return null;
        }
    }
}
