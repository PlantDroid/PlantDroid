package com.example.plantdroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantdroid.Database.DiscoveredPlant;
import com.example.plantdroid.Database.Plant;
import com.example.plantdroid.Database.PlantDroidViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import util.LoadImageUtil;

public class CameraResultActivity extends AppCompatActivity {

    String plantsStr = null;
    float accuracy = 0;
    float longitude = 0;
    float latitude = 0;
    String time = null;

    ArrayList<CardView> cardViews = new ArrayList<>();
    ArrayList<String> plantPros = new ArrayList<>();
    ArrayList<String> plantNames = new ArrayList<>();
    ArrayList<String> plantDescs = new ArrayList<>();
    ArrayList<String> plantWikiUrls = new ArrayList<>();

    int selectId = 0, recordId = -1;
    int DP15, DP90, DP150, DP180;

    private int getPixelsFromDp(int size) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_result);

        Intent intent = this.getIntent();
        accuracy = Float.parseFloat(intent.getStringExtra("accuracy"));
        System.out.println("[latitude]"+intent.getStringExtra("latitude"));
        if (intent.getStringExtra("latitude") != null)
            latitude = Float.parseFloat(intent.getStringExtra("latitude"));
        if (intent.getStringExtra("longitude") != null)
            longitude = Float.parseFloat(intent.getStringExtra("longitude"));
        plantsStr = intent.getStringExtra("response");
        time = intent.getStringExtra("time");

        DP15 = getPixelsFromDp(15);
        DP90 = getPixelsFromDp(90);
        DP150 = getPixelsFromDp(150);
        DP180 = getPixelsFromDp(260);

        System.out.println("[Response] " + plantsStr);
        System.out.println("[Accuracy] " + String.valueOf(accuracy));
        System.out.println("[Accuracy Origion] " + intent.getStringExtra("accuracy"));

        // get screen width
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        // set result image to 4:3
        ImageView resultImgLayout = findViewById(R.id.resultImg);
        ConstraintLayout.LayoutParams resultImgLayoutParams = (ConstraintLayout.LayoutParams) resultImgLayout.getLayoutParams();
        resultImgLayoutParams.height = screenWidth * 3 / 4;
        resultImgLayoutParams.width = screenWidth;
        resultImgLayout.setLayoutParams(resultImgLayoutParams);

        // set plant name width
        TextView candidatePlantName = findViewById(R.id.candidatePlantName);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) candidatePlantName.getLayoutParams();
        layoutParams.width = screenWidth - DP150;
        candidatePlantName.setLayoutParams(layoutParams);

        // set scroll height
        ScrollView sc = findViewById(R.id.plantDetailScrollView);
        ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) sc.getLayoutParams();
        layoutParams1.height = screenHeight - screenWidth * 3 / 4 - DP180;
        sc.setLayoutParams(layoutParams1);

        // initialize plants information
        try {
            JSONObject plantJson = new JSONObject(plantsStr);
            JSONArray plantsJsonArray = plantJson.getJSONArray("suggestions");
            JSONObject resultImgInfoJson = plantJson.getJSONArray("images").getJSONObject(0);
            // initialize information in arrays
            initializePlantInfos(plantsJsonArray);
            initializePlantImgCards(plantsJsonArray);

            // set information to ui
            setResultImg(resultImgInfoJson);
            setPlantInfo(0);
            selectCandidateCard(cardViews.get(0));
            setRecordBtn();

            // add record button on click listener
            Button recordBtn = findViewById(R.id.recordButton);
            recordBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recordId != selectId) {
                        recordId = selectId;
                        showConfirmDialog();
                    } else
                        recordId = -1;
                    setRecordBtn();
                }
            });
        } catch (JSONException e) {
            System.out.println("[JSON Error] result JSON parse failed.");
            e.printStackTrace();
            Toast.makeText(this, "Hard to recognize, please choose/take another photo.", Toast.LENGTH_LONG).show();
            this.finish();
        }

    }

    public void showConfirmDialog() {
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
        dialogbuilder.setTitle("Record Recognition");
        dialogbuilder.setMessage("Are you sure you want to record this plant as the recognition result?");
        dialogbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addRecordToDB();
                showNavigateDialog();
            }});
        dialogbuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recordId = -1;
                setRecordBtn();
            }});
        final AlertDialog alertdialog1 = dialogbuilder.create();
        alertdialog1.setCancelable(false);
        alertdialog1.show();
    }

    public void showNavigateDialog() {
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
        dialogbuilder.setTitle("Record Success!");
        dialogbuilder.setMessage("Go to the detail page to see more information about this plant or back to the camera page.");
        dialogbuilder.setPositiveButton("Go to Detail", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CameraResultActivity.this, DetailPageActivity.class);
                intent.putExtra("plantName", plantNames.get(recordId));
                startActivity(intent);
                CameraResultActivity.this.finish();
            }});
        dialogbuilder.setNeutralButton("Back to camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CameraResultActivity.this, BottomNavigationActivity.class);
                startActivity(intent);
                CameraResultActivity.this.finish();
            }});
        final AlertDialog alertdialog1 = dialogbuilder.create();
        alertdialog1.setCancelable(false);
        alertdialog1.show();
    }

    public void initializePlantInfos(JSONArray plantsJsonArray) {
        for (int i = 0; i < plantsJsonArray.length(); i++) {
            try {
                JSONObject plantJson = (JSONObject) plantsJsonArray.getJSONObject(i);
                JSONObject plantDetailJson = plantJson.getJSONObject("plant_details");

                String plantProbabilityStr = plantJson.getString("probability");
                float plantProbability = ((float) (int) (10000 * Float.parseFloat(plantProbabilityStr))) / 100;
                plantProbabilityStr = Float.toString(plantProbability) + "%";
                String plantNameStr = plantJson.getString("plant_name");
                String plantUrl = plantDetailJson.getString("url");
                String plantDescStr = null;
                try {
                    plantDescStr = plantDetailJson.getJSONObject("wiki_description").getString("value");
                } catch (JSONException e) {
                    System.out.println("[JSON Error] initialize plant infos.");
                }

                plantPros.add(plantProbabilityStr);
                plantNames.add(plantNameStr);
                plantDescs.add(plantDescStr);
                plantWikiUrls.add(plantUrl);
            } catch (JSONException e) {
                System.out.println("[JSON Error] inside JSON parse failed.");
                e.printStackTrace();
            }
        }
    }

    public void setResultImg(JSONObject imageInfoJson) {
        try {
            // String filePath = imageInfoJson.getString("file_name");
            String fileUrl = imageInfoJson.getString("url");
            ImageView resultImg = findViewById(R.id.resultImg);
            // Glide.with(this)
            //         .load(filePath)
            //         .placeholder(R.drawable.bluebell)
            //         .fitCenter()
            //         .into(resultImg);
            // System.out.println("[File url] " + fileUrl);
            LoadImageUtil.setImageView(resultImg, fileUrl);
        } catch (JSONException e) {
            System.out.println("[Load result image failed]");
            e.printStackTrace();
        }
    }

    public void setPlantInfo(Integer index) {
        TextView candidatePlantName = findViewById(R.id.candidatePlantName);
        TextView candidatePlantDescription = findViewById(R.id.candidatePlantDescription);
        TextView candidatePlantWiki = findViewById(R.id.candidatePlantWiki);
        TextView candidatePlantProbability = findViewById(R.id.candidatePlantProbability);
        candidatePlantName.setText(plantNames.get(index));
        candidatePlantDescription.setText("Description: " + plantDescs.get(index));
        candidatePlantWiki.setText(plantWikiUrls.get(index));
        candidatePlantProbability.setText("Similarity " + plantPros.get(index));

        candidatePlantWiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(plantWikiUrls.get(index));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        candidatePlantWiki.setTextColor(getColor(R.color.green_dark_1));
    }

    public void setRecordBtn() {
        Button recordBtn = findViewById(R.id.recordButton);
        if (selectId == recordId) {
            recordBtn.setText("RECORD");
            recordBtn.setBackgroundColor(Color.GRAY);
        } else {
            recordBtn.setText("CHOOSE");
            recordBtn.setBackgroundColor(getResources().getColor(R.color.green_light_3));
        }
    }

    public void initializePlantImgCards(JSONArray plantsJsonArray) {
        LinearLayout candidatePlantImgCardsLayout = findViewById(R.id.candidatePlantImgCardsLayout);
        for (int i = 0; i < plantsJsonArray.length(); i++) {
            try {
                JSONObject plantJson = (JSONObject) plantsJsonArray.getJSONObject(i);
                String plantImgUrl = plantJson.getJSONArray("similar_images").getJSONObject(0).getString("url");

                CardView cv = setPlantImgCard(plantImgUrl);
                candidatePlantImgCardsLayout.addView(cv);
                cv.setId(i);
                cardViews.add(cv);
            } catch (JSONException e) {
                System.out.println("[JSON Error] plantsJsonArray parse error.");
                e.printStackTrace();
            }
        }
    }

    public CardView setPlantImgCard(String plantImgUrl) {
        CardView plantImgCard = new CardView(this);
        ConstraintLayout.LayoutParams plantImgCardLayoutParams = new ConstraintLayout.LayoutParams(DP90, DP90);
        plantImgCardLayoutParams.setMargins(DP15, DP15, 0, DP15);
        plantImgCard.setLayoutParams(plantImgCardLayoutParams);
        plantImgCard.setRadius(DP90);

        plantImgCard.setClickable(true);
        plantImgCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCandidateCard((CardView) view);
                selectId = view.getId();
                setPlantInfo(view.getId());
                setRecordBtn();
            }
        });

        ImageView plantImgView = new ImageView(this);
        ConstraintLayout.LayoutParams candidatePlantImgLayoutParams = new ConstraintLayout.LayoutParams(DP90, DP90);
        // load network image
        LoadImageUtil.setImageView(plantImgView, plantImgUrl);
        plantImgView.setLayoutParams(candidatePlantImgLayoutParams);
        plantImgCard.addView(plantImgView);
        return plantImgCard;
    }

    public void selectCandidateCard(CardView selectCardView) {
        for (int i = 0; i < cardViews.size(); i++) {
            CardView cv = cardViews.get(i);
            if (cv.equals(selectCardView)) setCardSelected(cv, plantPros.get(i));
            else setCardUnselected(cv, plantPros.get(i));
        }
    }

    public void setCardSelected(CardView cv, String probability) {
        ImageView imageView = (ImageView) cv.getChildAt(0);
        cv.removeAllViews();
        cv.addView(imageView);

        ImageView selectMark = new ImageView(this);
        selectMark.setImageResource(R.drawable.ic_check);
        ConstraintLayout.LayoutParams selectMarkLayoutParams = new ConstraintLayout.LayoutParams(DP90, DP90);
        selectMark.setLayoutParams(selectMarkLayoutParams);
        cv.addView(selectMark);

        TextView candidatePlantProbabilityText = getProbabilityText(0, probability);
        cv.addView(candidatePlantProbabilityText);
    }

    public void setCardUnselected(CardView cv, String probability) {
        ImageView imageView = (ImageView) cv.getChildAt(0);
        cv.removeAllViews();
        cv.addView(imageView);

        TextView candidatePlantProbabilityText = getProbabilityText(1, probability);
        cv.addView(candidatePlantProbabilityText);
    }

    public TextView getProbabilityText(int type, String probability) {
        // type 0 - select, type 1 - unselect
        TextView tv = new TextView(this);
        ConstraintLayout.LayoutParams candidatePlantProbabilityLayoutParams = new ConstraintLayout.LayoutParams(DP90, DP90);
        tv.setLayoutParams(candidatePlantProbabilityLayoutParams);
        tv.setText(probability);
        if (type == 0) {
            tv.setTextColor(getResources().getColorStateList(R.color.white));
        } else if (type == 1) {
            tv.setTextColor(getResources().getColorStateList(R.color.black));
            tv.setShadowLayer(10, 2, 2, Color.WHITE);
        }
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(16);
        return tv;
    }

    public void addRecordToDB() {
        try {
            JSONObject resultJSON = new JSONObject(plantsStr);
            JSONObject plantJSON = resultJSON.getJSONArray("suggestions").getJSONObject(recordId);
            String plantName = plantJSON.getString("plant_name");
            JSONObject plantDetails = plantJSON.getJSONObject("plant_details");
            String plantDesc = plantDetails.getJSONObject("wiki_description").getString("value");
            String plantImg = null;
            try {
                // plantImg = plantJSON.getJSONArray("similar_images").getJSONObject(0).getString("url");
                plantImg = plantDetails.getJSONObject("wiki_image").getString("value");
            } catch (JSONException e) {
                plantImg = plantJSON.getJSONArray("similar_images").getJSONObject(0).getString("url");
            }
            System.out.println("[Plant Img] " + plantImg);
            String edibleParts = null;
            try {
                edibleParts = plantDetails.getString("edible_parts");
            } catch (JSONException e) {}
            String propagationMethods = null;
            try {
                propagationMethods = plantDetails.getString("propagation_methods");
            } catch (JSONException e) {}
            JSONObject taxonomy = plantDetails.getJSONObject("taxonomy");
            String discoverTime = time;
            // discoverTime = resultJSON.getString("uploaded_datetime");
            System.out.println("[DiscoverTime] " + discoverTime);
            Plant plant = new Plant(plantName, plantDetails.getString("common_names"),
                    taxonomy.getString("kingdom"), taxonomy.getString("phylum"), taxonomy.getString("class"),
                    taxonomy.getString("order"), taxonomy.getString("family"), taxonomy.getString("genus"), plantDesc, plantImg,
                    edibleParts, propagationMethods, plantDetails.getString("url"), true);
            PlantDroidViewModel plantDroidViewModel = ViewModelProviders.of(this).get(PlantDroidViewModel.class);
            DiscoveredPlant discovery = new DiscoveredPlant(discoverTime, latitude, longitude, accuracy, -1);

            // plantDroidViewModel.deleteAllPlants();
            plantDroidViewModel.getPlantByName(plantName).observe(this, new Observer<List<Plant>>() {
                @Override
                public void onChanged(List<Plant> plants) {
                    System.out.println(plants.size());
                    if (plants.isEmpty()) {
                        plantDroidViewModel.insertPlants(plant);
                        System.out.println("[Insert Finish] plant not exist, insert plant.");
                    }
                    else {
                        Plant p = plants.get(0);
                        discovery.setPlant_id(p.getId());
                        plantDroidViewModel.insertDiscoveredPlants(discovery);
                        System.out.println("[Insert Finish] plant exist, insert discovery");
                    }
                }
            });
        } catch (JSONException e) {
            System.out.println("[JSON Error] occur when insert database.");
            e.printStackTrace();
        }
    }
}