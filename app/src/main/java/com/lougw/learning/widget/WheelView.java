package com.lougw.learning.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import com.lougw.learning.R;
import com.lougw.learning.utils.FMConst;

public class WheelView extends View {

    private int mLow = 87500;
    private int mHigh = 108000; // 最低,最高的频率范围, 不需要存 list.
    private int mFrameWidth = 1288;
    private int mFrameHeight = 400;
    private int mFrameTop = 400;
    private int mFrameLeft = 400;

    private int mLineWidth = 80;
    private int mLineHeight = 4;

    private int mMarkCount;

    private final OverScroller mScroller;

    private final Paint mBgPaint;

    private final Paint mLinePaint;

    private final Paint mTextPaint;


    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new OverScroller(getContext());
        setBackgroundColor(0xCC000000);
        mBgPaint = new Paint();
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setColor(0xff1e1f21);
        mBgPaint.setAntiAlias(true);
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(4f);
        mLinePaint.setColor(0x0Dffffff);
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setStrokeWidth(5f);
        Typeface tf = context.getResources().getFont(R.font.barlow_condensed_regular);
        mTextPaint.setTypeface(tf);
        mTextPaint.setTextSize(80);
        setClickable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerY = getMeasuredHeight() / 2;
        int frameRight = mFrameLeft + mFrameWidth;
        canvas.drawRoundRect(mFrameLeft, mFrameTop, frameRight, mFrameTop + mFrameHeight, 30, 30, mBgPaint);

        canvas.drawLine(mFrameLeft + 80, centerY, mFrameLeft + mLineWidth + 80, centerY, mLinePaint);

        canvas.drawLine(frameRight - mLineWidth - 80, centerY, frameRight - 80, centerY, mLinePaint);



        float baseLine;

        baseLine = getBaseLine(mTextPaint, getHeight()/2);

        canvas.drawText(showString(10), getWidth()/2, baseLine, mTextPaint);
        mTextPaint.setTextSize(48);
        baseLine = getBaseLine(mTextPaint, getHeight()/2);
        canvas.drawText(showString(9), getWidth()/2-196, baseLine, mTextPaint);
        canvas.drawText(showString(8), getWidth()/2-358, baseLine, mTextPaint);
        canvas.drawText(showString(11), getWidth()/2+196, baseLine, mTextPaint);
        canvas.drawText(showString(12), getWidth()/2+358, baseLine, mTextPaint);



    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mFrameTop = (h - mFrameHeight) / 2;
        mFrameLeft = (w - mFrameWidth) / 2;
        Log.d("LgwTag", "w : " + w + "  h : " + h);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //父容器禁止拦截
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
//            if(事件交给父容器的条件) {
//            }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private String showString(int index) {
        int khz = mLow + index * 100;
        return FMConst.kHz2mHz(khz);
    }

    private float getBaseLine(Paint paint, float centerY) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        return centerY - (fontMetrics.bottom + fontMetrics.top) / 2;
    }

}
