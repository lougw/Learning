package com.lougw.learning.media;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.lougw.learning.R;
import com.lougw.learning.net.NetActivity;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MediaActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_draw_bitmap})
    public void Click(View v) {
        if (v.getId() == R.id.btn_draw_bitmap) {
            Intent intent = new Intent(this, DrawBitmapActivity.class);
            startActivity(intent);
        }
    }


}
