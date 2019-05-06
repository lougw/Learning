package com.lougw.learning.utils;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;

import java.text.DecimalFormat;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * 一些常量.
 * 一些工具类
 *
 * @author dundongfang on 2018/4/3.
 */
public class FMConst {
    //无效频率, 未初始化的值.
    public static final int INVALID_RADIO_CHANNEL = -1;

    // the min size to make recycler view loop/circle.
    public static final int MIN_LOOP = 8;

    // the max size to isStar/collect station.
    public static final int MAX_STAR = 8;

    public static final int LOW_RADIO = 87500;
    public static final int HIGH_RADIO = 108000;

    public static int TUNE_SOURCE_UNKNOWN = 0;
    public static int TUNE_SOURCE_TUNE = 1;
    public static int TUNE_SOURCE_STEP = 2;
    public static int TUNE_SOURCE_INIT = 3;

    //数据库操作成功, 失败(数据库冲突等), 失败(达到上限)
    public static int OP_SUCCESS = 0;
    public static int OP_FAIL = 1;
    public static int OP_FAIL_LIMIT = 2;

    // FM的activity 是否在前台
    public static boolean sFmForeground = false;

    public static final Scheduler scheduler = Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR);

    public static final String CLOSE_ACTION = "com.chehejia.car.ACTION.CLOSE_FM";

    private static final DecimalFormat FM_FORMATTER = new DecimalFormat("###.#");

    public static final String STATION_UPDATE_ACTION = "com.chehejia.car.action.STATION_UPDATE";

    public static final String RADIO_MANAGER_ACTION = "com.chehejia.car.action.RADIO_MANAGER";

    public static String kHz2mHz(int kHz) {
        String result = FM_FORMATTER.format((float) kHz / 1000f);
        if (!result.contains(".")) {
            result += ".0";
        }
        return result;
    }

    public static String getDeviceId() {
        final int DID_LENGTH = 17;
        final String REPLACED_DID = "00000000000000000";
        @SuppressLint("MissingPermission") // system app do not need.
                String sn = Build.getSerial();
        if (sn == null) {
            return REPLACED_DID;
        }
        String snTrim = sn.trim();
        int snLen = snTrim.length();
        String deviceID = snTrim;
        if (snLen < DID_LENGTH) {
            StringBuilder stringBuilder = new StringBuilder(REPLACED_DID);
            deviceID = stringBuilder.replace(0, snLen, snTrim).toString();
        } else if (snLen > DID_LENGTH) {
            deviceID = snTrim.substring(0, DID_LENGTH);
        }
        return deviceID;

    }



}
