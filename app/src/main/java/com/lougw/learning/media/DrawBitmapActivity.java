package com.lougw.learning.media;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.lougw.learning.R;
import com.lougw.learning.utils.UIUtils;

import java.io.File;

public class DrawBitmapActivity extends AppCompatActivity {
    /**
     * sd卡权限
     */
    public static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static boolean[] PERMISSIONS_NECESSARY_STORAGE = {true, true};
    private ImageView ivTest;
    private SurfaceView svTest;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_bitmap);
        intView();
        //使用兼容库就无需判断系统版本
        if (lacksPermissions(PERMISSIONS_STORAGE, PERMISSIONS_NECESSARY_STORAGE)) {
            //拥有权限，执行操作
            initData();
        } else {
            //没有权限，向用户请求权限
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 123);
        }

    }

    // 判断必须权限集合
    public boolean lacksPermissions(String permissions[], boolean isNecessaryPermissionList[]) {
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            boolean isNecessary = true;
            if (isNecessaryPermissionList != null) {
                isNecessary = isNecessaryPermissionList[i];
            }
            if (lacksPermission(permission) && isNecessary) {
                return true;
            }
        }
        return false;
    }

    // 判断是否缺少权限
    public boolean lacksPermission(String permission) {
        return !onCheckSelfPermission(permission);
    }

    // 判断是否获得权限
    public boolean onCheckSelfPermission(String permission) {
        try {
            return ContextCompat.checkSelfPermission(UIUtils.getContext(), permission) == PackageManager.PERMISSION_GRANTED;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }

    private void intView() {
        ivTest = (ImageView) findViewById(R.id.iv_test);
        svTest = (SurfaceView) findViewById(R.id.sv_test);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void initData() {


        String filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "aa.jpg";
        mToolbar.setTitle(filePath);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 123) {
            if (lacksPermissions(PERMISSIONS_STORAGE, PERMISSIONS_NECESSARY_STORAGE)) {
                //拥有权限，执行操作
                initData();
            }
        }

    }
}
