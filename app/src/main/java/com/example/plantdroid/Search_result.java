package com.example.plantdroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.plantdroid.Database.DiscoveredPlant;
import com.example.plantdroid.Database.Plant;
import com.example.plantdroid.Database.PlantDroidViewModel;
import com.example.plantdroid.ui.notifications.MyItemRecyclerViewAdapter;
import com.example.plantdroid.ui.notifications.NotificationsFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Search_result extends AppCompatActivity {
    List<Plant> Plants_result;
    RecyclerView RecyclerView;
    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> picture_url = new ArrayList<>();
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        String message = getIntent().getStringExtra("Search_context");
        RecyclerView=findViewById(R.id.Search_list);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        RecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
        PlantDroidViewModel plantDroidViewModel = ViewModelProviders.of(this).get(PlantDroidViewModel.class);
        plantDroidViewModel.searchPlantsByName(message).observe(this, new Observer<List<Plant>>() {
            @Override
            public void onChanged(List<Plant> Plants) {
                Plants_result=Plants;
                for (int i = 0; i < Plants_result.size(); i++) {
                        String plantname = Plants_result.get(i).getName();
                        String planturl = Plants_result.get(i).getImg();
                        name.add(plantname);
                        picture_url.add(planturl);
                }
                RecyclerView.setAdapter(new MyItemRecyclerViewAdapter(name, picture_url,Search_result.this));
            }
        });
    }

}