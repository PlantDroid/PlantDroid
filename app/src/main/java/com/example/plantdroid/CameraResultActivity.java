package com.example.plantdroid;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CameraResultActivity extends AppCompatActivity {

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


    }
}