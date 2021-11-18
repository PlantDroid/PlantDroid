package com.example.plantdroid;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.util.ActivityLifecycles;
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

import kotlin.jvm.JvmField;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private PlantDroidRepository repo;
    private PlantDroidDao dao;
    private PlantDroidViewModel vm;

    private Plant temp;


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =  new InstantTaskExecutorRule();


    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        repo = new PlantDroidRepository(context);
        dao = repo.testDao();
        vm =  new PlantDroidViewModel(ApplicationProvider.getApplicationContext());


    }


    @After
    public void closeDb() throws IOException {
        return;
    }


    @Test
    public void testInsertPlant() throws Exception{
        System.out.println(ArchTaskExecutor.getInstance().isMainThread());

        Plant plant = new Plant("111","aaaa","aaaa","aaaa","aaaa","aaaa","aaaa","aaaa","aaaa","aaaa","aaaa",false);
        vm.insertPlants(plant);

        final LiveData<List<Plant>> list =  dao.getPlantByName("111");

        Observer<List<Plant>> observer = plants -> temp = plants.get(0);

        list.observeForever(observer);

        assertEquals(plant.getName(), temp.getName());
    }


}
