package com.example.plantdroid;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.example.plantdroid.Database.Plant;
import com.example.plantdroid.Database.PlantDroidDao;
import com.example.plantdroid.Database.PlantDroidRepository;
import com.example.plantdroid.Database.PlantDroidViewModel;

import java.io.IOException;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

}