package com.lougw.learning.media;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import com.lougw.learning.R;

import java.io.File;

public class MediaActivity extends AppCompatActivity {
    private ImageView ivTest;
    private SurfaceView svTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        intView();
        initData();
    }


    private void intView() {
        ivTest = (ImageView) findViewById(R.id.iv_test);
        svTest = (SurfaceView) findViewById(R.id.sv_test);
    }

    private void initData() {
        gotoImageView();
        gotoSurfaceView();
    }

    private void gotoSurfaceView() {
        svTest.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (holder == null) {
                    return;
                }
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.STROKE);
                String filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "aa.jpg";
                Bitmap btm = BitmapFactory.decodeFile(filePath);
                Canvas canvas = holder.lockCanvas();
                canvas.drawBitmap(btm, 0, 0, paint);
                holder.unlockCanvasAndPost(canvas);


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });


    }

    void gotoImageView() {
        String filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "aa.jpg";
        Bitmap btm = BitmapFactory.decodeFile(filePath);
        ivTest.setImageBitmap(btm);
    }


}
