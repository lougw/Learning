package com.lougw.learning.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class ExtendHeadLayout extends FrameLayout {
    private View mExtendHead;
    private RecyclerView mRecyclerView;
    private boolean isExtendHead = true;
    private int mExtendHeadHeight = 0;

    public ExtendHeadLayout(@NonNull Context context) {
        super(context);
    }

    public ExtendHeadLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtendHeadLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() >= 2) {
            mExtendHead = getChildAt(1);
            mRecyclerView = (RecyclerView) getChildAt(0);
            mRecyclerView.addOnScrollListener(mOnScrollListener);
        }
    }

    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (!isExtendHead) {
                return;
            }
            float TranslationY = mExtendHead.getTranslationY();
            float ScrollY = TranslationY - dy;
            if (ScrollY >= 0) {
                ScrollY = 0;
            }
            if (ScrollY <= -mExtendHeadHeight) {
                ScrollY = -mExtendHeadHeight;
            }
            Log.d("LgwTag", "  ScrollY : " + ScrollY);
            mExtendHead.setTranslationY(ScrollY);
        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mExtendHead != null && mRecyclerView != null) {
            mExtendHeadHeight = mExtendHead.getMeasuredHeight();
            mRecyclerView.setPadding(0, mExtendHeadHeight, 0, 0);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mRecyclerView != null) {
            mRecyclerView.removeOnScrollListener(mOnScrollListener);
            mRecyclerView = null;
        }
    }

    public void setExtendHead(boolean extendHead) {
        isExtendHead = extendHead;
        if (!isExtendHead) {
            mExtendHead.setTranslationY(0);
        }
    }
}
