package com.example.plantdroid.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlantDroidDao {
    //operation for table plant
    @Insert
    void insertPlants(Plant... plants);

    @Update
    void updatePlants(Plant... plants);

    @Delete
    void deletePlants(Plant... plants);

    @Query("DELETE FROM PLANT")
    void deleteAllPlants();

    @Query("SELECT * FROM PLANT ORDER BY ID DESC")
    LiveData<List<Plant>> getAllPlantsLive();


    //operation for table found_list
    @Insert
    void insertDiscoveredPlants(DiscoveredPlant... DiscoveredPlants);

    @Update
    void updateDiscoveredPlants(DiscoveredPlant... DiscoveredPlants);

    @Delete
    void deleteDiscoveredPlants(DiscoveredPlant... DiscoveredPlants);

    @Query("DELETE FROM DiscoveredPlant")
    void deleteAllDiscoveredPlants();

    @Query("SELECT * FROM DiscoveredPlant ORDER BY ID DESC")
    LiveData<List<Plant>> getAllDiscoveredPlantsLive();

}
