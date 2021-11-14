package com.example.plantdroid.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PlantDroidRepository {
    private PlantDroidDao plantDroidDao;
    private LiveData<List<Plant>> allPlantList;


    public PlantDroidRepository(Context context) {
        PlantDroidDatabase plantDroidDatabase = PlantDroidDatabase.getDatabase(context.getApplicationContext());
        plantDroidDao = plantDroidDatabase.getPlantDroidDao();
        allPlantList = plantDroidDao.getAllPlantsLive();
    }

    LiveData<List<Plant>> getAllPlantList(){
        return allPlantList;
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

    static class DeleteAllPlantAsyncTask extends AsyncTask<Plant, Void, Void> {
        private PlantDroidDao plantDroidDao;

        DeleteAllPlantAsyncTask(PlantDroidDao plantDroidDao) {
            this.plantDroidDao = plantDroidDao;
        }

        @Override
        protected Void doInBackground(Plant... plants) {
            plantDroidDao.deleteAllPlants();
            return null;
        }
    }


}
