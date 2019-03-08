package com.lougw.learning;

import android.content.MutableContextWrapper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.lougw.learning.widget.CMWebView;

public class WebViewActivity extends AppCompatActivity {
    private FrameLayout webContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webContainer = (FrameLayout) findViewById(R.id.web_container);
        WebView webView = new WebView(getApplication());
        webContainer.addView(webView);
        webView.loadUrl("http://chls.pro/ssl");
//        MutableContextWrapper contextWrapper = new MutableContextWrapper(BaseApplicationImpl.sApplication);
//        mPool[0] = new WebView(contextWrapper);
//
//
//        ct =(MutableContextWrapper)webview.getContext();
//        ct.setBaseContext(getApplication());
//
//        //reuse WebView
//        ((MutableContextWrapper)webview.getContext()).setBaseContext(activityContext);
    }
}
