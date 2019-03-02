package com.lougw.learning;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.pm.ShortcutInfoCompat;
import android.support.v4.content.pm.ShortcutManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
    }

    @OnClick({R.id.brightness,R.id.recycle,R.id.animal,R.id.net, R.id.shimmer, R.id.full_screen, R.id.web_server, R.id.rxjava, R.id.andfix, R.id.network, R.id.guid, R.id.download, R.id.opengl, R.id.media,R.id.layout_android,R.id.webview})
    public void Click(View v) {
        if (v.getId() == R.id.net) {
            Intent intent = new Intent(this, NetActivity.class);
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
        }  else if (v.getId() == R.id.layout_android) {
            Intent intent = new Intent(this, AndroidViewActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.webview) {
            Intent intent = new Intent(this, WebViewActivity.class);
            startActivity(intent);
        }else if (v.getId() == R.id.animal) {
            Intent intent = new Intent(this, AnimalActivity.class);
            startActivity(intent);
        }else if (v.getId() == R.id.recycle) {
            Intent intent = new Intent(this, RecycleViewActivity.class);
            startActivity(intent);
        }else if (v.getId() == R.id.brightness) {
            Intent intent = new Intent(this, ScreenBrightnessActivity.class);
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
