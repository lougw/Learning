package com.lougw.learning;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.lougw.wheelview.WheelView;

import java.util.ArrayList;
import java.util.List;

public class WheelViewActivity extends AppCompatActivity {
    private WheelView mWheelView, mWheelView2, mWheelView3, mWheelView4, mWheelView5;
    private TextView mSelectedTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_view);

        mWheelView = (WheelView) findViewById(R.id.wheelview);
        mWheelView2 = (WheelView) findViewById(R.id.wheelview2);
        mWheelView3 = (WheelView) findViewById(R.id.wheelview3);
        mWheelView4 = (WheelView) findViewById(R.id.wheelview4);
        mWheelView5 = (WheelView) findViewById(R.id.wheelview5);
        mSelectedTv = (TextView) findViewById(R.id.selected_tv);

        final List<String> items = new ArrayList<>();
        for (int i = 1; i <= 40; i++) {
            items.add(String.valueOf(i * 1000));
        }

        mWheelView.setItems(items);
        mWheelView.selectIndex(8);
        mWheelView.setAdditionCenterMark("元");

        List<String> items2 = new ArrayList<>();
        items2.add("一月");
        items2.add("二月");
        items2.add("三月");
        items2.add("四月");
        items2.add("五月");
        items2.add("六月");
        items2.add("七月");
        items2.add("八月");
        items2.add("九月");
        items2.add("十月");
        items2.add("十一月");
        items2.add("十二月");

        mWheelView2.setItems(items2);

        List<String> items3 = new ArrayList<>();
        items3.add("1");
        items3.add("2");
        items3.add("3");
        items3.add("5");
        items3.add("7");
        items3.add("11");
        items3.add("13");
        items3.add("17");
        items3.add("19");
        items3.add("23");
        items3.add("29");
        items3.add("31");

        mWheelView3.setItems(items3);
        mWheelView3.setAdditionCenterMark("m");

        mWheelView4.setItems(items);

        mWheelView5.setItems(items);
        mWheelView5.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onWheelItemSelected(int position) {
                mSelectedTv.setText("选择：" + items.get(position) + "万");
            }
        });
    }

}
