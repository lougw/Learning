package com.lougw.learning.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class CFrameLayout extends FrameLayout {
    public CFrameLayout(Context context) {
        super(context);
    }

    public CFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchSetPressed(boolean pressed) {
        super.dispatchSetPressed(pressed);
    }
}
