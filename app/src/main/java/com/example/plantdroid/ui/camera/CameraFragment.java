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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.net.UnknownHostException;
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

    //???????????????????????????location??????

    public void locationUpdates(Location location) {  //???????????????????????????
        //??????location????????????
        if (location != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("?????????????????????\n");
            stringBuilder.append("?????????");
            stringBuilder.append(location.getLongitude());
            stringBuilder.append("\n?????????");
            stringBuilder.append(location.getLatitude());
            stringBuilder.append(location.getAccuracy());
            coordinateCamera[0] = String.valueOf(location.getLatitude());
            coordinateCamera[1] = String.valueOf(location.getLongitude());
            accuracy = String.valueOf(location.getAccuracy());
            System.out.println(stringBuilder);
        } else {
            //?????????????????????
            System.out.println("???????????????GPS??????");
        }
    }

    LocationListener locationListener = new LocationListener() {
        // Provider??????????????????????????????????????????????????????????????????????????????????????????
        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {
        }

        // Provider???enable???????????????????????????GPS?????????
        @Override
        public void onProviderEnabled(String provider) {
        }

        // Provider???disable???????????????????????????GPS?????????
        @Override
        public void onProviderDisabled(String provider) {
        }

        //??????????????????????????????????????????Provider?????????????????????????????????????????????
        @Override
        public void onLocationChanged(Location loc) {
            System.out.println("==onLocationChanged==");
            locationUpdates(loc);
        }
    };

    @SuppressLint({"CheckResult", "MissingPermission"})
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        LocationUtil.getInstance(getContext()).setAddressCallback(new LocationUtil.AddressCallback() {
            @Override
            public void onGetAddress(Address address) {
                String countryName = address.getCountryName();//??????
                String adminArea = address.getAdminArea();//???
                String locality = address.getLocality();//???
                String subLocality = address.getSubLocality();//???
                String featureName = address.getFeatureName();//??????
                System.out.println("????????????" + countryName + adminArea + locality + subLocality + featureName);
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
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe(granted -> {
                        System.out.println("[Granted] " + granted);
                        if (granted) {
                            //????????????
                            try {
                                //showMsg("???????????????????????????");
                            } catch (SecurityException e) {
                                System.out.println("[Error]");
                                e.printStackTrace();
                            }
                        } else {
                            //showMsg("????????????????????????,?????????????????????");
                        }
                    });

        }


        ImageButton cameraBtn = getActivity().findViewById(R.id.cameraBtn);
        cameraBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                System.out.println("click camera");
                // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                    rxPermissions
                            .request(Manifest.permission.CAMERA
                            )
                            .subscribe(granted -> {
                                if (granted) {
                                    turnOnCamera();
                                } else {
                                    showMsg("No permission, please open in Settings");
                                }
                            });
                } else {
                    rxPermissions
                            .request(Manifest.permission.CAMERA,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                            .subscribe(granted -> {
                                if (granted) {
                                    turnOnCamera();
                                } else {
                                    showMsg("No permission, please open in Settings");
                                }
                            });
                }


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    rxPermissions
                            .request(
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            .subscribe(granted -> {
                                System.out.println("[Granted] " + granted);
                                if (granted) {
                                    //????????????
                                    try {
                                        //showMsg("????????????????????????");
                                    } catch (SecurityException e) {
                                        System.out.println("[Error]");
                                        e.printStackTrace();
                                    }
                                } else {

                                    //showMsg("??????????????????????????????,?????????????????????");
                                }

                            });
                    LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

                    if (ActivityCompat.checkSelfPermission
                            (getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    } else {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        locationUpdates(location);
                    }
                    // }
                }else {
                    LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

                    if (ActivityCompat.checkSelfPermission
                            (getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    } else {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        locationUpdates(location);
                    }
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
                                    //????????????
                                    openAlbum();
                                } else {
                                    showMsg("No permission, please open in Settings");
                                }
                            });
                } else {
                    openAlbum();
                }
            }
        });

        TextView plantIdLink = getActivity().findViewById(R.id.plantIdLink);
        plantIdLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://plant.id/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


    }


    /**
     * ???????????????????????????
     *
     * @param context
     * @return
     */


    String[] coordinateCamera = new String[2];
    String timeALBUM;
    String accuracy = "-1";
    LoadingAnimatorView loadView;
    /**
     * ????????????
     */
    private static final int OPEN_ALBUM_CODE = 100;
    /**
     * ????????????
     */
    private static final int TAKE_PHOTO_CODE = 101;

    /**
     * ????????????
     */
    private ImageView ivPicture;

    /**
     * ????????????
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
     * @param imageBase64 ??????Base64
     */
    private void ImageDiscern(String imageBase64) throws Exception {
//         String apiKey = "ojlt9sSkvTjiugRGANYWXD1JQ035ttwx5oUILTL4rSYVGbzzN2";
//        String apiKey = "4PjUdBxwusXeBznHkdVREaF0WMquaTE6xIJrJprxLt5l13eU2M";
//        String apiKey = "B4dUArzXoRW6SILORcC0iEWGl0PGmFQv5EpK8vf8a2r5xPaCS7";
        String apiKey = "xEgQlF4IQZe9ttc1g5ohfrDlB7BVrHJ0AWONf4IadPyihXAVXs";
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

                    con.setDoOutput(true);//?????????????????????????????????
                    con.setDoInput(true);//?????????????????????????????????
                    con.setUseCaches(false); //????????????????????????
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
                    } catch (UnknownHostException e) {
                        System.out.println("[Connection Error]");
                        e.printStackTrace();
                        View v = CameraFragment.super.getView();
                        v.post(new Runnable() {
                            @Override
                            public void run() {
                                endLoading();
                                Toast.makeText(getActivity(), "Network Error. Please check your current network or open your VPN.", Toast.LENGTH_SHORT).show();
                            }
                        });

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

                            if (typePhoto) {
                                intent.putExtra("latitude", coordinateCamera[0]);
                                intent.putExtra("longitude", coordinateCamera[1]);
                                String tim = String.valueOf(System.currentTimeMillis());
                                char a = tim.charAt(0);
                                String timeCamera = a + "." + tim.substring(1) + "000E9";
                                intent.putExtra("time", timeCamera);


                                intent.putExtra("accuracy", accuracy);

                            } else {
                                intent.putExtra("latitude", coordinateALBUM[0]);
                                intent.putExtra("longitude", coordinateALBUM[1]);
                                intent.putExtra("time", timeALBUM);
                                intent.putExtra("accuracy", accuracy);
                            }
                            endLoading();
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
     * ????????????
     */
    private void openAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_ALBUM_CODE);
    }

    /**
     * ????????????
     */
    Uri imageUri;

    public void turnOnCamera() {
        //??????????????????
        String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "takephoto" + ".jpg";
        String path = CacheUtil.getCacheDirectory(getContext(), null)
                + "/" + filename;
        // ??????File??????,???????????????ContentURI
        outputImage = new File(path);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(getActivity(), "com.example.plantdroid.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }

        //????????????
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);//??????Action???MediaStore??????ACTION_IMAGE_CAPTURE
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, TAKE_PHOTO_CODE);
    }

    private void startUCrop() {
        //???????????????????????????
        String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "takephotocut" + ".jpg";
        outputImageCut = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + "/" + filename);
        Uri destinationUri = Uri.fromFile(outputImageCut);
        System.out.println("destinationUri" + destinationUri);

        UCrop uCrop = UCrop.of(imageUri, destinationUri);
        UCrop.Options options = new UCrop.Options();
        //????????????????????????????????????
        options.setToolbarTitle("Clip");
        options.setToolbarWidgetColor(ActivityCompat.getColor(getContext(), R.color.white));
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        options.setCropGridColor(ActivityCompat.getColor(getContext(), R.color.green_light_2));//???????????????????????????
        options.setCropFrameColor(ActivityCompat.getColor(getContext(), R.color.green_light_2));//????????????????????????
        options.setActiveControlsWidgetColor(ActivityCompat.getColor(getContext(), R.color.green_light_3));
        //options.setDimmedLayerColor(ActivityCompat.getColor(getContext(), R.color.green_light_3));
        //??????toolbar??????
        options.setToolbarColor(ActivityCompat.getColor(getContext(), R.color.green_light_3));
        //?????????????????????
        options.setStatusBarColor(ActivityCompat.getColor(getContext(), R.color.green_light_3));
        //????????????????????????
        options.setFreeStyleCropEnabled(false);
        uCrop.withOptions(options);
        uCrop.withAspectRatio(4, 3); //??????
        uCrop.start(getContext(), this, REQUEST_CROP);
    }

    /**
     * Toast??????
     *
     * @param msg ??????
     */
    private void showMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    String[] coordinateALBUM = new String[2];
    Boolean typePhoto;

    @SuppressLint("CheckResult")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        System.out.println(resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            System.out.println(requestCode);
            if (requestCode == OPEN_ALBUM_CODE) {
                typePhoto = false;
                //??????????????????
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
                //??????index
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                int columnIndexName = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                int columnIndexAddDate = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
                int columnIndexLatitude = cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE);
                int columnIndexLongitude = cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE);
                //??????????????????,???????????????????????????????????????
                String imagePath = cursor.getString(columnIndex);
                //String name = cursor.getString(columnIndexName);
                String addDate = String.valueOf(cursor.getLong(columnIndexAddDate));
                coordinateALBUM[0] = String.valueOf(cursor.getFloat(columnIndexLatitude));
                coordinateALBUM[1] = String.valueOf(cursor.getFloat(columnIndexLongitude));
                char a = addDate.charAt(0);
                timeALBUM = a + "." + addDate.substring(1) + "000E9";

                accuracy = "-1";
                cursor.close();

                //??????
                try {
                    localImageDiscern(imagePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == TAKE_PHOTO_CODE) {
                typePhoto = true;
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

                //????????????
                String imagePath = outputImage.getAbsolutePath();
                System.out.println(imagePath);
                //???????????????activity
                startUCrop();
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
                    Toast.makeText(getActivity(), throwable.getMessage().toString() + "Cut out failure", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Cut out failure", Toast.LENGTH_SHORT).show();
                }
            }
            showMsg("No pictures uploaded");
        }
    }

    /**
     * ????????????
     *
     * @param context
     */

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static String uriToFileApiQ(Context context, Uri uri) {
        File file = null;
        //android10????????????
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            file = new File(uri.getPath());
        } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //??????????????????????????????
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
     * ??????????????????
     */
    private void localImageDiscern(String imagePath) throws Exception {
        try {
            System.out.println("[Image Path] " + imagePath);
            String imageBase64 = base64EncodeFromFile(imagePath);
            //????????????
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
        } catch (Exception e) {
        }
    }
}