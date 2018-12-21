package com.lougw.learning.app;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.lougw.downloader.DownloadBuilder;
import com.lougw.downloader.Downloader;
import com.lougw.downloader.utils.DownloadUtils;
import com.lougw.learning.BuildConfig;
import com.lougw.learning.utils.AndFixPathManager;
import com.lougw.learning.utils.Installation;
import com.lougw.learning.utils.UIUtils;

/**
 * Created by lougw on 18-3-16.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        UIUtils.init(this);
//        Installation.id(this);
        enabledStrictMode();
        initAndFix();
        Downloader.getInstance().init(this, new DownloadBuilder().poolSize(3).localDir(DownloadUtils.getDownLoadFileHome()));
    }

    /**
     * StrictMode 测出一些不合理的代码块
     */
    private void enabledStrictMode() {
        if (BuildConfig.DEBUG) {
            /**
             线程策略（ThreadPolicy）
             线程策略主要包含了以下几个方面
             detectNetwork：监测主线程使用网络（重要）
             detectCustomSlowCalls：监测自定义运行缓慢函数
             penaltyLog：输出日志
             penaltyDialog：监测情况时弹出对话框
             detectDiskReads：检测在UI线程读磁盘操作 （重要）
             detectDiskWrites：检测在UI线程写磁盘操作（重要）
             detectResourceMismatches：检测发现资源不匹配 （api>22）
             detectAll：检测所有支持检测等项目（如果太懒，不想一一列出来，可以通过这个方式）
             permitDiskReads：允许UI线程在磁盘上读操作
             虚拟机策略（VmPolicy）
             虚拟机策略主要包含了以下几个方面
             detectActivityLeaks：检测Activity 的内存泄露情况（重要）（api>10）
             detectCleartextNetwork：检测明文的网络 （api>22）
             detectFileUriExposure：检测file://或者是content:// (api>17)
             detectLeakedClosableObjects：检测资源没有正确关闭（重要）（api>10）
             detectLeakedRegistrationObjects：检测BroadcastReceiver、ServiceConnection是否被释放 （重要）（api>15）
             detectLeakedSqlLiteObjects：检测数据库资源是否没有正确关闭（重要）(api>8)
             setClassInstanceLimit：设置某个类的同时处于内存中的实例上限，可以协助检查内存泄露（重要）
             penaltyLog：输出日志
             penaltyDeath：一旦检测到应用就会崩溃**/
            // 针对线程的相关策略
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectCustomSlowCalls()//监测自定义运行缓慢函数
                    .detectDiskReads()// 检测在UI线程读磁盘操作
                    .detectDiskWrites()// 检测在UI线程写磁盘操作
                    .detectNetwork()
                    .penaltyLog()//写入日志
                    .build());
            // 针对VM的相关策略
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .detectActivityLeaks()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
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
