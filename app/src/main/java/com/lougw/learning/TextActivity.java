package com.lougw.learning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lougw.learning.utils.ImageTextUtils;

public class TextActivity extends AppCompatActivity {
    ImageView iv;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        iv = findViewById(R.id.iv);
        btn= findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv.setImageBitmap(ImageTextUtils.drawTextToImage(getApplicationContext(),R.mipmap.img_fm,"18.3"));
            }
        });

    }

}
