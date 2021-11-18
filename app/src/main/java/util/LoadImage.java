package util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadImage {

    public static void setImageView(ImageView imgView, String imgUrl) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] data = LoadImage.getImage(imgUrl);
                    if (data != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap
                        imgView.setImageBitmap(bitmap);// display image
                    } else {
                        System.out.println("[Image error!]");
                    }
                } catch (Exception e) {
                    System.out.println("[network error!]");
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public static byte[] getImage(String path) throws Exception {
        URL url = new URL(path);
        byte[] imgBytes = new byte[1];
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setDoOutput(false);//允許輸入流，即允許下載
            conn.setDoInput(true);//允許輸出流，即允許上傳
            conn.setUseCaches(false); //設置是否使用緩存
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            System.out.println("[Connection] " + conn.toString());
            conn.connect();

            System.out.println("[Connection] " + conn.getRequestMethod());
            System.out.println("[Response code] " + conn.getResponseCode());
            InputStream inputStream = conn.getInputStream();

            if (conn.getResponseCode() == 200) {
                imgBytes = readStream(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgBytes;
    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }
}
