package com.example.plantdroid;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.plantdroid.Database.Plant;
import com.example.plantdroid.Database.PlantDroidDao;
import com.example.plantdroid.Database.PlantDroidRepository;
import com.example.plantdroid.Database.PlantDroidViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private PlantDroidDao dao;
    private PlantDroidViewModel vm;

    Plant temp;
    int size;


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =  new InstantTaskExecutorRule();

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        PlantDroidRepository repo = new PlantDroidRepository(context);
        dao = repo.testDao();
        vm =  new PlantDroidViewModel(ApplicationProvider.getApplicationContext());

    }

    @After
    public void closeDb() throws IOException {
    }


    @Test
    public void testInsertPlant(){
        System.out.println(ArchTaskExecutor.getInstance().isMainThread());

        Plant plant = new Plant("Flower","aaaa","aaaa","aaaa","aaaa","aaaa","aaa","aaaa","aaaa","aaaa","aaaa","aaaa", "aaa", false);
        vm.insertPlants(plant);

        Observer<List<Plant>> observer = new Observer<List<Plant>>() {
            @Override
            public void onChanged(List<Plant> plants) {
                assertEquals(100, plants.size());
            }
        };


    }

    @Test
    public void testInsertPlant1() throws Exception{
        System.out.println(ArchTaskExecutor.getInstance().isMainThread());


        Plant plant = new Plant("Flower","aaaa","aaaa","aaaa","aaaa","aaaa","aaa","aaaa","aaaa","aaaa","aaaa","aaaa", "aaa", false);
        vm.insertPlants(plant);

        final LiveData<List<Plant>> list =  vm.getPlantByName("rtrt");

        Observer<List<Plant>> observer = plants -> temp = plants.get(0);

        list.observeForever(observer);

        assertEquals(plant.getName(), temp.getName());
    }


}
