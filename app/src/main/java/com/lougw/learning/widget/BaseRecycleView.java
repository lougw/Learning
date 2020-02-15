package com.lougw.learning.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class BaseRecycleView extends RecyclerView {

    public BaseRecycleView(@NonNull Context context) {
        super(context);
    }

    public BaseRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseRecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        final int action = e.getActionMasked();

        Log.d("lougao", "onTouchEvent: action :"+action);

        return super.onTouchEvent(e);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        final int action = e.getActionMasked();

        Log.d("lougao", "onInterceptTouchEvent: action :"+action);

        return super.onInterceptTouchEvent(e);
    }
}
