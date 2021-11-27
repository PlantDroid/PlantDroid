package com.example.plantdroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.icu.math.BigDecimal;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class MapActivity extends AppCompatActivity {
    MapView mMapView = null;
    private static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
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
        // 给授权
        ServiceSettings.updatePrivacyShow(this, true, true);
        ServiceSettings.updatePrivacyAgree(this, true);

        AMap aMap = null;
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        if (discoverId != null) {
            getDiscoveredPlants(discoverId, aMap);

        }
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();
//        aMap.setMapLanguage("en"); 设置英语
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

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
            Log.i("33333333333", "getDiscoveredPlants: " + dp.getFoundTime());
            double lo = 0.00, la = 0.00;
            if (dp.getLongitude() != null) {
                lo = dp.getLongitude();
                la = dp.getLatitude();

            }

            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(
                    new CameraPosition(new LatLng(la, lo), 10, 0, 0));
            aMap.moveCamera(mCameraUpdate);
        });


    }

    protected void getAllLocation(AMap aMap) {

        PlantDroidViewModel plantDroidViewModel = ViewModelProviders.of(this).get(PlantDroidViewModel.class);

        plantDroidViewModel.getAllDiscoveredPlantsLive().observe(this, plants -> {
            MarkerOptions markerOption = new MarkerOptions();
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(), R.drawable.plant)));
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
                        Format format = new SimpleDateFormat("yyyy M    M dd HH:mm:ss");
                        markerOption.position(latLng);
                        markerOption.title(p.get(0).getName()).snippet("Found at " + format.format(date));

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
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
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
            TextView sippet_ui = (TextView) view.findViewById(R.id.info_snippet);
            sippet_ui.setText(snippet);
        }
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

}