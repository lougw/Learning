package com.lougw.learning.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild2;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class HeadLayout extends LinearLayout implements NestedScrollingChild2 {
    public HeadLayout(Context context) {
        super(context);
    }

    public HeadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean startNestedScroll(int i, int i1) {
        return false;
    }

    @Override
    public void stopNestedScroll(int i) {

    }

    @Override
    public boolean hasNestedScrollingParent(int i) {
        return false;
    }

    @Override
    public boolean dispatchNestedScroll(int i, int i1, int i2, int i3, @Nullable int[] ints, int i4) {
        return false;
    }

    @Override
    public boolean dispatchNestedPreScroll(int i, int i1, @Nullable int[] ints, @Nullable int[] ints1, int i2) {
        return false;
    }
}
