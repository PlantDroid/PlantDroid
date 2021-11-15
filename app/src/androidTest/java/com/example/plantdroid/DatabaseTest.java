package com.example.plantdroid;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.plantdroid.Database.Plant;
import com.example.plantdroid.Database.PlantDroidDao;
import com.example.plantdroid.Database.PlantDroidRepository;
import com.example.plantdroid.Database.PlantDroidViewModel;

import java.io.IOException;
import java.util.List;


@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private PlantDroidRepository repo;
    private PlantDroidDao dao;
    private PlantDroidViewModel vm;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        repo = new PlantDroidRepository(context);
        dao = repo.testDao();
        vm =  new PlantDroidViewModel(ApplicationProvider.getApplicationContext());
    }

    @After
    public void closeDb() throws IOException{
        return;
    }

    @Test
    public void testInsertPlant() throws Exception{
        Plant plant = new Plant("Flower","aaaa","aaaa","aaaa","aaaa","aaaa","aaaa","aaaa","aaaa","aaaa","aaaa","aaaa");
        vm.insertPlants(plant);

        List<Plant> source = dao.getPlantByName("Flower").getValue();

        assertEquals(plant, source.get(0));

    }


}