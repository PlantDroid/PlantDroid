package com.example.plantdroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.icu.math.BigDecimal;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantdroid.Database.DiscoveredPlant;
import com.example.plantdroid.Database.Plant;
import com.example.plantdroid.Database.PlantDroidViewModel;
import com.example.plantdroid.ui.dashboard.DashboardFragment;
import com.example.plantdroid.ui.notifications.NotificationsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import util.LoadImage;

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
            case R.id.media_route_menu_item://监听菜单按钮
                Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
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

    private void setPlainInfo(Plant plant) {
        TextView plantName = findViewById(R.id.plantName);
        plantName.setText(plant.getName());
        TextView plantDesc = findViewById(R.id.plantDescription);
        plantDesc.setText(plant.getDescription());
        ImageView plantImg = findViewById(R.id.plantImg);
        LoadImage.setImageView(plantImg, plant.getImg());
    }

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
                location = "Found at " + locationList.get(0).getAddressLine(0) + ", " + discoverTime;
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
                        intent.putExtra("discoverId", discovery.getId());
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