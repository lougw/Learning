package com.lougw.learning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lougw.learning.widget.ShimmerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShimmerActivity extends AppCompatActivity {
    @BindView(R.id.shimmer)
    ShimmerView shimmerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shimmer);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.start, R.id.stop})
    public void Click(View v) {
        if (v.getId() == R.id.start) {
            shimmerView.startShimmerAnimation();
        } else {
            shimmerView.stopShimmerAnimation();
        }


    }
}
