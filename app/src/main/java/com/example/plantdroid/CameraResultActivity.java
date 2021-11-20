package com.example.plantdroid;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.plantdroid.Database.DiscoveredPlant;
import com.example.plantdroid.Database.Plant;
import com.example.plantdroid.Database.PlantDroidViewModel;
import com.example.plantdroid.ui.camera.CameraFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import util.LoadImage;

public class CameraResultActivity extends AppCompatActivity {

    String plantsStr = "{\"id\": 32741014, \"custom_id\": null, \"meta_data\": {\"latitude\": null, \"longitude\": null, \"date\": \"2021-11-18\", \"datetime\": \"2021-11-18\"}, \"uploaded_datetime\": 1637244471.670826, \"finished_datetime\": 1637244473.744761, \"images\": [{\"file_name\": \"83cb5655708c41958c504d34973de18f.jpg\", \"url\": \"https://plant.id/media/images/83cb5655708c41958c504d34973de18f.jpg\"}], \"suggestions\": [{\"id\": 204965593, \"plant_name\": \"Schefflera arboricola\", \"plant_details\": {\"common_names\": [\"dwarf umbrella tree\"], \"url\": \"https://en.wikipedia.org/wiki/Schefflera_arboricola\", \"name_authority\": \"Schefflera arboricola (Hayata) Merr.\", \"wiki_description\": {\"value\": \"Schefflera arboricola is a flowering plant in the family Araliaceae, native to Taiwan as well as Hainan. Its common name is dwarf umbrella tree, as it appears to be a smaller version of the umbrella tree, Schefflera actinophylla.\", \"citation\": \"https://en.wikipedia.org/wiki/Schefflera_arboricola\", \"license_name\": \"CC BY-SA 3.0\", \"license_url\": \"https://cr\n" +
            "    eativecommons.org/licenses/by-sa/3.0/\"}, \"taxonomy\": {\"class\": \"Magnoliopsida\", \"family\": \"Araliaceae\", \"genus\": \"Schefflera\", \"kingdom\": \"Plantae\", \"order\": \"Apiales\", \"phylum\": \"Magnoliophyta\"}, \"synonyms\": [\"Heptapleurum arboricola\"], \"scientific_name\": \"Schefflera arboricola\", \"structured_name\": {\"genus\": \"schefflera\", \"species\": \"arboricola\"}}, \"probability\": 0.0430371817516584, \"confirmed\": false, \"similar_images\": [{\"id\": \"abed54c4b55933ebd96b92a107b77056\", \"similarity\": 0.6702009502909487, \"url\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/abe/d54c4b55933ebd96b92a107b77056.jpg\", \"url_small\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/abe/d54c4b55933ebd96b92a107b77056.small.jpg\"}, {\"id\": \"9e33f9cfcb63310e993604b9e6dcd9cc\", \"similarity\": 0.08216331434740487, \"url\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/9e3/3f9cfcb63310e993604b9e6dcd9cc.jpg\", \"url_small\": \"https://plant-id.ams3.cdn.digitaloc\n" +
            "    eanspaces.com/similar_images/images/9e3/3f9cfcb63310e993604b9e6dcd9cc.small.jpg\"}]}, {\"id\": 204965595, \"plant_name\": \"Panax quinquefolius\", \"plant_details\": {\"common_names\": [\"American ginseng\", \"Panacis quinquefolis\"], \"url\": \"https://en.wikipedia.org/wiki/Panax_quinquefolius\", \"name_authority\": \"Panax quinquefolius L.\", \"wiki_description\": {\"value\": \"American ginseng (Panax quinquefolius, Panacis quinquefolis) is a herbaceous perennial plant in the ivy family, commonly used as an herb in traditional medicine, including traditional Chinese Medicine. It is native to eastern North America, though it is also cultivated in China. Since the 18th century, American ginseng (P. quinquefolius) has been primarily exported to Asia, where it is highly valued for its cooling and sedative medicinal effects. It is considered to represent the cooling yin qualities, while Asian ginseng embodies the warmer aspects of yang.\", \"citation\": \"https://en.wikipedia.org/wiki/Panax_quinquefolius\", \"license_name\n" +
            "    \": \"CC BY-SA 3.0\", \"license_url\": \"https://creativecommons.org/licenses/by-sa/3.0/\"}, \"taxonomy\": {\"class\": \"Magnoliopsida\", \"family\": \"Araliaceae\", \"genus\": \"Panax\", \"kingdom\": \"Plantae\", \"order\": \"Apiales\", \"phylum\": \"Magnoliophyta\"}, \"synonyms\": [\"Aralia quinquefolia\", \"Ginseng quinquefolium\", \"Panax americanus\", \"Panax americanus var. elatus\", \"Panax americanus var. obovatus\", \"Panax quinquefolius var. americanus\", \"Panax quinquefolius var. obovatus\"], \"scientific_name\": \"Panax quinquefolius\", \"structured_name\": {\"genus\": \"panax\", \"species\": \"quinquefolius\"}}, \"probability\": 0.03441981621069197, \"confirmed\": false, \"similar_images\": [{\"id\": \"7dea9d2c025d122e4173204a3a5c535e\", \"similarity\": 0.09582642565533385, \"url\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/7de/a9d2c025d122e4173204a3a5c535e.jpg\", \"url_small\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/7de/a9d2c025d122e4173204a3a5c535e.small.jpg\", \"citation\": \"Lafitau, Jose\n" +
            "    ph Fran\\u00e7ois, 1681-1746; Orl\\u00e9ans, Philippe, duc d', 1674-1723;\", \"license_name\": \"CC0\", \"license_url\": \"https://creativecommons.org/publicdomain/zero/1.0/\"}, {\"id\": \"c1ce982f63c1a6688997bd94b4c20869\", \"similarity\": 0.0663259571066821, \"url\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/c1c/e982f63c1a6688997bd94b4c20869.jpg\", \"url_small\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/c1c/e982f63c1a6688997bd94b4c20869.small.jpg\", \"citation\": \"John Carl Jacobs\", \"license_name\": \"CC BY-SA 4.0\", \"license_url\": \"https://creativecommons.org/licenses/by-sa/4.0/\"}]}, {\"id\": 204965598, \"plant_name\": \"Callistosporium luteo-olivaceum\", \"plant_details\": {\"common_names\": null, \"url\": \"https://en.wikipedia.org/wiki/Callistosporium_luteo-olivaceum\", \"name_authority\": \"Agaricus luteo-olivaceus Berk. & M.A. Curtis, 1859\", \"wiki_description\": {\"value\": \"Callistosporium luteo-olivaceum is a species of agaric fungus in the family Tricholomatacea\n" +
            "    e. It was originally described in 1859 as Agaricus luteo-olivaceus by Miles Joseph Berkeley and Moses Ashley Curtis in 1859. Rolf Singer transferred it to Callistosporium in 1946. The fungus has an extensive synonymy. Although rare, C. luteo-olivaceum is widely distributed in temperate and tropical areas of Europe and North America. In 2014, it was reported growing in pine forests in Western Himalaya, Pakistan.\", \"citation\": \"https://en.wikipedia.org/wiki/Callistosporium_luteo-olivaceum\", \"license_name\": \"CC BY-SA 3.0\", \"license_url\": \"https://creativecommons.org/licenses/by-sa/3.0/\"}, \"taxonomy\": {\"class\": \"Agaricomycetes\", \"family\": \"Callistosporiaceae\", \"genus\": \"Callistosporium\", \"kingdom\": \"Fungi\", \"order\": \"Agaricales\", \"phylum\": \"Basidiomycota\"}, \"synonyms\": [\"Agaricus luteo-olivaceus\"], \"scientific_name\": \"Callistosporium luteo-olivaceum\", \"structured_name\": {\"genus\": \"callistosporium\", \"species\": \"luteo-olivaceum\"}}, \"probability\": 0.02709813583646778, \"confirmed\": false, \"sim\n" +
            "    ilar_images\": [{\"id\": \"8ded4935090340af092399b5c86a4c30\", \"similarity\": 0.035681397321806384, \"url\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/8de/d4935090340af092399b5c86a4c30.jpg\", \"url_small\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/8de/d4935090340af092399b5c86a4c30.small.jpg\", \"citation\": \"garmonb0zia\", \"license_name\": \"CC0\", \"license_url\": \"https://creativecommons.org/publicdomain/zero/1.0/\"}, {\"id\": \"841c9842f8e10cbaef57169d6970d029\", \"similarity\": 0.029975818204760328, \"url\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/841/c9842f8e10cbaef57169d6970d029.jpg\", \"url_small\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/841/c9842f8e10cbaef57169d6970d029.small.jpg\", \"citation\": \"Dick Culbert\", \"license_name\": \"CC BY 2.0\", \"license_url\": \"https://creativecommons.org/licenses/by/2.0/\"}]}, {\"id\": 204965599, \"plant_name\": \"Acer\", \"plant_details\": {\"common_names\": [\"Maple\"], \"url\": \"https://en.wikipedia.org/wiki/Acer_(plant)\", \"name_authority\": \"Acer L.\", \"wiki_description\": {\"value\": \"Acer is a genus of trees and shrubs commonly known as maples. The genus is placed in the family Sapindaceae. There are approximately 128 species, most of which are native to Asia, with a number also appearing in Europe, northern Africa, and North America. Only one species, Acer laurinum, extends to the Southern Hemisphere. The type species of the genus is the sycamore maple, Acer pseudoplatanus, the most common maple species in Europe. The maples usually have easily recognizable palmate leaves (Acer negundo is an exception) and distinctive winged fruits. The closest relatives of the maples are the horse chestnuts. Maple syrup is made from the sap of some maple species.\", \"citation\": \"https://en.wikipedia.org/wiki/Acer_(plant)\", \"license_name\": \"CC BY-SA 3.0\", \"license_url\": \"https://creativecommons.org/licenses/by-sa/3.0/\"}, \"taxonomy\": {\"class\": \"Magnoliopsida\", \"family\": \n" +
            "    \"Sapindaceae\", \"genus\": \"Acer\", \"kingdom\": \"Plantae\", \"order\": \"Sapindales\", \"phylum\": \"Magnoliophyta\"}, \"synonyms\": [\"Argentacer\", \"Crula\", \"Euacer\", \"Negundium\", \"Negundo\", \"Rufacer\", \"Rulac\", \"Saccharodendron\", \"Sacchrosphendamnus\"], \"scientific_name\": \"Acer\", \"structured_name\": {\"genus\": \"acer\"}}, \"probability\": 0.02066775744890652, \"confirmed\": false, \"similar_images\": [{\"id\": \"4f85542fe68eb5d83285bb8f07ccfb49\", \"similarity\": 0.2590887068062426, \"url\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/4f8/5542fe68eb5d83285bb8f07ccfb49.jpg\", \"url_small\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/4f8/5542fe68eb5d83285bb8f07ccfb49.small.jpg\"}, {\"id\": \"e97ef1b7c37baac30b14d35006063d2b\", \"similarity\": 0.1423633154863739, \"url\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/e97/ef1b7c37baac30b14d35006063d2b.jpg\", \"url_small\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/e97/ef1b7c37baac30b14d35006063d2b.small.jpg\"}]}, {\"id\": 204965600, \"plant_name\": \"Ambrosia artemisiifolia\", \"plant_details\": {\"common_names\": [\"common ragweed\", \"annual ragweed\", \"low ragweed\"], \"url\": \"https://en.wikipedia.org/wiki/Ambrosia_artemisiifolia\", \"name_authority\": \"Ambrosia artemisiifolia L.\", \"wiki_description\": {\"value\": \"Ambrosia artemisiifolia, with the common names common ragweed, annual ragweed, and low ragweed, is a species of the genus Ambrosia native to regions of the Americas.\", \"citation\": \"https://en.wikipedia.org/wiki/Ambrosia_artemisiifolia\", \"license_name\": \"CC BY-SA 3.0\", \"license_url\": \"https://creativecommons.org/licenses/by-sa/3.0/\"}, \"taxonomy\": {\"class\": \"Magnoliopsida\", \"family\": \"Asteraceae\", \"genus\": \"Ambrosia\", \"kingdom\": \"Plantae\", \"order\": \"Asterales\", \"phylum\": \"Magnoliophyta\"}, \"synonyms\": [\"Ambrosia artemisiifolia f. gracilissima\", \"Ambrosia artemisiifolia f. villosa\", \"Ambrosia artemisiifolia subsp. diversifolia\", \"Ambrosia artemisiifolia var. elatior\", \"Ambrosia artemisiifolia var. jamaicensis\", \"Ambrosia artemisiifolia var. octocornis\", \"Ambrosia artemisiifolia var. paniculata\", \"Ambrosia artemisiifolia var. quadricornis\", \"Ambrosia chilensis\", \"Ambrosia diversifolia\", \"Ambrosia elata\", \"Ambrosia elatior\", \"Ambrosia elatior f. aurea\", \"Ambrosia glandulosa\", \"Ambrosia monophylla\", \"Ambrosia paniculata\", \"Ambrosia senegalensis\", \"Ambrosia umbellata\", \"Iva monophylla\"], \"scientific_name\": \"Ambrosia artemisiifolia\", \"structured_name\": {\"genus\": \"ambrosia\", \"species\": \"artemisiifolia\"}}, \"probability\": 0.018327029549802823, \"confirmed\": false, \"similar_images\": [{\"id\": \"419741f6bbea96677e9b4a7776772d51\", \"similarity\": 0.032119438580803975, \"url\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/419/741f6bbea96677e9b4a7776772d51.jpg\", \"url_small\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/419/741f6bbea96677e9b4a7776772d51.small.jpg\"}, {\"id\": \"4823efad2e730b51a7b486d18ce9a24b\", \"similarity\n" +
            "    \": 0.023242592530509615, \"url\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/482/3efad2e730b51a7b486d18ce9a24b.jpg\", \"url_small\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/482/3efad2e730b51a7b486d18ce9a24b.small.jpg\"}]}, {\"id\": 204965601, \"plant_name\": \"Acer saccharum\", \"plant_details\": {\"common_names\": [\"sugar maple\"], \"url\": \"https://en.wikipedia.org/wiki/Acer_saccharum\", \"name_authority\": \"Acer saccharum Marshall\", \"wiki_description\": {\"value\": \"Acer saccharum, the sugar maple, is a species of flowering plant in the soapberry and lychee family Sapindaceae. It is native to the hardwood forests of eastern Canada, from Nova Scotia west through southern Quebec, central and southern Ontario to southeastern Manitoba around Lake of the Woods, and northcentral and northeastern United States, from Minnesota eastward to Maine and southward to northern Virginia, Tennessee and Missouri. Sugar maple is best known for being the primary sour\n" +
            "    ce of maple syrup and for its brightly colored fall foliage. It may also be known as \\\"rock maple\\\", \\\"sugar tree\\\", \\\"birds-eye maple\\\", \\\"sweet maple\\\", \\\"curly maple\\\", or \\\"hard maple\\\", particularly when referring to the wood.\", \"citation\": \"https://en.wikipedia.org/wiki/Acer_saccharum\", \"license_name\": \"CC BY-SA 3.0\", \"license_url\": \"https://creativecommons.org/licenses/by-sa/3.0/\"}, \"taxonomy\": {\"class\": \"Magnoliopsida\", \"family\": \"Sapindaceae\", \"genus\": \"Acer\", \"kingdom\": \"Plantae\", \"order\": \"Sapindales\", \"phylum\": \"Magnoliophyta\"}, \"synonyms\": [\"Acer saccharinum var. glaucum\", \"Acer barbatum f. commune\", \"Acer hispidum\", \"Acer nigrum subsp. saccharophorum\", \"Acer nigrum var. glaucum\", \"Acer palmifolium\", \"Acer palmifolium f. euconcolor\", \"Acer palmifolium f. glabratum\", \"Acer palmifolium f. integrilobum\", \"Acer palmifolium var. concolor\", \"Acer palmifolium var. glaucum\", \"Acer saccharinum var. viride\", \"Acer saccharophorum\", \"Acer saccharophorum f. angustilobatum\", \"Acer sacch\n" +
            "    arophorum f. conicum\", \"Acer saccharophorum f. glaucum\", \"Acer saccharophorum var. subvestitum\", \"Acer saccharum f. angustilobatum\", \"Acer saccharum f. conicum\", \"Acer saccharum f. euconcolor\", \"Acer saccharum f. glabratum\", \"Acer saccharum f. hispidum\", \"Acer saccharum f. integrilobum\", \"Acer saccharum f. pubescens\", \"Acer saccharum f. rubrocarpum\", \"Acer saccharum f. subvestitum\", \"Acer saccharum f. truncatum\", \"Acer saccharum f. villipes\", \"Acer saccharum f. villosum\", \"Acer saccharum var. quinquelobulatum\", \"Acer saccharum var. viride\", \"Acer subglaucum\", \"Acer subglaucum var. sinuosum\", \"Acer treleaseanum\", \"Saccharodendron hispidum\", \"Saccharodendron saccharum\"], \"scientific_name\": \"Acer saccharum\", \"structured_name\": {\"genus\": \"acer\", \"species\": \"saccharum\"}}, \"probability\": 0.013896970030333846, \"confirmed\": false, \"similar_images\": [{\"id\": \"39eb6ff2e076154121841cafc74fa8df\", \"similarity\": 0.608939381086905, \"url\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_image\n" +
            "    s/images/39e/b6ff2e076154121841cafc74fa8df.jpg\", \"url_small\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/39e/b6ff2e076154121841cafc74fa8df.small.jpg\"}, {\"id\": \"584081db09ae1ca0127e2faa79d440fe\", \"similarity\": 0.09214662071630311, \"url\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/584/081db09ae1ca0127e2faa79d440fe.jpg\", \"url_small\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/584/081db09ae1ca0127e2faa79d440fe.small.jpg\"}]}], \"modifiers\": [\"crops_fast\", \"similar_images\"], \"secret\": \"uq01yaGDx1xsBBt\", \"fail_cause\": null, \"countable\": true, \"feedback\": null, \"is_plant_probability\": 0.07250318489999999, \"is_plant\": false}ific_name\": \"Acer saccharum\", \"structured_name\": {\"genus\": \"acer\", \"species\": \"saccharum\"}}, \"probability\": 0.013896970030333846, \"confirmed\": false, \"similar_images\": [{\"id\": \"39eb6ff2e076154121841cafc74fa8df\", \"similarity\": 0.608939381086905, \"url\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/39e/b6ff2e076154121841cafc74fa8df.jpg\", \"url_small\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/images/39e/b6ff2e076154121841cafc74fa8df.small.jpg\"}, {\"id\": \"584081db09ae1ca0127e2faa79d440fe\", \"similarity\": 0.09214662071630311, \"url\": \"https://plant-id.ams3.cdn.digitaloceanspaces.com/similar_images/image";

    ArrayList<CardView> cardViews = new ArrayList<>();
    ArrayList<String> plantPros = new ArrayList<>();
    ArrayList<String> plantNames = new ArrayList<>();
    ArrayList<String> plantDescs = new ArrayList<>();
    ArrayList<String> plantWikiUrls = new ArrayList<>();

    int selectId = 0, recordId = -1;
    int DP15, DP90, DP140;

    PlantDroidViewModel plantDroidViewModel;


    private int getPixelsFromDp(int size) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_result);

        DP15 = getPixelsFromDp(15);
        DP90 = getPixelsFromDp(90);
        DP140 = getPixelsFromDp(150);

        plantDroidViewModel = ViewModelProviders.of(this).get(PlantDroidViewModel.class);

        // get screen width
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;

        // set result image to 4:3
        ImageView resultImgLayout = findViewById(R.id.resultImg);
        ConstraintLayout.LayoutParams resultImgLayoutParams = (ConstraintLayout.LayoutParams) resultImgLayout.getLayoutParams();
        resultImgLayoutParams.height = screenWidth * 3 / 4;
        resultImgLayoutParams.width = screenWidth;
        resultImgLayout.setLayoutParams(resultImgLayoutParams);

        // set plant name width
        TextView candidatePlantName = findViewById(R.id.candidatePlantName);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) candidatePlantName.getLayoutParams();
        layoutParams.width = screenWidth - DP140;
        candidatePlantName.setLayoutParams(layoutParams);

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
                        addRecordToDB(plantJson);
                        showDialog();
                    } else
                        recordId = -1;
                    setRecordBtn();
                }
            });
        } catch (JSONException e) {
            System.out.println("[JSON Error] result JSON parse failed.");
            e.printStackTrace();
        }
    }

    public void showDialog() {
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
        dialogbuilder.setTitle("Record success");
        dialogbuilder.setMessage("Record this recognition result success! Go to the detail page to see more information about this plant now?");
        dialogbuilder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CameraResultActivity.this, DetailPageActivity.class);
                // intent.putExtra(MESSAGE_KEY,message);
                startActivity(intent);
            }});
        dialogbuilder.setNeutralButton("Back to camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CameraResultActivity.this, BottomNavigationActivity.class);
                // intent.putExtra(MESSAGE_KEY,message);
                startActivity(intent);
        }});
        dialogbuilder.setNegativeButton("Stay", null);
        final AlertDialog alertdialog1 = dialogbuilder.create();
        alertdialog1.show();
    }

    public void initializePlantInfos(JSONArray plantsJsonArray) {
        for (int i = 0; i < plantsJsonArray.length(); i++) {
            try {
                JSONObject plantJson = (JSONObject) plantsJsonArray.getJSONObject(i);
                JSONObject plantDetailJson = plantJson.getJSONObject("plant_details");
                JSONObject plantWikiDescJson = plantDetailJson.getJSONObject("wiki_description");

                String plantProbabilityStr = plantJson.getString("probability");
                float plantProbability = ((float) (int) (10000 * Float.parseFloat(plantProbabilityStr))) / 100;
                plantProbabilityStr = Float.toString(plantProbability) + "%";
                String plantNameStr = plantJson.getString("plant_name");
                String plantUrl = plantDetailJson.getString("url");
                String plantDescStr = plantWikiDescJson.getString("value");

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
            LoadImage.setImageView(resultImg, fileUrl);
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
        LoadImage.setImageView(plantImgView, plantImgUrl);
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

    public void addRecordToDB(JSONObject resultJSON) {
        try {
            JSONObject plantJSON = resultJSON.getJSONArray("suggestions").getJSONObject(recordId);
            String plantName = plantJSON.getString("plant_name");
            JSONObject plantDetails = plantJSON.getJSONObject("plant_details");
            String plantDesc = plantDetails.getJSONObject("wiki_description").getString("value");
            String plantImg = null;
            try {
                plantImg = plantDetails.getJSONObject("wiki_image").getString("value");
            } catch (JSONException e) {}
            String edibleParts = null;
            try {
                edibleParts = plantDetails.getString("edible_parts");
            } catch (JSONException e) {}
            String propagationMethods = null;
            try {
                propagationMethods = plantDetails.getString("propagation_methods");
            } catch (JSONException e) {}
            JSONObject taxonomy = plantDetails.getJSONObject("taxonomy");

            Plant plant = new Plant(plantName, plantDetails.getString("common_names"),
                    taxonomy.getString("kingdom"), taxonomy.getString("phylum"), taxonomy.getString("class"),
                    taxonomy.getString("order"), taxonomy.getString("family"), taxonomy.getString("genus"), plantDesc, plantImg,
                    edibleParts, propagationMethods, plantDetails.getString("url"), true);
            // DiscoveredPlant discovery = new DiscoveredPlant(plantJSON.getString("uploaded_datetime"), "", "", "", "");
//            PlantDroidViewModel plantDroidViewModel = ViewModelProviders.of(this).get(PlantDroidViewModel.class);
            // plantDroidViewModel.deleteAllPlants();

            plantDroidViewModel.getAllPlantsLive().observe(this, plants -> {

                System.out.println(plants.size());
                if (plants.isEmpty()) {
                    System.out.println("[Is Empty]");
                    plantDroidViewModel.insertPlants(plant);
                    System.out.println("[Insert Finish]");
                    // plantDroidViewModel.insertDiscoveredPlants();
                }
                else {
                    System.out.println("[Not Empty]");
                }
            });


        } catch (JSONException e) {
            System.out.println("[JSON Error] database error");
            e.printStackTrace();
        }
        System.out.println("[Add Success]");
    }
}