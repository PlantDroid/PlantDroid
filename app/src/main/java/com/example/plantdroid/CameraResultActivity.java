package com.example.plantdroid;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantdroid.Database.Plant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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

    private int getPixelsFromDp(int size) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_result);

        // set result image to square
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;

        ImageView resultImgLayout = findViewById(R.id.resultImg);
        ConstraintLayout.LayoutParams resultImgLayoutParams = (ConstraintLayout.LayoutParams) resultImgLayout.getLayoutParams();
        resultImgLayoutParams.height = screenWidth * 3 / 4;
        resultImgLayoutParams.width = screenWidth;
        resultImgLayout.setLayoutParams(resultImgLayoutParams);

        // plants information initialize
        JSONArray plantsJsonArray = new JSONArray();
        try {
            plantsJsonArray = new JSONObject(plantsStr).getJSONArray("suggestions");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<Plant> plants = new ArrayList<>();
        Plant p1 = new Plant("Bluebell", "common names", "kingdom", "p", "c", "order", "genus",
                "Lily of the valley, Convallaria majalis (), sometimes written lily-of-the-valley, is a woodland flowering plant with sweetly scented, pendent, bell-shaped white flowers borne in sprays in spring. It is native throughout the cool temperate Northern Hemisphere in Asia and Europe. Due to its dense content of cardiac glycosides, it is highly poisonous if consumed by humans or domestic animals. Other names include May bells, Our Lady's tears, and Mary's tears. Its French name, muguet, sometimes appears in the names of perfumes imitating the flower's scent. In pre-modern England, the plant was known as glovewort (as it was a wort used to create a salve for sore hands), or Apollinaris (according to a legend that it was discovered by Apollo).",
                "img", "edible parts", "propagation methods", false);
        Plant p2 = new Plant("Chrysanthemum", "common names", "kingdom", "p", "c", "order", "genus",
                "Wild Chrysanthemum taxa are herbaceous perennial plants or subshrubs. They have alternately arrang\"edible parts\", \"propagation methods\", false);\n" +
                        "    }ed leaves divided into leaflets with toothed or occasionally smooth edges. The compound inflorescence is an array of several flower heads, or sometimes a solitary head. The head has a base covered in layers of phyllaries. The simple row of ray florets is white, yellow, or red; many horticultural specimens have been bred to bear many rows of ray florets in a great variety of colors. The disc florets of wild taxa are yellow. Pollen grains are appropriately 34 microns. The fruit is a ribbed achene.[8] Chrysanthemums start blooming early in the autumn. This is also known as the favorite flower for the month of November.[9]",
                "img", "edible parts", "propagation methods", false);
        Plant p3 = new Plant("Cherry Blossom", "common names", "kingdom", "p", "c", "order", "genus",
                "The botanical classification of cherry blossoms varies from period to period and from country to country. As of the 21st century, in the mainstream classification in Europe and North America, cherry trees for ornamental purposes are classified into the genus Prunus which consists of about 400 species. In the mainstream classification in Japan, China, and Russia, on the other hand, ornamental cherry trees are classified into the genus Cerasus, which consists of about 100 species separated from the genus Prunus, and the genus Cerasus does not include Prunus salicina, Prunus persica (Peach), Prunus mume, Prunus grayana, etc.[4] In Japan, the genus Prunus was the mainstream as in Europe and America until around 1992, but it was reclassified into the genus Cerasus in order to more accurately reflect the latest botanical situation of cherry blossoms. However, it is often classified into the genus Prunus for presentation in English-speaking countries. In general, cherry blossom (sakura) refers only to some of these about 100 species and the cultivars produced from them, and it does not refer to plum blossoms (梅, ume) which are similar to sakura.[4]\n" +
                        "In addition, since cherry trees are relatively prone to mutation and have a variety of flowers and trees, there are many varieties, such as variety which is a sub-classification of species, hybrids between species, and cultivar. For this reason, many researchers have named different scientific names for a particular type of cherry tree in different periods, and there is confusion in the classification of cherry trees.[14]",
                "img", "edible parts", "propagation methods", false);
        plants.add(p1);
        plants.add(p2);
        plants.add(p3);

        // information
        TextView candidatePlantName = findViewById(R.id.candidatePlantName);
        TextView candidatePlantDescription = findViewById(R.id.candidatePlantDescription);
        TextView candidatePlantWiki = findViewById(R.id.candidatePlantWiki);
        candidatePlantName.setText(plants.get(0).getName());
        candidatePlantDescription.setText(plants.get(0).getDescription());

        setCandidatePlantImgCards(plantsJsonArray);

    }

    public void setCandidatePlantImgCards(JSONArray plantsJsonArray) {
        LinearLayout candidatePlantImgCardsLayout = findViewById(R.id.candidatePlantImgCardsLayout);
        int index = 0;
        for (int i = 0; i < plantsJsonArray.length(); i++) {
            try {
                JSONObject plantJson = (JSONObject) plantsJsonArray.getJSONObject(i);
                CardView cv = setCandidatePlantImgCard(plantJson);
                candidatePlantImgCardsLayout.addView(cv);

                if (i == 0)
                    selectCandidateCard(cv);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public CardView setCandidatePlantImgCard(JSONObject plantJson) {
        System.out.println(plantJson);
        String plantImgUrl = "";
        try {
            plantImgUrl = plantJson.getJSONArray("similar_images").getJSONObject(0).getString("url");
            System.out.println("[plant image url] " + plantImgUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CardView candidatePlantImgCard = new CardView(this);
        ConstraintLayout.LayoutParams candidatePlantImgCardLayoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        candidatePlantImgCardLayoutParams.setMargins(getPixelsFromDp(15), 0, 0, 0);
        candidatePlantImgCard.setLayoutParams(candidatePlantImgCardLayoutParams);
        candidatePlantImgCard.setRadius(getPixelsFromDp(100));

        ImageView candidatePlantImg = new ImageView(this);
        ConstraintLayout.LayoutParams candidatePlantImgLayoutParams = new ConstraintLayout.LayoutParams(getPixelsFromDp(90), getPixelsFromDp(90));

        final String imgUrl = plantImgUrl;
        LoadImage.setImageView(candidatePlantImg, imgUrl);

        candidatePlantImg.setLayoutParams(candidatePlantImgLayoutParams);
        candidatePlantImgCard.addView(candidatePlantImg);

        return candidatePlantImgCard;
    }


    public void selectCandidateCard(CardView cv) {
        ImageView selectMark = new ImageView(this);
        selectMark.setImageResource(R.drawable.ic_check);
        ConstraintLayout.LayoutParams selectMarkLayoutParams = new ConstraintLayout.LayoutParams(getPixelsFromDp(90), getPixelsFromDp(90));
        selectMark.setLayoutParams(selectMarkLayoutParams);
        cv.addView(selectMark);

        TextView candidatePlantProbabilityText = new TextView(this);
        ConstraintLayout.LayoutParams candidatePlantProbabilityLayoutParams = new ConstraintLayout.LayoutParams(getPixelsFromDp(90), getPixelsFromDp(90));
        candidatePlantProbabilityText.setLayoutParams(candidatePlantProbabilityLayoutParams);
        candidatePlantProbabilityText.setText("88.6%");
        candidatePlantProbabilityText.setGravity(Gravity.CENTER);
        candidatePlantProbabilityText.setTextColor(getResources().getColorStateList(R.color.white));
        candidatePlantProbabilityText.setTextSize(16);
        cv.addView(candidatePlantProbabilityText);
    }
}