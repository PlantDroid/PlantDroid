package com.example.plantdroid.ui.notifications;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.plantdroid.Database.Plant;
import com.example.plantdroid.Database.PlantDroidViewModel;
import com.example.plantdroid.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressLint("ValidFragment")
public class MainFragment8 extends Fragment {
    private static String catalog_title;
    private List<String> list = Arrays.asList("Pinopsida", "Cycadopsida", "Polypodiopsida", "Magnoliopsida", "Streptophyta", "Basidiomycota", "Magnoliopsida");

    public static MainFragment8 newInstance(String title) {
        MainFragment8 mainFragment = new MainFragment8();
        Bundle bundle = new Bundle();
        catalog_title = title;
        bundle.putString("title", title);
        mainFragment.setArguments(bundle);
        return mainFragment;
    }

    public static String getTitle() {
        return catalog_title;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> picture_url = new ArrayList<>();
        PlantDroidViewModel plantDroidViewModel = ViewModelProviders.of(this).get(PlantDroidViewModel.class);
        plantDroidViewModel.getAllPlantsLive().observe(getViewLifecycleOwner(), new Observer<List<Plant>>() {
            @Override
            public void onChanged(List<Plant> plants) {

                for (int i = 0; i < plants.size(); i++) {
                    if (!list.contains( plants.get(i).getPlantClass())&!list.contains(plants.get(i).getPhylum())){
                        String plantname = plants.get(i).getName();
                        String planturl = plants.get(i).getImg();
                        name.add(plantname);
                        picture_url.add(planturl);
                        break;
                    }
                }
                RecyclerView recyclerView = view.findViewById(R.id.list0);
                StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(new MyItemRecyclerViewAdapter(name, picture_url, getActivity()));
                if (name.size()>0){
                    View include = view.findViewById(R.id.empty_layout);
                    include.setVisibility(View.GONE);
                }
            }
        });
        name.clear();
        picture_url.clear();
        return view;

    }
}