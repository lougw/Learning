package com.lougw.learning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lougw.wheelview.WheelView;

import java.util.ArrayList;
import java.util.List;

public class WheelViewActivity extends AppCompatActivity {
    private WheelView mWheelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_view);

        mWheelView = (WheelView) findViewById(R.id.wheelview);
        final List<String> items = new ArrayList<>();
        for (int i = 1; i <= 40; i++) {
            items.add(String.valueOf(i * 1000));
        }

        mWheelView.setItems(items);
        mWheelView.selectIndex(8);

    }

}
