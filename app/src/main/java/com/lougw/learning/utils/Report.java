package com.lougw.learning.utils;

import android.util.Log;
import android.view.View;

/**
 * <pre>
 *     author : lougw
 *     e-mail : gaoweilou@pptv.com
 *     time   : 2017/12/23
 *     desc   :
 * </pre>
 */

public class Report {

    private Report() {
    }

    private static class Instance {
        private static Report instance = new Report();
    }

    public static Report getInstance() {
        return Instance.instance;
    }

    public void report(View view) {
        Log.d("654321", "1111111111111111111111  : " + UIUtils.getResourceEntryName(view.getId()));
    }

}


