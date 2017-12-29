package com.lougw.learning;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.lougw.learning.net.NetActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.net) Button btnNet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.net,R.id.shimmer})
    public void Click(View v) {
        if (v.getId() == R.id.net) {
            Intent intent = new Intent(this, NetActivity.class);
            startActivity(intent);
        }else if (v.getId() == R.id.shimmer) {
            Intent intent = new Intent(this, ShimmerActivity.class);
            startActivity(intent);
        }

    }
}
