package com.example.plantdroid.ui.camera;

import static com.yalantis.ucrop.UCrop.REQUEST_CROP;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.plantdroid.CameraResultActivity;
import com.example.plantdroid.LoadingAnimatorView;
import com.example.plantdroid.R;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

import util.CacheUtil;
import util.FileUtil;
import util.LocationUtil;
import util.LogUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment {

    public CameraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CameraFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CameraFragment newInstance(String param1, String param2) {
        CameraFragment fragment = new CameraFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final LocationManager locationManager =
                (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        LocationUtil.getInstance(getContext()).setAddressCallback(new LocationUtil.AddressCallback() {
            @Override
            public void onGetAddress(Address address) {
                String countryName = address.getCountryName();//国家
                String adminArea = address.getAdminArea();//省
                String locality = address.getLocality();//市
                String subLocality = address.getSubLocality();//区
                String featureName = address.getFeatureName();//街道
                System.out.println("定位地址" + countryName + adminArea + locality + subLocality + featureName);
            }

            @Override
            public void onGetLocation(double lat, double lng, double acc) {
                coordinate[0] = String.valueOf(lat);
                coordinate[1] = String.valueOf(lng);
                accuracy = String.valueOf(acc);
                System.out.println("coordinate[0]=" + coordinate[0] + "coordinate[1]" + coordinate[1] + "accuracy" + accuracy);
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            rxPermissions
                    .request(
                            Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(granted -> {
                        System.out.println("[Granted] " + granted);
                        if (granted) {
                            //获得权限
                            try {
                                // locationManager.removeUpdates(listener);
                                // locationManager.requestLocationUpdates(
                                //         LocationManager.GPS_PROVIDER, 0, 0, listener);
                                showMsg("已授权精确定位权限");
                            } catch (SecurityException e) {
                                System.out.println("[Error]");
                                e.printStackTrace();
                            }
                        } else {
                            showMsg("未获取到位置权限,请在设置内打开");
                        }
                    });
            rxPermissions
                    .request(
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    .subscribe(granted -> {
                        System.out.println("[Granted] " + granted);
                        if (granted) {
                            //获得权限
                            try {
                                showMsg("已获后台位置权限");
                            } catch (SecurityException e) {
                                System.out.println("[Error]");
                                e.printStackTrace();
                            }
                        } else {
                            showMsg("未获取到后台位置权限,请在设置内打开");
                        }
                    });
        }
        ImageButton cameraBtn = getActivity().findViewById(R.id.cameraBtn);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    rxPermissions
                            .request(Manifest.permission.CAMERA
                            )
                            .subscribe(granted -> {
                                if (granted) {
                                    //获得权限
                                    turnOnCamera();
                                } else {
                                    showMsg("未获取到权限,请在设置内打开");
                                }
                            });
                } else {
                    turnOnCamera();
                }
            }
        });
        ImageButton albumBtn = getActivity().findViewById(R.id.albumBtn);
        albumBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    rxPermissions
                            .request(
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(granted -> {
                                System.out.println("[Granted] " + granted);
                                if (granted) {
                                    //获得权限
                                    openAlbum();
                                } else {
                                    showMsg("未获取到权限,请在设置内打开");
                                }
                            });
                } else {
                    openAlbum();
                }
            }
        });

        boolean er = shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        System.out.println("===========================================" + "\n" + er);

        // showLoading();
    }

    String[] coordinate = new String[2];
    String accuracy = "-1";
    LoadingAnimatorView loadView;
    /**
     * 打开相册
     */
    private static final int OPEN_ALBUM_CODE = 100;
    /**
     * 打开相机
     */
    private static final int TAKE_PHOTO_CODE = 101;

    /**
     * 显示图片
     */
    private ImageView ivPicture;

    /**
     * 动态权限
     */
    private RxPermissions rxPermissions;
    private File outputImage;
    private File outputImageCut;
    Context mContext;


    @SuppressLint("CheckResult")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxPermissions = new RxPermissions(getParentFragment().getActivity());


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
     * @param imageBase64 图片Base64
     */
    private void ImageDiscern(String imageBase64) throws Exception {
        // String apiKey = "ojlt9sSkvTjiugRGANYWXD1JQ035ttwx5oUILTL4rSYVGbzzN2";
        String apiKey = "4PjUdBxwusXeBznHkdVREaF0WMquaTE6xIJrJprxLt5l13eU2M";

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
        showLoading();


    }

    public void showLoading() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        CardView layout = getActivity().findViewById(R.id.cameraCardView);
        loadView = new LoadingAnimatorView(getActivity(), screenWidth, screenHeight);
        layout.addView(loadView);
        System.out.println("[Start Loading]");
        ImageButton imageButton1 = getActivity().findViewById(R.id.cameraBtn);
        ImageButton imageButton2 = getActivity().findViewById(R.id.albumBtn);
        imageButton1.setClickable(false);
        imageButton2.setClickable(false);
    }

    public void endLoading() {
        loadView.flag = false;
        CardView layout = getActivity().findViewById(R.id.cameraCardView);
        layout.removeView(loadView);
        ImageButton imageButton1 = getActivity().findViewById(R.id.cameraBtn);
        ImageButton imageButton2 = getActivity().findViewById(R.id.albumBtn);
        imageButton1.setClickable(true);
        imageButton2.setClickable(true);
    }

    public String sendPostRequest(String urlString, JSONObject data) throws Exception {
        URL url = new URL(urlString);
        Thread t = new Thread(new Runnable() {
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
                    System.out.println("[Connection] " + con.toString());
                    try {
                        con.connect();
                        OutputStream os = con.getOutputStream();
                        os.write(data.toString().getBytes());
                        os.flush();
                        os.close();
                        System.out.println("[Response code] " + con.getResponseCode());
                    } catch (Exception e) {
                        System.out.println("[Connection Error]");
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "connection error.", Toast.LENGTH_SHORT).show();
                    }
                    InputStream inputStream = con.getInputStream();
                    byte[] data = new byte[1024];
                    StringBuffer sb1 = new StringBuffer();
                    int length = 0;
                    while ((length = inputStream.read(data)) != -1) {
                        String s = new String(data, Charset.forName("utf-8"));
                        sb1.append(s);
                    }
                    String response = sb1.toString();
                    LogUtil.handleMessage(1, "1", response);
                    View v = CameraFragment.super.getView();
                    v.post(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.setClass(getContext(), CameraResultActivity.class);
                            intent.putExtra("response", response);
                            intent.putExtra("latitude", coordinate[0]);
                            intent.putExtra("longitude", coordinate[1]);
                            intent.putExtra("accuracy", accuracy);
                            System.out.println("[Accuracy] " + accuracy);

                            endLoading();
                            startActivity(intent);
                            System.out.println("[Accuracy] " + accuracy);
                        }
                    });
                    System.out.println("[Response] " + response);
                    con.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        return null;
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
    Uri imageUri;

    public void turnOnCamera() {
        //定义文件名称
        String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "takephoto" + ".jpg";
        String path = CacheUtil.getCacheDirectory(getContext(), null)
                + "/" + filename;
        // 创建File对象,获取文件的ContentURI
        outputImage = new File(path);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(getActivity(), "com.example.plantdroid.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }

        //打开相机
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为MediaStore下的ACTION_IMAGE_CAPTURE
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, TAKE_PHOTO_CODE);
    }

    private void startUCrop() {
        //裁剪后保存到文件中
        String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "takephotocut" + ".jpg";
        outputImageCut = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + "/" + filename);
        Uri destinationUri = Uri.fromFile(outputImageCut);
        System.out.println("destinationUri" + destinationUri);

        UCrop uCrop = UCrop.of(imageUri, destinationUri);
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setToolbarTitle("Clip");
        options.setToolbarWidgetColor(ActivityCompat.getColor(getContext(), R.color.white));
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        options.setCropGridColor(ActivityCompat.getColor(getContext(), R.color.green_light_2));//设置裁剪网格的颜色
        options.setCropFrameColor(ActivityCompat.getColor(getContext(), R.color.green_light_2));//设置裁剪框的颜色
        options.setActiveControlsWidgetColor(ActivityCompat.getColor(getContext(), R.color.green_light_3));
        //options.setDimmedLayerColor(ActivityCompat.getColor(getContext(), R.color.green_light_3));
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(getContext(), R.color.green_light_3));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(getContext(), R.color.green_light_3));
        //是否能调整裁剪框
        options.setFreeStyleCropEnabled(false);
        uCrop.withOptions(options);
        uCrop.withAspectRatio(4, 3); //比例
        uCrop.start(getContext(), this, REQUEST_CROP);
    }

    /**
     * Toast提示
     *
     * @param msg 内容
     */
    private void showMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            System.out.println(requestCode);
            if (requestCode == OPEN_ALBUM_CODE) {
                //打开相册返回
                final String[] filePathColumns = {
                        MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.DATE_ADDED,
                        MediaStore.Images.Media.LATITUDE,
                        MediaStore.Images.Media.LONGITUDE,
                };
                final Uri imageUri = Objects.requireNonNull(data).getData();
                Cursor cursor = getActivity().getContentResolver().query(imageUri, filePathColumns, null, null, null);
                cursor.moveToFirst();
                //获取index
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                int columnIndexName = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                int columnIndexAddDate = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
                int columnIndexLatitude = cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE);
                int columnIndexLongitude = cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE);
                //获取图片路径,名字，添加日期，经度，纬度
                String imagePath = cursor.getString(columnIndex);
                //String name = cursor.getString(columnIndexName);
                //long addDate = cursor.getLong(columnIndexAddDate);
                coordinate[0] = String.valueOf(cursor.getFloat(columnIndexLatitude));

                coordinate[1] = String.valueOf(cursor.getFloat(columnIndexLongitude));
                accuracy = "-1";
                cursor.close();

                //识别
                try {
                    localImageDiscern(imagePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == TAKE_PHOTO_CODE) {
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

                //拍照返回
                String imagePath = outputImage.getAbsolutePath();
                System.out.println(imagePath);
                //调取另一个activity
                startUCrop();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    float[] latLong = new float[2];
                    Uri photoUri = MediaStore.setRequireOriginal(imageUri);
                    InputStream stream = null;
                    try {
                        stream = getActivity().getContentResolver().openInputStream(photoUri);
                        if (stream != null) {
                            ExifInterface exifInterface = new ExifInterface(stream);
                            exifInterface.getLatLong(latLong);
                            //获取图片经纬度信息，第一个元素为纬度，第二个元素为经度
                            if (latLong[0] == 0 && latLong[1] == 0) {
                                System.out.println("本图片无位置信息");
                            } else {
                                coordinate[0] = String.valueOf(latLong[0]);
                                coordinate[1] = String.valueOf(latLong[1]);
                                accuracy = "-1";
                                System.out.println("latLong[0]" + latLong[0] + "latLong[1]" + latLong[1]);
                            }
                            stream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == REQUEST_CROP) {
                String imagePath;
                Uri croppedUri = UCrop.getOutput(data);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    imagePath = uriToFileApiQ(getContext(), croppedUri);
                } else {
                    imagePath = outputImageCut.getAbsolutePath();
                }
                try {
                    localImageDiscern(imagePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (resultCode == UCrop.RESULT_ERROR) {
                if (data != null) {
                    Throwable throwable = UCrop.getError(data);
                    Toast.makeText(getActivity(), throwable.getMessage().toString() + "裁剪失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "裁剪失败", Toast.LENGTH_SHORT).show();
                }
            }
            showMsg("没有上传图片呦(ﾉ◕ヮ◕)ﾉ*:･ﾟ✧");
        }
    }

    /**
     * 最终结果
     *
     * @param context
     */

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static String uriToFileApiQ(Context context, Uri uri) {
        File file = null;
        //android10以上转换
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            file = new File(uri.getPath());
        } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //把文件复制到沙盒目录
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                try {
                    InputStream is = contentResolver.openInputStream(uri);
                    File cache = new File(context.getExternalCacheDir().getAbsolutePath(), Math.round((Math.random() + 1) * 1000) + displayName);
                    FileOutputStream fos = new FileOutputStream(cache);
                    FileUtils.copy(is, fos);
                    file = cache;
                    fos.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file.getAbsolutePath();
    }

    /**
     * 本地图片识别
     */
    private void localImageDiscern(String imagePath) throws Exception {
        try {
            System.out.println("[Image Path] " + imagePath);
            //通过图片路径显示图片
            // Glide.with(this)
            //         .load(imagePath)
            //         .placeholder(R.drawable.bluebell)
            //         .fitCenter()
            //         .into(ivPicture);
            String imageBase64 = base64EncodeFromFile(imagePath);
            //图像识别
            ImageDiscern(imageBase64);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            endLoading();
        } catch (Exception e) {}
    }
}