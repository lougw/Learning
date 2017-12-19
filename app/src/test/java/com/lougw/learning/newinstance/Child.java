package com.lougw.learning.newinstance;

import android.util.Log;
import android.util.Printer;

/**
 * <pre>
 *     author : lougw
 *     e-mail : gaoweilou@pptv.com
 *     time   : 2017/12/18
 *     desc   :
 * </pre>
 */

public class Child implements  Persion {
    @Override
    public void work() {
        //Log.d("Persion","上学");
System.out.print("上学");
    }
}
