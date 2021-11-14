package com.example.plantdroid.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Plant {
    public Plant(String name, String commonNames, String kingdom, String phylum, String plantClass, String order, String genus, String description, String img, String edibleParts, String propagationMethods, String isOwned) {
        this.name = name;
        this.commonNames = commonNames;
        this.kingdom = kingdom;
        this.phylum = phylum;
        this.plantClass = plantClass;
        this.order = order;
        this.genus = genus;
        this.description = description;
        this.img = img;
        this.edibleParts = edibleParts;
        this.propagationMethods = propagationMethods;
        this.isOwned = isOwned;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "plant_name")
    private String name;

    @ColumnInfo(name = "common_names")
    private String commonNames;

    @ColumnInfo(name = "kingdom")
    private String kingdom;

    @ColumnInfo(name = "phylum")
    private String phylum;

    @ColumnInfo(name = "class")
    private String plantClass;

    @ColumnInfo(name = "order")
    private String order;

    @ColumnInfo(name = "genus")
    private String genus;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "img")
    private String img;

    @ColumnInfo(name = "edible_parts")
    private String edibleParts;

    @ColumnInfo(name = "propagation_methods")
    private String propagationMethods;

    @ColumnInfo(name = "is_owned")
    private String isOwned;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommonNames() {
        return commonNames;
    }

    public void setCommonNames(String commonNames) {
        this.commonNames = commonNames;
    }

    public String getKingdom() {
        return kingdom;
    }

    public void setKingdom(String kingdom) {
        this.kingdom = kingdom;
    }

    public String getPhylum() {
        return phylum;
    }

    public void setPhylum(String phylum) {
        this.phylum = phylum;
    }

    public String getPlantClass() {
        return plantClass;
    }

    public void setPlantClass(String plantClass) {
        this.plantClass = plantClass;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getEdibleParts() {
        return edibleParts;
    }

    public void setEdibleParts(String edibleParts) {
        this.edibleParts = edibleParts;
    }

    public String getPropagationMethods() {
        return propagationMethods;
    }

    public void setPropagationMethods(String propagationMethods) {
        this.propagationMethods = propagationMethods;
    }

    public String getIsOwned() {
        return isOwned;
    }

    public void setIsOwned(String isOwned) {
        this.isOwned = isOwned;
    }
}


