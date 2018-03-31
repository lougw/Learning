package com.lougw.learning.app;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.lougw.learning.utils.AndFixPathManager;

/**
 * Created by lougw on 18-3-16.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initAndFix();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);

    }

    /**
     * 初始化AndFix
     */
    private void initAndFix() {
        AndFixPathManager.getInstance().initPatch(this);
    }
}
