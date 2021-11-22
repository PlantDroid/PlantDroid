package com.example.plantdroid.ui.notifications;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import java.util.List;

@SuppressLint("ValidFragment")
public class MainFragment extends Fragment {
    private static String catalog_title;
    PlantDroidViewModel plantDroidViewModel;
    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> picture_url = new ArrayList<>();

    public static MainFragment newInstance(String title) {
        MainFragment mainFragment = new MainFragment();
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
//        RecyclerView list=view.findViewById(R.id.list);
//        plantDroidViewModel = ViewModelProviders.of(this).get(PlantDroidViewModel.class);
//        plantDroidViewModel.getAllPlantsLive().observe(getViewLifecycleOwner(), new Observer<List<Plant>>() {
//            @Override
//            public void onChanged(List<Plant> plants) {
//                for (int i = 0; i < plants.size(); i++) {
////                    Log.e("TAG", "catalog_title: "+catalog_title );
////                    if(catalog_title== plants.get(i).getPhylum()){
//                        String plantname = plants.get(i).getName();
//                        String planturl = plants.get(i).getImg();
//                        name.add(plantname);
//                        picture_url.add(planturl);
//                    Log.e("TAG", "otherPlant: "+plantname );
////                        Log.e("TAG", "onChanged: " +plants.get(i).getPhylum());
////                    }else if (catalog_title=="Other"){
////                        String plantname = plants.get(i).getName();
////                        String planturl = plants.get(i).getImg();
////                        name.add(plantname);
////                        picture_url.add(planturl);
////                        Log.e("TAG", "otherPlant: "+plantname );
////                    }
//                }
//                if (view instanceof RecyclerView) {
//                    Context context = view.getContext();
//                    RecyclerView recyclerView = (RecyclerView) view;
//                    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
//                    recyclerView.setLayoutManager(layoutManager);
//                    recyclerView.setAdapter(new MyItemRecyclerViewAdapter(name, picture_url,getActivity()));
//                }
//            }
//
//        });

        return view;
    }
}