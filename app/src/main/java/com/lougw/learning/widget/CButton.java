package com.lougw.learning.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

public class CButton extends Button {
    public CButton(Context context) {
        super(context);
    }

    public CButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchSetPressed(boolean pressed) {
        super.dispatchSetPressed(pressed);

//        Log.d("LgwTag","pressed : "+pressed+" "+Log.getStackTraceString(new Throwable()));




    }
}
