package com.lougw.learning.net;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lougw.learning.R;
import com.lougw.net.ResponseModel;
import com.lougw.net.HttpSenderCallback;

public class NetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        RequestHelper.getSender().test(new HttpSenderCallback() {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
            }

            @Override
            public void onFail(ResponseModel error) {
                super.onFail(error);
            }
        }, "");
    }

}
