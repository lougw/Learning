package com.lougw.learning;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lougw.learning.utils.UIUtils;

public class AndroidViewActivity extends AppCompatActivity {
    FrameLayout parent_002;
    TextView tv_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_view);
        parent_002 = (FrameLayout) findViewById(R.id.parent_002);
        tv_test = (TextView) findViewById(R.id.tv_test);
        findViewById(R.id.btn_test1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent_002.setVisibility(View.INVISIBLE);
            }
        });
        findViewById(R.id.btn_test2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isViewVisibility(tv_test);
            }
        });


    }


    private boolean isViewVisibility(View view) {
        View current = view;
        do {
            Log.d("LgwTag", "isViewVisibility() " + UIUtils.getResourceEntryName(current.getId())+"  "+current.getVisibility());
//            if (current.getVisibility() != View.VISIBLE) {
//                return false;
//            }
            ViewParent parent = current.getParent();
            if (parent == null) {
                return false;
            }
            if (!(parent instanceof View)) {
                return true;
            }
            current = (View) parent;
        } while (current != null);
        return false;
    }
}
