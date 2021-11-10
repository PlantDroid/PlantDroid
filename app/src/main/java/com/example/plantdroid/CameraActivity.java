package com.example.plantdroid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

//import network.ApiService;
import util.Base64Util;
import util.FileUtil;

public class CameraActivity extends AppCompatActivity {
    /**
     * 打开相册
     */
    private static final int OPEN_ALBUM_CODE = 100;
    /**
     * 打开相机
     */
    private static final int TAKE_PHOTO_CODE = 101;
    /**
     * 进度条
     */
    private ProgressBar pbLoading;
    /**
     * 显示图片
     */
    private ImageView ivPicture;

    /**
     * Api服务
     */
    //private ApiService service;
    ImageView imgFavorite;
    private File outputImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        ivPicture = findViewById(R.id.iv_picture);
        pbLoading = findViewById(R.id.pb_loading);
    }


    /**
     * 图像识别请求
     * <p>
     * //     * @param token       token
     *
     * @param imageBase64 图片Base64
     * @param imgUrl      网络图片Url
     */
    private void ImageDiscern(String imageBase64, String imgUrl) {
        String your_api_key = "fd3slj47dj... -- ask for one: https://web.plant.id/api-access-request/ --";
        JSONObject object = new JSONObject();
//        service.getDiscernResult(token, imageBase64, imgUrl).enqueue(new NetCallBack<GetDiscernResultResponse>() {
//            @Override
//            public void onSuccess(Call<GetDiscernResultResponse> call, Response<GetDiscernResultResponse> response) {
//                if(response.body() == null){
//                    showMsg("未获得相应的识别结果");
//                    return;
//                }
//                List<GetDiscernResultResponse.ResultBean> result = response.body().getResult();
//                if (result != null && result.size() > 0) {
//                    //显示识别结果
//                    showDiscernResult(result);
//                } else {
//                    pbLoading.setVisibility(View.GONE);
//                    showMsg("未获得相应的识别结果");
//                }
//            }
//
//            @Override
//            public void onFailed(String errorStr) {
//                pbLoading.setVisibility(View.GONE);
//                Log.e(TAG, "图像识别失败，失败原因：" + errorStr);
//            }
//        });
    }

    /**
     * 识别拍照图片
     *
     * @param view
     */
    @SuppressLint("CheckResult")
    public void IdentifyTakePhotoImage(View view) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            rxPermissions.request(
//                    Manifest.permission.CAMERA)
//                    .subscribe(grant -> {
//                        if (grant) {
//                            //获得权限
//                            turnOnCamera();
//                        } else {
//                            showMsg("未获取到权限");
//                        }
//                    });
//        } else {
        turnOnCamera();
        //}
    }

    /**
     * 识别相册图片
     *
     * @param view
     */
    @SuppressLint("CheckResult")
    public void IdentifyAlbumPictures(View view) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            rxPermissions.request(
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    .subscribe(grant -> {
//                        if (grant) {
//                            //获得权限
//                            openAlbum();
//                        } else {
//                            showMsg("未获取到权限");
//                        }
//                    });
//        } else {
        openAlbum();
        //}
    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_ALBUM_CODE);
    }

    /**
     * 打开相机
     */
    public void turnOnCamera() {
        //定义文件名称
        String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "takephoto" + ".jpg";
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()
                + "/" + filename;
        //创建File对象,获取文件的ContentURI
        outputImage = new File(path);
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(this, "com.example.plantdroid.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        //打开相机
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为MediaStore下的ACTION_IMAGE_CAPTURE
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//设置Extra标志为输出类型
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//授予临时权限
        startActivityForResult(intent, TAKE_PHOTO_CODE);
    }

    /**
     * Toast提示
     *
     * @param msg 内容
     */
    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            pbLoading.setVisibility(View.VISIBLE);
            if (requestCode == OPEN_ALBUM_CODE) {
                //打开相册返回
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                final Uri imageUri = Objects.requireNonNull(data).getData();
                Cursor cursor = getContentResolver().query(imageUri, filePathColumns, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
                //获取图片路径
                String imagePath = cursor.getString(columnIndex);
                cursor.close();
                //识别
                localImageDiscern(imagePath);

            } else if (requestCode == TAKE_PHOTO_CODE) {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
//                    Bitmap bp = (Bitmap) data.getExtras().get("data");
//                    imgFavorite.setImageBitmap(bp);
                //拍照返回
                String imagePath = outputImage.getAbsolutePath();
                //识别
                localImageDiscern(imagePath);
            }
        } else {
            showMsg("没有上传图片呦");
        }
    }

    /**
     * 本地图片识别
     */
    private void localImageDiscern(String imagePath) {
        try {
            //String token = getAccessToken();
            //通过图片路径显示图片
            Glide.with(this).load(imagePath).into(ivPicture);
            //按字节读取文件
            byte[] imgData = FileUtil.readFileByBytes(imagePath);
            //字节转Base64
            String imageBase64 = Base64Util.encode(imgData);
            System.out.println(imageBase64);
            //图像识别
            ImageDiscern(imageBase64, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

}