package com.example.plantdroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        setLocations();
        setCommonNames();
        setTaxonomies();
        setEdibleParts();
        setPropagationMethods();
    }

    private void setLocations() {
        String[] locations = {"You have found this at XJTLU, 2021.10.03", "You have found this at Tian'an men, 2021.12.03"};
        LinearLayout locationsLayout = findViewById(R.id.plantLocationsLayout);
        setCardItems(locationsLayout, locations, 1);
    }

    private void setCommonNames() {
        String[] commonNames = {"Lily-of-the-valley", "May bells", "Our Lady's tears", "Mary's tears", "muguet", "glovewort", "Apollinaris"};
        LinearLayout commonNamesLayout = findViewById(R.id.plantCommonNamesLayout);
        setCardItems(commonNamesLayout, commonNames, 0);
    }

    private void setTaxonomies() {
        String[] taxonomies = {"Kingdom: Plantae", "Phylum: Magnoliophyta", "Class: Magnoliopsida", "Order: Asparagales", "Family: Asparagaceae", "Genus: Convallaria"};
        LinearLayout taxonomiesLayout = findViewById(R.id.plantTaxonomiesLayout);
        setCardItems(taxonomiesLayout, taxonomies, 0);
    }

    private void setEdibleParts() {
        String[] edibleParts = {"flowers", "gum", "leaves", "seeds"};
        LinearLayout ediblePartsLayout = findViewById(R.id.plantEdiblePartsLayout);
        setCardItems(ediblePartsLayout, edibleParts, 0);
    }

    private void setPropagationMethods() {
        String[] propagationMethods = {"cuttings", "seeds"};
        LinearLayout propagationMethodsLayout = findViewById(R.id.plantPropagationMethodsLayout);
        setCardItems(propagationMethodsLayout, propagationMethods, 0);
    }

    private void setCardItems(LinearLayout layout, String[] items, int iconType) {
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