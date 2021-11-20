package com.example.plantdroid;

import android.app.Activity;
import android.content.Context;
import android.icu.math.BigDecimal;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CustomMapStyleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.ServiceSettings;
import com.example.plantdroid.Database.DiscoveredPlant;
import com.example.plantdroid.Database.Plant;
import com.example.plantdroid.Database.PlantDroidViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MapActivity extends AppCompatActivity {
    MapView mMapView = null;
    private static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        MapActivity.context = getApplicationContext();
        ServiceSettings.updatePrivacyShow(this, true, true);
        ServiceSettings.updatePrivacyAgree(this, true);
        setContentView(R.layout.activity_map);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        AMap aMap = null;
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。

        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。


        getAllLocation(aMap);
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
        Log.i("####加载自定义地图=", filePath + styleName);
        aMap.setCustomMapStyle(
                new com.amap.api.maps.model.CustomMapStyleOptions()
                        .setEnable(true)
                        .setStyleDataPath(filePath + "/" + styleName)
//                        .setStyleExtraPath("/mnt/sdcard/amap/style_extra.data")                       .setStyleTexturePath("/mnt/sdcard/amap/textures.zip")
                        );

    }

    private Context getAppContext() {
        return MapActivity.context;
    }

    protected void getAllLocation(AMap aMap) {
        PlantDroidViewModel plantDroidViewModel = ViewModelProviders.of(this).get(PlantDroidViewModel.class);

        plantDroidViewModel.getAllDiscoveredPlantsLive().observe(this, plants -> {


            for (int i = 0; i < plants.size(); i++) {
                DiscoveredPlant plant = plants.get(i);

                LatLng latLng = new LatLng(plant.getLatitude(), plant.getLongitude());
                BigDecimal bd;
                bd = new BigDecimal(plant.getFoundTime());

                Date date = new Date(bd.longValue() * 1000L);
                Log.i("TImeeee", "getAllLocation: " + date);

                Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
                final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("Plant " + String.valueOf(plant.getPlant_id())).snippet("Found at " + format.format(date)));
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

}