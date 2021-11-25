package com.example.plantdroid;

// import cn.yw.lib.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class LoadingAnimatorView extends SurfaceView implements
        SurfaceHolder.Callback, Runnable {
    private SurfaceHolder holder;
    private Bitmap bitmap;
    private Paint paint1;
    private Paint paint2;

    private int screenWidth;
    private int screenHeight;

    public boolean flag = true;
    private int y = 100;

    public LoadingAnimatorView(Context context, int width, int  height) {
        super(context);
        screenWidth = width;
        screenHeight = height;
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        holder = this.getHolder();
        holder.addCallback(this);
        paint1 = new Paint();
        paint1.setColor(getResources().getColor(R.color.white));
        paint1.setTextSize(72);
        paint2 = new Paint();
        paint2.setColor(Color.GRAY);
        @SuppressLint("ResourceType") Bitmap bitmap1 = BitmapFactory.decodeStream(context.getResources()
                .openRawResource(R.drawable.ic_logo_xlarge));
        bitmap = bitmap1.extractAlpha();// 获取一个透明图片
        y = bitmap.getHeight();//初始化y轴坐标
    }
    //改变裁剪区域
    private void playAnimator() {
        if (y > 5) {
            y -= 10;
        } else {
            // y = bitmap.getWidth();
        }
    }

    private void drawLoadingAnimator() {
        Canvas canvas = null;
        try {
            canvas = holder.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(getResources().getColor(R.color.green_light_3));
                int centerX = (screenWidth - bitmap.getWidth()) / 2;
                int centerY = (screenHeight/2 - bitmap.getHeight());
                canvas.drawText(String.valueOf(100 * (bitmap.getHeight()-y) / bitmap.getHeight()) + "%", centerX + 200, centerY + bitmap.getHeight() + 100, paint1);
                canvas.drawBitmap(bitmap, centerX, centerY, paint2);
                canvas.save();      //裁剪
                canvas.clipRect(centerX, y + centerY, centerX + bitmap.getWidth(),
                        y + centerY + bitmap.getHeight());
                canvas.drawBitmap(bitmap, centerX, centerY, paint1);
                canvas.restore();
            }
            /*
             * Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
             * Rect dst = new Rect(100, 100, bitmap.getWidth()+100, y+100);
             * canvas.drawBitmap(bitmap, src, dst, paint2);
             */
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (holder != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        new Thread(this).start();//开启绘制线程
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    //绘制动画线程

    @Override
    public void run() {
        while (flag) {
            drawLoadingAnimator();
            playAnimator();
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}