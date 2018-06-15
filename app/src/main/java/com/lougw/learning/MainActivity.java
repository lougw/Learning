package com.lougw.learning;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lougw.learning.net.NetActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.net)
    Button btnNet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.d("Main1111111", "onCreate");
    }

    @OnClick({R.id.net, R.id.shimmer, R.id.full_screen, R.id.web_server, R.id.rxjava, R.id.andfix, R.id.network})
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
        }else if (v.getId() == R.id.network) {
            Intent intent = new Intent(this, NetActivity.class);
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
}
