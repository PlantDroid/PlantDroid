package com.example.plantdroid;

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
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
//import com.yalantis.ucrop.UCrop;
import com.tbruyelle.rxpermissions2.RxPermissions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

import kotlin.jvm.internal.Intrinsics;
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
     * 动态权限管理
     */
    private RxPermissions rxPermissions;
    ImageView imgFavorite;
    private File outputImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        ivPicture = findViewById(R.id.iv_picture);

        rxPermissions = new RxPermissions(this);
    }



    private static String base64EncodeFromFile(String fileString) throws Exception {
        File file = new File(fileString);

        FileInputStream fis = new FileInputStream(file);

        String res = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            res = Base64.getEncoder().encodeToString(FileUtil.readFileByBytes(fileString));
        }
        fis.close();
        return res;
    }

    /**
     * 图像识别请求
     * <p>
     * //     * @param token       token
     *
     * @param imageBase64 图片Base64
     *                    //* @param imgUrl      网络图片Url
     */
    private void ImageDiscern(String imageBase64) throws Exception {
        String apiKey = "ojlt9sSkvTjiugRGANYWXD1JQ035ttwx5oUILTL4rSYVGbzzN2";
        JSONObject data = new JSONObject();
        data.put("api_key", apiKey);

        JSONArray images = new JSONArray();
        images.put(imageBase64);
        data.put("images", images);

        // add modifiers
        JSONArray modifiers = new JSONArray()
                .put("crops_fast")
                .put("similar_images");
        System.out.println("modifiers" + modifiers);
        data.put("modifiers", modifiers);
        data.put("plant_language", "en");

        // add details
        JSONArray plantDetails = new JSONArray()
                .put("common_names")
                .put("url")
                .put("name_authority")
                .put("wiki_description")
                .put("taxonomy")
                .put("synonyms");
        data.put("plant_details", plantDetails);
        sendPostRequest("https://api.plant.id/v2/identify", data);
    }

    public static void sendPostRequest(String urlString, JSONObject data) throws Exception {

        Thread t;
        URL url = new URL(urlString);
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    con.setDoOutput(true);//允許輸入流，即允許下載
                    con.setDoInput(true);//允許輸出流，即允許上傳
                    con.setUseCaches(false); //設置是否使用緩存
                    con.setConnectTimeout(30000);
                    con.setReadTimeout(30000);
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-type", "application/x-javascript->json");
                    con.addRequestProperty("Charset", "UTF-8");
                    // System.out.println("[Connection] " + con.toString());
                    con.connect();
                    // System.out.println("[Connection] " + con.getRequestMethod());
                    // System.out.println("[Connection] is connected.");
                    OutputStream os = con.getOutputStream();
                    os.write(data.toString().getBytes());
                    os.flush();
                    os.close();
                    // System.out.println("[Output Stream] finished.");
                    System.out.println("[Response code] " + con.getResponseCode());

                    // System.out.println("[Connection] " + con.getRequestMethod());
                    InputStream inputStream = con.getInputStream();
                    // System.out.println("[Input Stream] get.");
                    byte[] data = new byte[1024];
                    StringBuffer sb1 = new StringBuffer();
                    int length = 0;
                    while ((length = inputStream.read(data))!=-1){
                        String s=new String(data, Charset.forName("utf-8"));
                        sb1.append(s);
                    }
                    String response = sb1.toString();
                    System.out.println("[Response] " + response);
                    con.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        return;
    }

    /**
     * 识别拍照图片
     *
     * @param view
     */
    @SuppressLint("CheckResult")
    public void IdentifyTakePhotoImage(View view) {
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           rxPermissions
                   .request(Manifest.permission.CAMERA)
                   .subscribe(granted -> {
                       if (granted) {
                           //获得权限
                           turnOnCamera();
                       } else {
                           showMsg("未获取到权限");
                       }
                   });
       } else {
        turnOnCamera();
        }
    }

    /**
     * 识别相册图片
     *
     * @param view
     */
    @SuppressLint("CheckResult")
    public void IdentifyAlbumPictures(View view) {
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           rxPermissions
                   .request(
                   Manifest.permission.READ_EXTERNAL_STORAGE,
                   Manifest.permission.WRITE_EXTERNAL_STORAGE)
                   .subscribe(granted -> {
                       if (granted) {
                           //获得权限
                           openAlbum();
                       } else {
                           showMsg("未获取到权限");
                       }
                   });
       } else {
        openAlbum();
        }
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
        // 创建File对象,获取文件的ContentURI
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
                try {
                    localImageDiscern(imagePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == TAKE_PHOTO_CODE) {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                //拍照返回
                String imagePath = outputImage.getAbsolutePath();
                //识别
                try {
                    localImageDiscern(imagePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            showMsg("没有上传图片呦");
        }
    }


    /**
     * 本地图片识别
     */

    private void localImageDiscern(String imagePath) throws Exception {
        try {
            //String token = getAccessToken();
            System.out.println("imagePath" + imagePath);
            //通过图片路径显示图片
            Glide.with(this).load(imagePath).into(ivPicture);
            //按字节读取文件
            //byte[] imgData = FileUtil.readFileByBytes(imagePath);
            //字节转Base64
            String imageBase64 = base64EncodeFromFile(imagePath);
            //String imageBase64 = Base64Util.encode(imgData);
            //图像识别
            ImageDiscern(imageBase64);
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