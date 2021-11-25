package com.example.plantdroid.ui.camera;


import static com.yalantis.ucrop.UCrop.REQUEST_CROP;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;


import com.bumptech.glide.Glide;
import com.example.plantdroid.CameraResultActivity;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
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

    //设置每一秒获取一次location信息

    public void locationUpdates(Location location) {  //获取指定的查询信息
        //如果location不为空时
        if (location != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("您的位置信息：\n");
            stringBuilder.append("经度：");
            stringBuilder.append(location.getLongitude());
            stringBuilder.append("\n纬度：");
            stringBuilder.append(location.getLatitude());
            stringBuilder.append(location.getAccuracy());
            coordinateCamera[0] = String.valueOf(location.getLatitude());
            coordinateCamera[1] = String.valueOf(location.getLongitude());
            accuracy = String.valueOf(location.getAccuracy());
            System.out.println(stringBuilder);
        } else {
            //否则输出空信息
            System.out.println("没有获取到GPS信息");
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final LocationManager locationManager =
                (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
            @Override
            public void onStatusChanged(String provider, int status, Bundle arg2) {
            }

            // Provider被enable时触发此函数，比如GPS被打开
            @Override
            public void onProviderEnabled(String provider) {
            }

            // Provider被disable时触发此函数，比如GPS被关闭
            @Override
            public void onProviderDisabled(String provider) {
            }

            //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
            @Override
            public void onLocationChanged(Location loc) {
                System.out.println("==onLocationChanged==");
                locationUpdates(loc);
            }
        };
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        //从GPS获取最新的定位信息

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationUpdates(location);    //将最新的定位信息传递给创建的locationUpdates()方法中

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
                coordinateCamera[0] = String.valueOf(lat);
                coordinateCamera[1] = String.valueOf(lng);
                accuracy = String.valueOf(acc);
                System.out.println("coordinate[0]=" + coordinateCamera[0] + "coordinate[1]" + coordinateCamera[1] + "accuracy" + accuracy);
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
        Button shareB =getActivity().findViewById((R.id.button2));
        shareB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String dir = Environment.getExternalStorageDirectory().getAbsolutePath() ;
                Calendar now = new GregorianCalendar();
                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                String fileName = simpleDate.format(now.getTime());
                Bitmap cs =  screenShotView(requireView());
                //System.out.println("+++++++++++++++++++++++++++++++++"+cs.toString()+"+++++++++++++++++++++++++++++");
                saveBitmap(fileName+".jpg",cs,getContext());

            }
        });

        ImageButton cameraBtn = getActivity().findViewById(R.id.cameraBtn);
        cameraBtn.setOnClickListener(new View.OnClickListener() {

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

        ivPicture = getActivity().findViewById(R.id.preImgView);
    }
    static boolean fileIsExist(String fileName)
    {
        //传入指定的路径，然后判断路径是否存在
        File file=new File(fileName);
        if (file.exists())
            return true;
        else{
            //file.mkdirs() 创建文件夹的意思
            return file.mkdirs();
        }
    }
    void saveBitmap(String name, Bitmap bm, Context mContext) {
        Log.d("Save Bitmap", "Ready to save picture");
        //指定我们想要存储文件的地址
        String TargetPath = mContext.getExternalFilesDir(null).getPath() + "/images/";
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
                imageUri1 = Uri.fromFile(saveFile);

            //}
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri1);
            shareIntent.setType("image/jpeg");
            startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.hello)));
        }
    }

    String[] coordinateCamera = new String[2];
    String time ;
    String accuracy = "-1";
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


    private Bitmap screenShotView(View view) {
        //开启缓存功能
        view.setDrawingCacheEnabled(true);
        //创建缓存
        view.buildDrawingCache();
        //获取缓存Bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        return bitmap;
    }

    /**
     * 获取整个窗口的截图
     *
     * @param context
     * @return
     */
    @SuppressLint("NewApi")
    private Bitmap captureScreen(Activity context) {
        View cv = context.getWindow().getDecorView();

        cv.setDrawingCacheEnabled(true);
        cv.buildDrawingCache();
        Bitmap bmp = cv.getDrawingCache();
        if (bmp == null) {
            return null;
        }

        bmp.setHasAlpha(false);
        bmp.prepareToDraw();
        return bmp;
    }

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
                            if (typePhoto) {
                                intent.putExtra("latitude", coordinateCamera[0]);
                                intent.putExtra("longitude", coordinateCamera[1]);
                                intent.putExtra("time", time = String.valueOf(System.currentTimeMillis()));
                                intent.putExtra("accuracy", accuracy);
                            }else{
                                intent.putExtra("latitude", coordinateALBUM[0]);
                                intent.putExtra("longitude", coordinateALBUM[1]);
                                intent.putExtra("time", time);
                                intent.putExtra("accuracy", accuracy);
                            }
                            startActivity(intent);
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
    String[] coordinateALBUM =new String[2];
    Boolean typePhoto;
    @SuppressLint("CheckResult")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {

            System.out.println(requestCode);

            if (requestCode == OPEN_ALBUM_CODE) {
                typePhoto = false;
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
                String addDate = String.valueOf(cursor.getLong(columnIndexAddDate));
                coordinateALBUM[0] = String.valueOf(cursor.getFloat(columnIndexLatitude));
                coordinateALBUM[1] = String.valueOf(cursor.getFloat(columnIndexLongitude));
                time =addDate;
                System.out.println("addDate: "+addDate);
                accuracy = "-1";
                cursor.close();

                //识别
                try {
                    localImageDiscern(imagePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == TAKE_PHOTO_CODE) {
                typePhoto = true;
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

                //拍照返回
                String imagePath = outputImage.getAbsolutePath();
                System.out.println(imagePath);
                //调取另一个activity
                startUCrop();
                // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                //     float[] latLong = new float[2];
                //     Uri photoUri = MediaStore.setRequireOriginal(imageUri);
                //     InputStream stream = null;
                //     try {
                //         stream = getActivity().getContentResolver().openInputStream(photoUri);
                //         if (stream != null) {
                //             ExifInterface exifInterface = new ExifInterface(stream);
                //             exifInterface.getLatLong(latLong);
                //             //获取图片经纬度信息，第一个元素为纬度，第二个元素为经度
                //             if (latLong[0] == 0 && latLong[1] == 0) {
                //                 System.out.println("本图片无位置信息");
                //             } else {
                //                 coordinateCamera[0] = String.valueOf(latLong[0]);
                //                 coordinateCamera [1] = String.valueOf(latLong[1]);
                //                 accuracy = "-1";
                //                 System.out.println("=============================================");
                //                 System.out.println("latLong[0]" + latLong[0] + "latLong[1]" + latLong[1]);
                //                 System.out.println("=============================================");
                //             }
                //             stream.close();
                //         }
                //     } catch (IOException e) {
                //         e.printStackTrace();
                //     }
                //}
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
            Glide.with(this)
                    .load(imagePath)
                    .placeholder(R.drawable.bluebell)
                    .fitCenter()
                    .into(ivPicture);
            String imageBase64 = base64EncodeFromFile(imagePath);
            //图像识别
            ImageDiscern(imageBase64);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}