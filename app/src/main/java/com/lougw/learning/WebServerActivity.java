package com.lougw.learning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.lougw.jettyserver.SpeechManager;
import com.lougw.jettyserver.WebService;

public class WebServerActivity extends AppCompatActivity implements SpeechManager.SpeechObserver {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_server);
        WebService.onStartService(this);
        SpeechManager.getInstance().registerObserver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebService.onStopService(this);
        SpeechManager.getInstance().unRegisterObserver(this);
    }

    @Override
    public void onSpeech(int page, int direction) {
        Log.d("WebServerActivity","page  "+page+"  direction  "+direction);
        Toast.makeText(this,"page  "+page+"  direction  "+direction,Toast.LENGTH_LONG).show();
    }
}
