package com.lougw.learning;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.lougw.learning.media.MediaActivity;
import com.lougw.learning.media.OpenGLActivity;
import com.lougw.learning.net.NetActivity;
import com.lougw.learning.utils.CheckLogin;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.net)
    Button btnNet;

    @CheckLogin
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.d("Main1111111", "onCreate");
        getName();
        isHigh();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        // 设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        wmParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
        wmParams.gravity = Gravity.LEFT | Gravity.BOTTOM;
        // 设置Window flag
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.width = 50;
        wmParams.height = 50;
        View mContainer = LayoutInflater.from(this).inflate(R.layout.music_app_float_btn, null);
        wm.addView(mContainer, wmParams);
    }

    @OnClick({R.id.calculate_view, R.id.text_view, R.id.switcher_view, R.id.wheel_view, R.id.flowLayout, R.id.tab, R.id.service, R.id.brightness, R.id.recycle, R.id.animal, R.id.net, R.id.shimmer, R.id.full_screen, R.id.web_server, R.id.rxjava, R.id.andfix, R.id.network, R.id.guid, R.id.download, R.id.opengl, R.id.media, R.id.layout_android, R.id.webview})
    public void Click(View v) {
        if (v.getId() == R.id.net) {
            Intent intent = new Intent(this, NetActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.calculate_view) {
            Intent intent = new Intent(this, CalculateActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.text_view) {
            Intent intent = new Intent(this, TextActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.switcher_view) {
            Intent intent = new Intent(this, TextSwitcherActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.wheel_view) {
            Intent intent = new Intent(this, WheelViewActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.flowLayout) {
            Intent intent = new Intent(this, CustomViewActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tab) {
            Intent intent = new Intent(this, TabActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.shimmer) {
            Intent intent = new Intent(this, ShimmerActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.full_screen) {
            Intent intent = new Intent(this, FullScreenActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.web_server) {
            Intent intent = new Intent(this, WebServerActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.rxjava) {
            Intent intent = new Intent(this, RxActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.andfix) {
            Intent intent = new Intent(this, AndFixActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.network) {
            Intent intent = new Intent(this, NetActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.guid) {
            Intent intent = new Intent(this, GUIDActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.download) {
            Intent intent = new Intent(this, DownloadActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.opengl) {
            Intent intent = new Intent(this, OpenGLActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.media) {
            Intent intent = new Intent(this, MediaActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.layout_android) {
            Intent intent = new Intent(this, AndroidViewActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.webview) {
            Intent intent = new Intent(this, WebViewActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.animal) {
            Intent intent = new Intent(this, AnimalActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.recycle) {
            Intent intent = new Intent(this, RecycleViewActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.brightness) {
            Intent intent = new Intent(this, ScreenBrightnessActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.service) {
            Intent intent = new Intent(this, ServiceActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Main1111111", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Main1111111", "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Main1111111", "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Main1111111", "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Main1111111", "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Main1111111", "onDestroy");
    }

    private String getName() {
        return "lougw";
    }

    private boolean isHigh() {
        return true;
    }
}
