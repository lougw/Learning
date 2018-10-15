package com.lougw.learning.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

public class CustomSeekBar extends View {

    private Scroller mScroller;

    private Rect mPaddingRect;
    private Rect mCursorRect;
    private RectF mSeekBarRect;
    private RectF mSeekBarRectSelected;
    private CharSequence[] mTextArray;
    private float mCursorIndex = 1.0f;
    private Paint mPaint;
    /**
     * 文字大小
     */
    private int mTextSize = 12;

    private float mDensity;

    private int mTextColorNormal;
    private int mTextColorSelected;
    private int mSeekBarColorSelected;

    private float[] mTextWidthArray;

    private Rect[] mClickRectArray;


    /**
     * 刻度之间的宽度
     */
    private int mPartLength;

    private float mTextDrawBottom;

    private int mTextHeight;
    private int mGap;

    /**
     * Hseekbar 的高度
     */
    private int mSeekBarHeight;


    private int mMarkSize;

    public CustomSeekBar(Context context) {
        super(context);
    }

    public CustomSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mCursorRect = new Rect();
        mSeekBarRect = new RectF();
        mSeekBarRectSelected = new RectF();
        mScroller = new Scroller(context, new DecelerateInterpolator());
        mTextArray = new String[]{"0", "5", "10", "15", "20"};
        mDensity = getContext().getResources().getDisplayMetrics().density;

        setWillNotDraw(false);
        setFocusable(true);
        setClickable(true);
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(mTextSize);
        mPaint.setStrokeWidth(0.1f * mDensity);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*** 把数字放到seekBar上去 ***/
        final int length = mTextArray.length;
        mPaint.setTextSize(mTextSize);
        for (int i = 0; i < length; i++) {
            if ((i > 0 && i < mCursorIndex) || i == mCursorIndex) {
                mPaint.setColor(mTextColorSelected);
            } else {
                mPaint.setColor(mTextColorNormal);
            }

            final String text2draw = mTextArray[i].toString();
            final float textWidth = mTextWidthArray[i];

            float markX = mSeekBarRect.left + i * mPartLength;
            if (i == 0) markX += mPaint.getStrokeWidth();
            else if (i == length - 1) markX -= mPaint.getStrokeWidth();
            float textDrawLeft = markX - textWidth / 2f;
            canvas.drawText(text2draw, textDrawLeft, mTextDrawBottom, mPaint);
            canvas.drawLine(markX, mTextDrawBottom + mGap, markX, mTextDrawBottom + mGap + mMarkSize, mPaint);

            Rect rect = mClickRectArray[i];
            if (rect == null) {
                rect = new Rect();
                rect.top = mPaddingRect.top;
                rect.bottom = rect.top + mTextHeight + mGap + mSeekBarHeight;
                rect.left = (int) textDrawLeft;
                rect.right = (int) (rect.left + textWidth);
                mClickRectArray[i] = rect;
            }
        }
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            final int deltaX = mScroller.getCurrX();
            mCursorIndex = (float) deltaX / mPartLength;
            invalidate();
        }
        super.computeScroll();
    }

}
