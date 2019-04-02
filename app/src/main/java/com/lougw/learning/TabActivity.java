package com.lougw.learning;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.lougw.learning.widget.tab.TabLayout;

public class TabActivity extends AppCompatActivity {
TabLayout  tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        tabLayout=findViewById(R.id.tab);
        tabLayout.setIndicatorWidthWrapContent(true);
//        tabLayout.setSelectedTabIndicatorWidth(10);
        for (int i = 0; i < 40; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View view = View.inflate(getApplicationContext(), R.layout.episode_tab_item, null);
            TextView title = view.findViewById(R.id.title);
            title.setText("tabtest"+i);
            tab.setCustomView(view);
            tabLayout.addTab(tab);
        }
    }

}
