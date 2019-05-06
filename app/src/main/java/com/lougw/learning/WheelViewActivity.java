package com.lougw.learning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lougw.learning.utils.FMConst;
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
        int size=(108000-87500)/100;
        for (int i = 1; i <= size; i++) {
            items.add(showString(i));
        }

        mWheelView.setItems(items);
        mWheelView.selectIndex(8);

    }
    private String showString(int index) {
        int khz = 87500 + index * 100;
        return FMConst.kHz2mHz(khz);
    }
}
