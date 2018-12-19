package com.lougw.learning;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.lougw.learning.widget.CMWebView;

public class WebViewActivity extends AppCompatActivity {
    private FrameLayout webContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webContainer = (FrameLayout) findViewById(R.id.web_container);
        CMWebView webView = new CMWebView(getApplication());
        webContainer.addView(webView);
        webView.loadUrl("https://www.baidu.com");
    }
}
