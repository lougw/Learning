package com.lougw.wheelview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.OverScroller;

import java.util.List;

/**
 * Created by kyle on 15/11/9.
 */
public class WheelView extends View implements GestureDetector.OnGestureListener {
    public static final float DEFAULT_MARK_RATIO = 0.7f;

    private Paint mMarkPaint;
    private TextPaint mMarkTextPaint;
    private int mCenterIndex;

    private int mHighlightColor, mMarkTextColor;
    private int mMarkColor;

    private int mHeight;
    private List<String> mItems;
    private OnWheelItemSelectedListener mOnWheelItemSelectedListener;
    private float mMarkRatio = DEFAULT_MARK_RATIO;

    private int mMarkCount;
    //	private Path mCenterIndicatorPath = new Path();
    private int mViewScopeSize;

    // scroll control args ---- start
    private OverScroller mScroller;
    private float mMaxOverScrollDistance;
    private RectF mContentRectF;
    private boolean mFling = false;
    private float mCenterTextSize, mNormalTextSize;
    private float mIntervalDis;
    private float mCenterMarkWidth;
    private GestureDetectorCompat mGestureDetectorCompat;
    // scroll control args ---- end


    public WheelView(Context context) {
        super(context);
        init(null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    protected void init(AttributeSet attrs) {
        float density = getResources().getDisplayMetrics().density;
        mCenterMarkWidth = (int) (density * 1.5f + 0.5f);

        mHighlightColor = 0xFFF74C39;
        mMarkTextColor = 0xFF666666;
        mMarkColor = 0xFFEEEEEE;
        mCenterTextSize = density * 40;
        mNormalTextSize = density * 18;

        TypedArray ta = attrs == null ? null : getContext().obtainStyledAttributes(attrs, R.styleable.WheelView);
        if (ta != null) {
            mHighlightColor = ta.getColor(R.styleable.WheelView_lwvHighlightColor, mHighlightColor);
            mMarkTextColor = ta.getColor(R.styleable.WheelView_lwvMarkTextColor, mMarkTextColor);
            mMarkRatio = ta.getFloat(R.styleable.WheelView_lwvMarkRatio, mMarkRatio);
            mCenterTextSize = ta.getDimension(R.styleable.WheelView_lwvCenterMarkTextSize, mCenterTextSize);
            mNormalTextSize = ta.getDimension(R.styleable.WheelView_lwvMarkTextSize, mNormalTextSize);
        }
        mMarkRatio = Math.min(1, mMarkRatio);

        mMarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMarkTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mMarkTextPaint.setTextAlign(Paint.Align.CENTER);
        mMarkTextPaint.setColor(mHighlightColor);

        mMarkPaint.setColor(mMarkColor);
        mMarkPaint.setStrokeWidth(mCenterMarkWidth);

        mMarkTextPaint.setTextSize(mCenterTextSize);
        calcIntervalDis();

        mScroller = new OverScroller(getContext());
        mContentRectF = new RectF();

        mGestureDetectorCompat = new GestureDetectorCompat(getContext(), this);

        selectIndex(0);
    }

    /**
     * calculate interval distance between items
     */
    private void calcIntervalDis() {
        mIntervalDis = 162;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int measureMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureSize = MeasureSpec.getSize(widthMeasureSpec);
        int result = getSuggestedMinimumWidth();
        switch (measureMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = measureSize;
                break;
            default:
                break;
        }
        return result;
    }

    private int measureHeight(int heightMeasure) {
        int measureMode = MeasureSpec.getMode(heightMeasure);
        int measureSize = MeasureSpec.getSize(heightMeasure);
        int result = (int) ( mCenterTextSize);
        switch (measureMode) {
            case MeasureSpec.EXACTLY:
                result = Math.max(result, measureSize);
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(result, measureSize);
                break;
            default:
                break;
        }
        return result;
    }

    public void fling(int velocityX, int velocityY) {
        mScroller.fling(getScrollX(), getScrollY(), velocityX, velocityY, (int) -mMaxOverScrollDistance, (int) (mContentRectF.width() - mMaxOverScrollDistance), 0, 0, (int) mMaxOverScrollDistance, 0);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            mHeight = h;
            mMaxOverScrollDistance = w / 2.f;
            mContentRectF.set(0, 0, (mMarkCount - 1) * mIntervalDis, h);
            mViewScopeSize = (int) Math.ceil(mMaxOverScrollDistance / mIntervalDis);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int start = mCenterIndex - mViewScopeSize;
        int end = mCenterIndex + mViewScopeSize + 1;

        start = Math.max(start, -mViewScopeSize * 2);
        end = Math.min(end, mMarkCount + mViewScopeSize * 2);

        // extends both ends
        if (mCenterIndex == mMarkCount - 1) {
            end += mViewScopeSize;
        } else if (mCenterIndex == 0) {
            start -= mViewScopeSize;
        }

        float x = start * mIntervalDis;

        Log.d("LgwTag", "start : " + start + " end : " + end + " mCenterIndex : " + mCenterIndex + " mMarkCount : " + mMarkCount);

        for (int i = start; i < end; i++) {
            // mark text
            if (mMarkCount > 0 && i >= 0 && i < mMarkCount) {
                CharSequence temp = mItems.get(i);

                if (mCenterIndex == i) {
                    mMarkTextPaint.setColor(mHighlightColor);
                    mMarkTextPaint.setTextSize(mCenterTextSize);
                    float baseLine = getBaseLine(mMarkTextPaint, getHeight()/2);
                    canvas.drawText(temp, 0, temp.length(), x, baseLine, mMarkTextPaint);
                } else {
                    mMarkTextPaint.setColor(mMarkTextColor);
                    mMarkTextPaint.setTextSize(mNormalTextSize);
                    float baseLine = getBaseLine(mMarkTextPaint, getHeight()/2);
                    canvas.drawText(temp, 0, temp.length(), x, baseLine, mMarkTextPaint);
                }
            }

            x += mIntervalDis;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mItems == null || mItems.size() == 0) {
            return false;
        }
        boolean ret = mGestureDetectorCompat.onTouchEvent(event);
        if (!mFling && MotionEvent.ACTION_UP == event.getAction()) {
            if (getScrollX() < -mMaxOverScrollDistance) {
                mScroller.startScroll(getScrollX(), 0, (int) -mMaxOverScrollDistance - getScrollX(), 0);
                invalidate();
                ret = true;
            } else if (getScrollX() > mContentRectF.width() - mMaxOverScrollDistance) {
                mScroller.startScroll(getScrollX(), 0, (int) (mContentRectF.width() - mMaxOverScrollDistance) - getScrollX(), 0);
                invalidate();
                ret = true;
            } else {
                autoSettle();
                ret = true;
            }
        }
        return ret || super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            refreshCenter();
            invalidate();
        } else {
            if (mFling) {
                mFling = false;
                autoSettle();
            }
        }
    }

    private void autoSettle() {
        int sx = getScrollX();
        float dx = mCenterIndex * mIntervalDis - sx - mMaxOverScrollDistance;
        mScroller.startScroll(sx, 0, (int) dx, 0);
        invalidate();
    }

    private void refreshCenter(int offsetX) {
        Log.d("LgwTag", "offsetX : " + offsetX + " mMaxOverScrollDistance: " + mMaxOverScrollDistance);

        int offset = (int) (offsetX + mMaxOverScrollDistance);
        Log.d("LgwTag", "offset : " + offset);
        mCenterIndex = Math.round(offset / mIntervalDis);
        Log.d("LgwTag", "mCenterIndex : " + mCenterIndex + " mIntervalDis : " + mIntervalDis);
        if (mCenterIndex < 0) {
            mCenterIndex = 0;
        } else if (mCenterIndex > mMarkCount - 1) {
            mCenterIndex = mMarkCount - 1;
        }
        Log.d("LgwTag", "mCenterIndex : " + mCenterIndex);
        if (null != mOnWheelItemSelectedListener) {
            mOnWheelItemSelectedListener.onWheelItemSelected(mCenterIndex);
        }
    }

    private void refreshCenter() {
        refreshCenter(getScrollX());
    }

    public void selectIndex(int index) {
        mCenterIndex = index;
        post(new Runnable() {
            @Override
            public void run() {
                scrollTo((int) (mCenterIndex * mIntervalDis - mMaxOverScrollDistance), 0);
                invalidate();
                refreshCenter();
            }
        });
    }

    public void smoothSelectIndex(int index) {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
        int deltaIndex = index - mCenterIndex;
        mScroller.startScroll(getScrollX(), 0, (int) (deltaIndex * mIntervalDis), 0);
        invalidate();
    }

    public List<String> getItems() {
        return mItems;
    }

    public void setItems(List<String> items) {
        mItems = items;
        mMarkCount = null == mItems ? 0 : mItems.size();
        mCenterIndex = Math.min(mCenterIndex, mMarkCount);
        calcIntervalDis();
        invalidate();
    }

    public int getSelectedPosition() {
        return mCenterIndex;
    }

    public void setOnWheelItemSelectedListener(OnWheelItemSelectedListener onWheelItemSelectedListener) {
        mOnWheelItemSelectedListener = onWheelItemSelectedListener;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (!mScroller.isFinished()) {
            mScroller.forceFinished(false);
        }
        mFling = false;
        if (null != getParent()) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        playSoundEffect(SoundEffectConstants.CLICK);
        refreshCenter((int) (getScrollX() + e.getX() - mMaxOverScrollDistance));
        autoSettle();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float dis = distanceX;
        float scrollX = getScrollX();
        if (scrollX < 2 * -mMaxOverScrollDistance) {
            dis = 0;
        } else if (scrollX < -mMaxOverScrollDistance) {
            dis = distanceX / 4.f;
        } else if (scrollX > mContentRectF.width()) {
            dis = 0;
        } else if (scrollX > mContentRectF.width() - mMaxOverScrollDistance) {
            dis = distanceX / 4.f;
        }
        scrollBy((int) dis, 0);
        refreshCenter();
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float scrollX = getScrollX();
        if (scrollX < -mMaxOverScrollDistance || scrollX > mContentRectF.width() - mMaxOverScrollDistance) {
            return false;
        } else {
            mFling = true;
            fling((int) -velocityX, 0);
            return true;
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.index = getSelectedPosition();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        selectIndex(ss.index);
        requestLayout();
    }

    static class SavedState extends BaseSavedState {
        int index;
        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            index = (int) in.readValue(null);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(index);
        }

        @Override
        public String toString() {
            return "WheelView.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " index=" + index + "}";
        }
    }

    public interface OnWheelItemSelectedListener {
        void onWheelItemSelected(int position);
    }
    private float getBaseLine(Paint paint, float centerY) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        return centerY - (fontMetrics.bottom + fontMetrics.top) / 2;
    }

}
