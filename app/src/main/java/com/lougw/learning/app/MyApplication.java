package com.lougw.learning.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.lougw.downloader.DownloadBuilder;
import com.lougw.downloader.Downloader;
import com.lougw.downloader.utils.DownloadUtils;
import com.lougw.learning.utils.AndFixPathManager;
import com.lougw.learning.utils.Installation;

/**
 * Created by lougw on 18-3-16.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        Installation.id(this);
        initAndFix();
        Downloader.getInstance().init(this, new DownloadBuilder().poolSize(3).localDir(DownloadUtils.getDownLoadFileHome()));
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
