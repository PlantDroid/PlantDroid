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

import com.example.plantdroid.Database.PlantDroidViewModel;
import com.example.plantdroid.R;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class MainFragment2 extends Fragment {
    private static String catalog_title;
    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> picture_url = new ArrayList<>();

    public static MainFragment2 newInstance(String title) {
        MainFragment2 mainFragment = new MainFragment2();
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
        View view = inflater.inflate(R.layout.fragment_view2, container, false);
        return view;
    }
}