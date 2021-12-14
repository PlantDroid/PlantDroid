package com.example.plantdroid.ui.map;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.icu.math.BigDecimal;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.ServiceSettings;
import com.example.plantdroid.Database.DiscoveredPlant;
import com.example.plantdroid.Database.PlantDroidViewModel;
import com.example.plantdroid.DetailPageActivity;
import com.example.plantdroid.R;
import com.example.plantdroid.ui.camera.CameraFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import util.LoadImageUtil;
import util.LocationUtil;

public class MapActivity extends AppCompatActivity {
    MapView mMapView = null;
    private static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // 给授权
        ServiceSettings.updatePrivacyShow(this, true, true);
        ServiceSettings.updatePrivacyAgree(this, true);

        Intent i = getIntent();
        String discoverId = getIntent().getStringExtra("discoverId");
        MapActivity.context = getApplicationContext();
        setContentView(R.layout.activity_map);
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        initMap(discoverId);
    }


    static Context getAppContext() {
        return MapActivity.context;
    }

    private void initMap(String discoverId) {

        AMap aMap = null;
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        if (discoverId != null) {
            getDiscoveredPlants(discoverId, aMap);

        } else {
            String[] coord = new String[2];
            LocationUtil.getInstance(MapActivity.context).setAddressCallback(new LocationUtil.AddressCallback() {
                @Override
                public void onGetAddress(Address address) {
                }

                @Override
                public void onGetLocation(double lat, double lng, double acc) {
                    coord[0] = String.valueOf(lat);
                    coord[1] = String.valueOf(lng);
                }
            });
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(
                    new CameraPosition(new LatLng(Double.valueOf(coord[0]), Double.valueOf(coord[1])), 17, 0, 0));
            aMap.moveCamera(mCameraUpdate);
        }
        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        aMap.setMapLanguage("en"); 设置英语
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.interval(2000);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
        getAllLocation(aMap);
        setMapStyle(aMap);
        aMap.setOnInfoWindowClickListener(listener);
    }

    private void setMapStyle(AMap aMap) {
        String styleName = "style.data";
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        String filePath = null;
        try {
            inputStream = getAppContext().getAssets().open(styleName);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            filePath = getAppContext().getFilesDir().getAbsolutePath();
            File file = new File(filePath + "/" + styleName);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            outputStream = new FileOutputStream(file);
            outputStream.write(b);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();

                if (outputStream != null)
                    outputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        aMap.setCustomMapStyle(
                new com.amap.api.maps.model.CustomMapStyleOptions()
                        .setEnable(true)
                        .setStyleDataPath(filePath + "/" + styleName)
                        .setStyleExtraPath(filePath + "/" + "style_extra.data")
        );
    }

    protected void getDiscoveredPlants(String plantId, AMap aMap) {
        PlantDroidViewModel plantDroidViewModel = ViewModelProviders.of(this).get(PlantDroidViewModel.class);
        int id = Integer.parseInt(plantId);
        plantDroidViewModel.getDiscoveredPlantById(id).observe(this, plants -> {
            DiscoveredPlant dp = (plants.get(0));
            double lo = 0.00, la = 0.00;
            lo = dp.getLongitude();
            la = dp.getLatitude();
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(
                    new CameraPosition(new LatLng(la, lo), 18, 0, 0));
            aMap.moveCamera(mCameraUpdate);
        });
    }

    protected void getAllLocation(AMap aMap) {
        PlantDroidViewModel plantDroidViewModel = ViewModelProviders.of(this).get(PlantDroidViewModel.class);
        plantDroidViewModel.getAllDiscoveredPlantsLive().observe(this, plants -> {


//            markerOption.setFlat(true);//设置marker平贴地图效果
            for (int i = 0; i < plants.size(); i++) {

                DiscoveredPlant plant = plants.get(i);


                double la = plant.getLatitude();
                double lo = plant.getLongitude();
                if (la == 0 && lo == 0) {
                    la += Math.random() / 10;
                    lo += Math.random() / 10;
                }
                LatLng latLng = new LatLng(la, lo);
                plantDroidViewModel.getPlantById(plant.getPlant_id()).observe(this, p -> {

                    if (!p.isEmpty()) {
                        BigDecimal bd;
                        bd = new BigDecimal(plant.getFoundTime());
                        Date date = new Date(bd.longValue() * 1000L);
                        Format format = new SimpleDateFormat("yyyy-MM-dd");
                        String plantPhylum = p.get(0).getPhylum();
                        String plantClass = p.get(0).getPlantClass();
                        Log.i("TAG222222222222", "getAllLocation: " + plantPhylum + " " + plantClass);
                        MarkerOptions markerOption = new MarkerOptions();
                        if (plantClass.equals("Pinopsida"))
                            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                    .decodeResource(getResources(), R.drawable.pinopsida)));
                        else if (plantClass.equals("Polypodiopsida"))
                            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                    .decodeResource(getResources(), R.drawable.plypodiopsida)));
                        else if (plantClass.equals("Agaricomycetes"))
                            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                    .decodeResource(getResources(), R.drawable.agaricomycetes)));
                        else if (plantClass.equals("Magnoliopsida"))
                            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                    .decodeResource(getResources(), R.drawable.magnoliophyta)));
                        else if (plantClass.equals("Polytrichopsida"))
                            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                    .decodeResource(getResources(), R.drawable.plant)));
                        else if (plantClass.equals("Cycadopsida"))
                            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                    .decodeResource(getResources(), R.drawable.cycadopsida)));
                        markerOption.position(latLng);
                        markerOption.title(p.get(0).getName()).snippet(format.format(date) + "$" + p.get(0).getImg());
                        final Marker marker = aMap.addMarker(markerOption);
                        InfoWindow info_window = new InfoWindow();
                        aMap.setInfoWindowAdapter(info_window);

                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    AMap.OnInfoWindowClickListener listener = new AMap.OnInfoWindowClickListener() {

        @Override
        public void onInfoWindowClick(Marker marker) {
            Intent intent = new Intent(MapActivity.this, DetailPageActivity.class);
            intent.putExtra("plantName", marker.getTitle());
            startActivity(intent);
            MapActivity.this.onPause();
        }
    };

}

class InfoWindow implements AMap.InfoWindowAdapter {


    @Override
    public View getInfoWindow(Marker marker) {

        View infoWindow = LayoutInflater.from(MapActivity.getAppContext()).inflate(
                R.layout.custom_info_window, null);
        if (marker.getTitle() != null) {
            render(marker, infoWindow);
            return infoWindow;
        }
        return null;
    }

    public void render(Marker marker, View view) {

        String title = marker.getTitle();

        if (title != null) {
            TextView title_ui = (TextView) view.findViewById(R.id.info_title);
            title_ui.setText(title);
            String snippet = marker.getSnippet();
            String[] snippetLst = snippet.split("\\$");
            TextView sippet_ui = (TextView) view.findViewById(R.id.info_snippet);
            sippet_ui.setText("Found at " + snippetLst[0]);
            ImageView plantImg = (ImageView) view.findViewById(R.id.info_imag);
            LoadImageUtil.setImageView(plantImg, snippetLst[1]);
        }
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

}