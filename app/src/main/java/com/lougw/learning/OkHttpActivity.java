package com.lougw.learning;

import android.icu.util.TimeUnit;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http);
        OkHttpClient  client=new OkHttpClient.Builder().readTimeout(5, java.util.concurrent.TimeUnit.SECONDS).build();

        Request  request=new Request.Builder().url("").get().build();
        Call  call=client.newCall(request);
        try {
            Response response=call.execute();
        call.enqueue(new Callback() {
           @Override
           public void onFailure(Call call, IOException e) {

           }

           @Override
           public void onResponse(Call call, Response response) throws IOException {

           }
       });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
