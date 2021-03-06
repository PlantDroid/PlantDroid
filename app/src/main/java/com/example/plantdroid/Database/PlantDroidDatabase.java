package com.example.plantdroid.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Plant.class, DiscoveredPlant.class}, version = 5, exportSchema = false)
public abstract class PlantDroidDatabase extends RoomDatabase {
    private static PlantDroidDatabase INSTANCE;

    static synchronized PlantDroidDatabase getDatabase(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), PlantDroidDatabase.class, "plant_droid_database.db")
                    .createFromAsset("prefill_database.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return INSTANCE;
    }

    public abstract PlantDroidDao getPlantDroidDao();

}
