package com.lougw.learning;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lougw.learning.utils.AndFixPathManager;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AndFixActivity extends AppCompatActivity {
    String mPatchDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_and_fix);
        ButterKnife.bind(this);
        mPatchDir = getExternalCacheDir().getAbsolutePath() + "/apptch/";
        File file = new File(mPatchDir);
        if (file == null || !file.exists()) {
            file.mkdir();
        }

    }


    @OnClick({R.id.btn_bug, R.id.btn_fix})
    public void Click(View v) {
        if (v.getId() == R.id.btn_bug) {
            String log ="success";
            Log.d("AndFixActivity", log);

        } else if (v.getId() == R.id.btn_fix) {
            Log.d("AndFixActivity", "fix1");
            AndFixPathManager.getInstance().addPath(mPatchDir+"patch.apatch");

            Log.d("AndFixActivity", "fix");
        }

    }
}
