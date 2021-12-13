package com.example.plantdroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.icu.math.BigDecimal;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantdroid.Database.DiscoveredPlant;
import com.example.plantdroid.Database.Plant;
import com.example.plantdroid.Database.PlantDroidViewModel;
import com.example.plantdroid.ui.map.MapActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import util.LoadImageUtil;

public class DetailPageActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.region_right_menu, menu);
        return true;
        // return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.media_route_menu_item:
                Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();
                Calendar now = new GregorianCalendar();
                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                String fileName = simpleDate.format(now.getTime());

                viewSaveToImage(fileName, findViewById(R.id.detailPageScrollView));
                // saveBitmap(fileName+".jpg",cs,this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void viewSaveToImage(String fileName, ScrollView view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);

        int h = 0;
        Bitmap cachebmp = null;
        for (int i = 0; i < view.getChildCount(); i++) {
            h += view.getChildAt(i).getHeight();
        }
        cachebmp = Bitmap.createBitmap(view.getWidth(), h, Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(cachebmp);
        canvas.drawColor(getColor(R.color.green_light_1));
        view.draw(canvas);

        // 把一个View转换成图片
        // Bitmap cachebmp = loadBitmapFromView(view);
        saveBitmap(fileName, cachebmp, this);
    }
    
    // @SuppressLint("NewApi")
    // private Bitmap captureScreen(Activity context) {
    //     View cv = context.getWindow().getDecorView();
    //
    //     cv.setDrawingCacheEnabled(true);
    //     cv.buildDrawingCache();
    //     Bitmap bmp = cv.getDrawingCache();
    //     if (bmp == null) {
    //         return null;
    //     }
    //
    //     bmp.setHasAlpha(false);
    //     bmp.prepareToDraw();
    //     return bmp;
    // }

    static boolean fileIsExist(String fileName) {
        //传入指定的路径，然后判断路径是否存在
        File file = new File(fileName);
        if (file.exists())
            return true;
        else {
            //file.mkdirs() 创建文件夹的意思
            return file.mkdirs();
        }
    }

    void saveBitmap(String name, Bitmap bm, Context mContext) {
        Log.d("Save Bitmap", "Ready to save picture");
        //指定我们想要存储文件的地址
        String TargetPath = mContext.getExternalFilesDir(null) + "/images/";
        Log.d("Save Bitmap", "Save Path=" + TargetPath);
        //判断指定文件夹的路径是否存在
        if (!fileIsExist(TargetPath)) {
            Log.d("Save Bitmap", "TargetPath isn't exist");
        } else {
            //如果指定文件夹创建成功，那么我们则需要进行图片存储操作
            File saveFile = new File(TargetPath, name);

            try {
                FileOutputStream saveImgOut = new FileOutputStream(saveFile);
                // compress - 压缩的意思
                bm.compress(Bitmap.CompressFormat.JPEG, 80, saveImgOut);
                //存储完成后需要清除相关的进程
                saveImgOut.flush();
                saveImgOut.close();
                Log.d("Save Bitmap", "The picture is save to your phone!");

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Uri imageUri1;
            // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //     imageUri1 = FileProvider.getUriForFile(getActivity(), "com.example.plantdroid.fileprovider", saveFile);
            // } else {
            imageUri1 = FileProvider.getUriForFile(
                    this,
                    "com.example.plantdroid.fileprovider",
                    saveFile);

            //}
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri1);
            shareIntent.setType("image/jpeg");
            startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.hello)));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        String plantName = getIntent().getStringExtra("plantName");
        System.out.println("[Plant Name] " + plantName);
        loadPlantInfo(plantName);
    }

    public void loadPlantInfo(String plantName) {
        PlantDroidViewModel plantDroidViewModel = ViewModelProviders.of(this).get(PlantDroidViewModel.class);
        plantDroidViewModel.getPlantByName(plantName).observe(this, new Observer<List<Plant>>() {
            @Override
            public void onChanged(List<Plant> plants) {
                if (plants.isEmpty()) {
                    Toast.makeText(DetailPageActivity.this, "The plant isn't exist in catalog.", Toast.LENGTH_LONG).show();
                    System.out.println("[Plant] plant not exist.");
                    DetailPageActivity.this.finish();
                } else {
                    Plant plant = plants.get(0);
                    System.out.println("[Plant] " + plant.getId());
                    plantDroidViewModel.getDiscoveredPlantsByPlantID(plant.getId()).observe(DetailPageActivity.this, new Observer<List<DiscoveredPlant>>() {
                        @Override
                        public void onChanged(List<DiscoveredPlant> discoveredPlants) {
                            System.out.println("[Discovery] " + String.valueOf(plant.getId()));
                            try {
                                setLocations(discoveredPlants);
                            } catch (IOException e) {
                                System.out.println("[IO Error] location io error.");
                                e.printStackTrace();
                            }
                        }
                    });
                    try {
                        setPlainInfo(plant);
                        setCommonNames(plant);
                        setTaxonomies(plant);
                        setEdibleParts(plant);
                        setPropagationMethods(plant);
                    } catch (JSONException e) {
                        System.out.println("[JSON Error] database information parse error.");
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setLongClick(TextView v) {
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                System.out.println("[Long Click]");
                return false;
            }
        });
    }

    private void setPlainInfo(Plant plant) {
        TextView plantName = findViewById(R.id.plantName);
        plantName.setText(plant.getName());
        // plantName
        setLongClick(plantName);

        TextView plantWikiLink = findViewById(R.id.plantWikiLink);
        plantWikiLink.setText(plant.getWiki() + " (click to jump)");
        plantWikiLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(plant.getWiki());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        TextView plantDesc = findViewById(R.id.plantDescription);
        plantDesc.setText(plant.getDescription());
        ImageView plantImg = findViewById(R.id.plantImg);
        LoadImageUtil.setImageView(plantImg, plant.getImg());
    }

    @SuppressLint("NewApi")
    private void setLocations(List<DiscoveredPlant> discoveries) throws IOException {
        // String[] locations = {"You have found this at XJTLU, 2021.10.03", "You have found this at Tian'an men, 2021.12.03"};
        LinearLayout locationsLayout = findViewById(R.id.plantLocationsLayout);
        FrameLayout.LayoutParams taxonomyLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        taxonomyLayoutParams.topMargin = 3;
        for (DiscoveredPlant discovery : discoveries) {
            String location = "";
            double longitude = discovery.getLongitude();
            double latitude = discovery.getLatitude();
            System.out.println("[Loaction] " + String.valueOf(longitude) + ", " + String.valueOf(latitude));

            String discoverTime = "???";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !discovery.getFoundTime().equals("0.000E9")) {
                BigDecimal bd;
                bd = new BigDecimal(discovery.getFoundTime());
                Date date = new Date(bd.longValue() * 1000L);
                Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                discoverTime = format.format(date);
            }

            if (latitude == 0 && longitude == 0)
                location = "Found at ??? , " + String.valueOf(discoverTime);
            else {
                Geocoder gc = new Geocoder(this, Locale.getDefault());
                List<Address> locationList = gc.getFromLocation(latitude, longitude, 1);
                if (locationList.size() == 0)
                    location = "Found at ???";
                else location = "Found at " + locationList.get(0).getAddressLine(0);
                location += ", " + discoverTime;
            }

            TextView textView = new TextView(this);
            textView.setText(location);
            textView.setLayoutParams(taxonomyLayoutParams);
            Resources resources = getResources();
            Drawable icon = resources.getDrawable(R.drawable.ic_baseline_location_on_18);
            textView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
            textView.setCompoundDrawablePadding(20);
            if (latitude != 0 || longitude != 0) {
                textView.setTextColor(getColor(R.color.green_light_3));
                textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DetailPageActivity.this, MapActivity.class);
                        intent.putExtra("discoverId",Integer.toString(discovery.getId()) );
                        startActivity(intent);
                    }
                });
            }
            locationsLayout.addView(textView);
        }
    }

    private void setCommonNames(Plant plant) throws JSONException {
        ArrayList<String> commonNames = new ArrayList<>();
        System.out.println(plant.getCommonNames());
        if (plant.getCommonNames() == null) commonNames.add("null");
        else {
            JSONArray commonNamesJSONArray = new JSONArray(plant.getCommonNames());
            // String[] commonNames = {"Lily-of-the-valley", "May bells", "Our Lady's tears", "Mary's tears", "muguet", "glovewort", "Apollinaris"}
            for (int i = 0; i < commonNamesJSONArray.length(); i++)
                commonNames.add(commonNamesJSONArray.getString(i));
        }
        LinearLayout commonNamesLayout = findViewById(R.id.plantCommonNamesLayout);
        setCardItems(commonNamesLayout, commonNames, 0);
    }

    private void setTaxonomies(Plant plant) {
        // String[] taxonomies = {"Kingdom: Plantae", "Phylum: Magnoliophyta", "Class: Magnoliopsida", "Order: Asparagales", "Family: Asparagaceae", "Genus: Convallaria"};
        ArrayList<String> taxonomies = new ArrayList<>();
        taxonomies.add("Kingdom: " + plant.getKingdom());
        taxonomies.add("Phylum: " + plant.getPhylum());
        taxonomies.add("Class: " + plant.getPlantClass());
        taxonomies.add("Order: " + plant.getOrder());
        taxonomies.add("Family: " + plant.getFamily());
        taxonomies.add("Genus: " + plant.getGenus());
        LinearLayout taxonomiesLayout = findViewById(R.id.plantTaxonomiesLayout);
        setCardItems(taxonomiesLayout, taxonomies, 0);
    }

    private void setEdibleParts(Plant plant) throws JSONException {
        // String[] edibleParts = {"flowers", "gum", "leaves", "seeds"};
        ArrayList<String> edibleParts = new ArrayList<>();
        if (plant.getEdibleParts() == null) edibleParts.add("null");
        else {
            JSONArray ediblePartsJSONArray = new JSONArray(plant.getEdibleParts());
            // String[] commonNames = {"Lily-of-the-valley", "May bells", "Our Lady's tears", "Mary's tears", "muguet", "glovewort", "Apollinaris"}
            for (int i = 0; i < ediblePartsJSONArray.length(); i++)
                edibleParts.add(ediblePartsJSONArray.getString(i));
        }
        LinearLayout ediblePartsLayout = findViewById(R.id.plantEdiblePartsLayout);
        setCardItems(ediblePartsLayout, edibleParts, 0);
    }

    private void setPropagationMethods(Plant plant) throws JSONException {
        // String[] propagationMethods = {"cuttings", "seeds"};
        ArrayList<String> propagationMethods = new ArrayList<>();
        if (plant.getPropagationMethods() == null) propagationMethods.add("null");
        else {
            JSONArray propagationMethodsSONArray = new JSONArray(plant.getPropagationMethods());
            // String[] commonNames = {"Lily-of-the-valley", "May bells", "Our Lady's tears", "Mary's tears", "muguet", "glovewort", "Apollinaris"}
            for (int i = 0; i < propagationMethodsSONArray.length(); i++)
                propagationMethods.add(propagationMethodsSONArray.getString(i));
        }
        LinearLayout propagationMethodsLayout = findViewById(R.id.plantPropagationMethodsLayout);
        setCardItems(propagationMethodsLayout, propagationMethods, 0);
    }

    private void setCardItems(LinearLayout layout, ArrayList<String> items, int iconType) {
        FrameLayout.LayoutParams taxonomyLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        taxonomyLayoutParams.topMargin = 3;
        for (String item : items) {
            System.out.println(item);
            TextView textView = new TextView(this);
            textView.setText(item);
            textView.setLayoutParams(taxonomyLayoutParams);
            Resources resources = getResources();
            Drawable icon = resources.getDrawable(R.drawable.ic_baseline_brightness_1_8);
            if (iconType == 1)
                icon = resources.getDrawable(R.drawable.ic_baseline_location_on_18);
            textView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
            textView.setCompoundDrawablePadding(20);
            layout.addView(textView);
        }
    }

}