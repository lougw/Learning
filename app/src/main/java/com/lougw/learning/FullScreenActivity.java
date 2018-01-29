package com.lougw.learning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FullScreenActivity extends AppCompatActivity implements View.OnClickListener {
    LayoutInflater mLayoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        ButterKnife.bind(this);
        mLayoutInflater = LayoutInflater.from(this);
    }

    @OnClick({R.id.full_screen})
    public void Click(View v) {
        if (v.getId() == R.id.full_screen) {
            ViewGroup vp = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
            View old = vp.findViewById(R.id.fullscreen_id);
            if (old != null) {
                vp.removeView(old);
            }
            View view = mLayoutInflater.inflate(R.layout.layout_full_screen, null, false);
            view.setId(R.id.fullscreen_id);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            vp.addView(view, lp);
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);
            view.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fullscreen_id) {
            ViewGroup vp = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
            View old = vp.findViewById(R.id.fullscreen_id);
            if (old != null) {
                vp.removeView(old);
            }
        }
    }
}
