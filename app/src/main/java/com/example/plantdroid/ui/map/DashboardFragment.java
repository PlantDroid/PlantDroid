package com.example.plantdroid.ui.map;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.plantdroid.Database.PlantDroidViewModel;
import com.example.plantdroid.databinding.FragmentDashboardBinding;
import com.tbruyelle.rxpermissions2.RxPermissions;

public class DashboardFragment extends Fragment {


    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView textView = binding.textDashboard;
        final Button btn = binding.mapBtn;

        RxPermissions rxPermissions = new RxPermissions(getParentFragment().getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) rxPermissions
                .request(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(granted -> {
                    System.out.println("[Granted] " + granted);
                    if (granted) {
                        //获得权限
                        try {
                            //showMsg("已授权精确定位权限");
                        } catch (SecurityException e) {
                            System.out.println("[Error]");
                            e.printStackTrace();
                        }
                    } else {
                        //showMsg("未获取到位置权限,请在设置内打开");
                    }
                });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MapActivity.class);
                startActivity(intent);
            }
        });
        PlantDroidViewModel plantDroidViewModel = ViewModelProviders.of(this).get(PlantDroidViewModel.class);
        plantDroidViewModel.getAllDiscoveredPlantsLive().observe(getViewLifecycleOwner(), plants -> {

            textView.setText(String.valueOf(plants.size()));
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}