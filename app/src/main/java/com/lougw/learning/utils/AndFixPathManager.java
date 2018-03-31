package com.lougw.learning.utils;

import android.content.Context;

import com.alipay.euler.andfix.patch.PatchManager;

import java.io.IOException;

/**
 * Created by lougw on 18-3-31.
 */

public class AndFixPathManager {
    public  static final  String FILE_END=".apatch";
    private static PatchManager mPatchManager = null;

    private AndFixPathManager() {

    }

    private static class InstanceHolder {
        public static final AndFixPathManager instance = new AndFixPathManager();
    }

    public static AndFixPathManager getInstance() {
        return InstanceHolder.instance;
    }

    /**
     * 初始化andfix方法
     *
     * @param context
     */
    public void initPatch(Context context) {
        mPatchManager = new PatchManager(context);
        mPatchManager.init(Integer.toString(AppUtils.getAppVersionCode(context)));//current version
        mPatchManager.loadPatch();

    }

    public void addPath(String path) {
        try {
            if (mPatchManager != null) {
                mPatchManager.addPatch(path);//path of the patch file that was downloaded
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
