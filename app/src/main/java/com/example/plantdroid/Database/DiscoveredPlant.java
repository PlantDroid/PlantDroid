package com.example.plantdroid.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity
public class DiscoveredPlant {

    public DiscoveredPlant(String foundTime, String altitude, String longitude, String accuracy, int plant_id) {
        this.foundTime = foundTime;
        this.altitude = altitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.plant_id = plant_id;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "found_time")
    private String foundTime;

    @ColumnInfo(name = "altitude")
    private String altitude;

    @ColumnInfo(name = "longitude")
    private String longitude;

    @ColumnInfo(name = "accuracy")
    private String accuracy;

    @ColumnInfo(name = "plant_id")
    private int plant_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoundTime() {
        return foundTime;
    }

    public void setFoundTime(String foundTime) {
        this.foundTime = foundTime;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public int getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(int plant_id) {
        this.plant_id = plant_id;
    }
}
