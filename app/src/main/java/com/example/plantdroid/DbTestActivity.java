package com.example.plantdroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.plantdroid.Database.Plant;
import com.example.plantdroid.Database.PlantDroidViewModel;

import java.util.List;

public class DbTestActivity extends AppCompatActivity {
    TextView textView;
    Button buttonInsert, buttonUpdate, buttonDelete, buttonClear, buttonGatAll;
    PlantDroidViewModel plantDroidViewModel;

    LiveData<List<Plant>> plantCatalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_test);
        plantDroidViewModel = ViewModelProviders.of(this).get(PlantDroidViewModel.class);

        textView = findViewById(R.id.textView);
        plantDroidViewModel.getAllPlantsLive().observe(this, plants -> {
            StringBuilder text = new StringBuilder();

            for (int i = 0; i < plants.size(); i++) {
                Plant plant = plants.get(i);
                text.append(plant.getId()).append(":").append(plant.getName()).append("!!").append("\n");
            }
            textView.setText(text.toString());
        });

        buttonInsert = findViewById(R.id.buttonInsert);
        buttonClear = findViewById(R.id.buttonClear);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonGatAll = findViewById(R.id.button);
        buttonInsert.setOnClickListener(v -> {
            Plant plant1 = new Plant("Flower", "aaaa", "aaaa", "aaaa", "aaaa", "aaaa", "aaa", "aaaa", "aaaa", "aaaa", "aaaa", "aaaa", "aaa", false);
            Plant plant2 = new Plant("Sunflower", "aaaa", "aaaa", "aaaa", "aaaa", "aaaa", "aaa", "aaaa", "aaaa", "aaaa", "aaaa", "aaaa", "aaa", false);
            plantDroidViewModel.insertPlants(plant1, plant2);
        });

        buttonClear.setOnClickListener(v -> plantDroidViewModel.deleteAllPlants());

        buttonUpdate.setOnClickListener(v -> {
//            Plant plant = new Plant("Lily","aaaa","aaaa","aaaa","aaaa","aaaa","aaaa","aaaa","aaaa","aaaa","aaaa",false);
//            plant.setId(90);
//            plantDroidViewModel.updatePlants(plant);
            plantDroidViewModel.deleteAllPlants();
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Plant plant = new Plant("Flower", "aaaa", "aaaa", "aaaa", "aaaa", "aaaa", "aaa", "aaaa", "aaaa", "aaaa", "aaaa", "aaaa", "aaa", false);
                plant.setId(90);
                plantDroidViewModel.deletePlants(plant);
            }
        });
    }


}
