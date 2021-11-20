package com.example.plantdroid.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlantDroidDao {
    //operation for table plant
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertPlants(Plant... plants);

    @Update
    void updatePlants(Plant... plants);

    @Delete
    void deletePlants(Plant... plants);

    @Query("DELETE FROM PLANT")
    void deleteAllPlants();

    @Query("SELECT * FROM PLANT ORDER BY ID DESC")
    LiveData<List<Plant>> getAllPlantsLive();

    @Query("SELECT * FROM PLANT WHERE id LIKE :id")
    LiveData<List<Plant>> getPlantById(int id);

    @Query("SELECT * FROM PLANT WHERE plant_name LIKE :name")
    LiveData<List<Plant>> getPlantByName(String name);

    @Query("SELECT * FROM PLANT WHERE plant_name LIKE :name "
            + "OR common_names LIKE '%' + :name + '%' ")
    LiveData<List<Plant>> searchPlantByName(String name);

    @Query("SELECT * FROM PLANT WHERE phylum LIKE :phylum")
    LiveData<List<Plant>> getPlantListByPhylum(String phylum);



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
    LiveData<List<DiscoveredPlant>> getAllDiscoveredPlantsLive();

    @Query("SELECT * FROM DISCOVEREDPLANT WHERE plant_id LIKE :plantID")
    LiveData<List<DiscoveredPlant>> getDiscoveredPlantsByPlantID(int plantID);

//    @Query("SELECT * FROM DISCOVEREDPLANT WHERE plant_name LIKE :plant_name")
//    LiveData<List<DiscoveredPlant>> getDiscoveredPlantsByPlantName(String plant_name);

}
