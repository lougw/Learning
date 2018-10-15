
package com.lougw.learning.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import com.lougw.learning.R;

public class RangeSeekBar extends View {

    private static final int DEFAULT_DURATION = 150;

    private static final int SENSITIVITY_IN_DP = 5;

    private static final float PADDING_LEFT = 10f;
    private static final float PADDING_TOP = 10f;
    private static final float PADDING_RIGHT = 10f;
    private static final float PADDING_BOTTOM = 10f;
    private static final float GAP = 5f;
    private static final float MARK_SIZE = 6f;
    private static final float SEEKBAR_HEIGHT = 15f;
    private static final float TEST_SIZE = 12f;
    private static final float CURSOR_WIDTH = 24f;
    private static final float CURSOR_HEIGHT = CURSOR_WIDTH;
    private static final float CURSOR_HINT_WIDTH = 42f;
    private static final float CURSOR_HINT_HEIGHT = CURSOR_HINT_WIDTH * 64f / 52f;


    private enum DIRECTION {
        LEFT, RIGHT
    }

    private int mDuration;

    /**
     * 左右两边滑动的游标
     */
    private Scroller mScroller;

    /**
     * 游标背景
     */
    private Drawable mRightCursorBG;

    /**
     * 选择状态
     */
    private int[] mPressedEnableState = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};

    /**
     * 刻度数字的选择和未选择的亚瑟变化
     */
    private int mTextColorNormal;
    private int mTextColorSelected;
    private int mSeekBarColorSelected;
    private int mSeekBarColorNormal;

    /**
     * Hseekbar 的高度
     */
    private int mSeekBarHeight;

    private int mGap;

    private int mMarkSize;
    /**
     * 文字大小
     */
    private int mTextSize;

    /**
     * 刻度之间的宽度
     */
    private int mPartLength;

    /**
     * 刻度数字
     */
    private CharSequence[] mTextArray;

    private float[] mTextWidthArray;
    private int mTextHeight;

    private Rect mPaddingRect;
    private Rect mLeftCursorRect;
    private Rect mRightCursorRect;

    private RectF mSeekBarRect;
    private RectF mSeekBarRectSelected;

    private float mRightCursorIndex = 1.0f;
    private int mRightCursorNextIndex = 1;

    private Paint mPaint;

    private int mRightPointerLastX;

    private int mRightPointerID = -1;

    private boolean mRightHited;

    private int mRightBoundary;

    private OnCursorChangeListener mListener;

    private Rect[] mClickRectArray;
    private int mClickIndex = -1;
    private int mClickDownLastX = -1;
    private int mClickDownLastY = -1;

    private float mTextDrawBottom;

    private float mDensity;

    private int mCursorH;

    private int mCursorW;

    private int mCursorHintH;


    public RangeSeekBar(Context context) {
        this(context, null, 0);
    }

    public RangeSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RangeSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


        applyConfig(context, attrs);

        mLeftCursorRect = new Rect();
        mRightCursorRect = new Rect();

        mSeekBarRect = new RectF();
        mSeekBarRectSelected = new RectF();

        mScroller = new Scroller(context, new DecelerateInterpolator());

        mDensity = getContext().getResources().getDisplayMetrics().density;

        setWillNotDraw(false);
        setFocusable(true);
        setClickable(true);

        mGap = (int) (GAP * mDensity + 0.5f);
        mMarkSize = (int) (MARK_SIZE * mDensity + 0.5f);
        mCursorH = (int) (CURSOR_HEIGHT * mDensity + 0.5f);
        mCursorW = (int) (CURSOR_WIDTH * mDensity + 0.5f);
        mCursorHintH = (int) (CURSOR_HINT_HEIGHT * mDensity + 0.5f);

        initPadding();
        initData();
        initPaint();
        initTextBoundsArray();
    }

    private void applyConfig(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RangeSeekBar);

        mDuration = a.getInteger(R.styleable.RangeSeekBar_autoMoveDuration, DEFAULT_DURATION);

        mRightCursorBG = a.getDrawable(R.styleable.RangeSeekBar_rightCursorBackground);

        if (mRightCursorBG == null)
            mRightCursorBG = context.getResources().getDrawable(R.drawable.bg_red_dot);

        mTextColorNormal = a.getColor(R.styleable.RangeSeekBar_textColorNormal, Color.parseColor("#999999"));
        mTextColorSelected = a.getColor(R.styleable.RangeSeekBar_textColorSelected, Color.parseColor("#1D1D26"));
        mSeekBarColorSelected = a.getColor(R.styleable.RangeSeekBar_SeekBarColorSelected, Color.parseColor("#FF42FF"));
        mSeekBarColorNormal = a.getColor(R.styleable.RangeSeekBar_SeekBarColorNormal, Color.parseColor("#999999"));
        mSeekBarHeight = (int) a.getDimension(R.styleable.RangeSeekBar_SeekBarHeight, SEEKBAR_HEIGHT * metrics.density);

        mTextSize = (int) a.getDimension(R.styleable.RangeSeekBar_textSize, TEST_SIZE * metrics.scaledDensity);
        mGap = (int) a.getDimension(R.styleable.RangeSeekBar_spaceBetween, GAP * metrics.density);

        mTextArray = a.getTextArray(R.styleable.RangeSeekBar_markTextArray);
        a.recycle();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);

        initPadding();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int heightNeeded = mPaddingRect.top + mCursorHintH + mGap + mTextHeight + mGap + mMarkSize + mGap + mSeekBarHeight + mGap + mCursorH + mPaddingRect.bottom;

        if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightNeeded, MeasureSpec.EXACTLY);
        }

        mTextDrawBottom = mPaddingRect.top + mCursorHintH + mGap + mTextHeight;

        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        mSeekBarRect.left = mPaddingRect.left + mCursorH / 2f;
        mSeekBarRect.right = widthSize - mPaddingRect.right - mCursorH / 2f;
        mSeekBarRect.top = mTextDrawBottom + mGap + mMarkSize + mGap;
        mSeekBarRect.bottom = mSeekBarRect.top + mSeekBarHeight / 2;

        mSeekBarRectSelected.top = mSeekBarRect.top;
        mSeekBarRectSelected.bottom = mSeekBarRect.bottom;

        mPartLength = (int) ((mSeekBarRect.right - mSeekBarRect.left) / (mTextArray.length - 1) + 0.5f);
        mRightBoundary = (int) (mSeekBarRect.right + mCursorH / 2);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initData() {
        if (mTextArray == null || mTextArray.length == 0) {
            mTextArray = new String[]{"0.5", "1", "2Min", "3", "5"};
        }
        mRightCursorIndex = mTextArray.length - 1;
        mRightCursorNextIndex = (int) mRightCursorIndex;

        mTextWidthArray = new float[mTextArray.length];
        mClickRectArray = new Rect[mTextArray.length];
    }

    private void initPadding() {
        if (mPaddingRect == null) {
            mPaddingRect = new Rect();
        }
        mPaddingRect.left = (int) (PADDING_LEFT * mDensity + getPaddingLeft() + 0.5f);
        mPaddingRect.top = (int) (PADDING_TOP * mDensity + getPaddingTop() + 0.5f);
        mPaddingRect.right = (int) (PADDING_RIGHT * mDensity + getPaddingRight() + 0.5f);
        mPaddingRect.bottom = (int) (PADDING_BOTTOM * mDensity + getPaddingBottom() + 0.5f);

    }


    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Style.FILL);
        mPaint.setTextSize(mTextSize);
        mPaint.setStrokeWidth(0.1f * mDensity);
    }

    private void initTextBoundsArray() {
        if (mTextArray != null && mTextArray.length > 0) {
            final int length = mTextArray.length;
            for (int i = 0; i < length; i++) {
                mTextWidthArray[i] = mPaint.measureText(mTextArray[i].toString());
            }
        }

        FontMetrics fm = mPaint.getFontMetrics();
        mTextHeight = (int) Math.ceil(fm.leading - fm.ascent) - 2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*** 把数字放到seekBar上去 ***/
        final int length = mTextArray.length;
        mPaint.setTextSize(mTextSize);
        for (int i = 0; i < length; i++) {
            if (i == mRightCursorIndex) {
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
            mPaint.setStrokeWidth((float) 2.0);
            canvas.drawLine(markX, mTextDrawBottom + mGap, markX, mSeekBarRect.bottom, mPaint);

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

        /*** 画 seekbar ***/
        mSeekBarRectSelected.left = mSeekBarRect.left + mPartLength * 0;
        mSeekBarRectSelected.right = mSeekBarRect.left + mPartLength * mRightCursorIndex;

        mPaint.setColor(mSeekBarColorNormal);
        mPaint.setStyle(Style.FILL);
        canvas.drawRect(mSeekBarRect, mPaint);
        mPaint.setStyle(Style.FILL);

        mPaint.setColor(mSeekBarColorSelected);
        canvas.drawRect(mSeekBarRectSelected, mPaint);


        /*** 画游标 ***/
        // left cursor first
        final int leftWidth = mCursorW;
        final int leftHieght = mCursorH;
        final int leftLeft = (int) (mSeekBarRectSelected.left - (float) leftWidth / 2);
        final int leftTop = (int) (mSeekBarRect.bottom + mGap + 0.5f);
        mLeftCursorRect.left = leftLeft;
        mLeftCursorRect.top = leftTop;
        mLeftCursorRect.right = leftLeft + leftWidth;
        mLeftCursorRect.bottom = leftTop + leftHieght;

        // right cursor second
        final int rightWidth = mCursorW;
        final int rightLeft1 = (int) (mSeekBarRectSelected.right - (float) rightWidth / 2f);
        int right = (int) (rightLeft1 <= mPaddingRect.left ? mPaddingRect.left : rightLeft1);
        mRightCursorRect.left = right;
        mRightCursorRect.top = (int) (mSeekBarRect.bottom - mCursorH / 2);
        mRightCursorRect.right = right + rightWidth;
        mRightCursorRect.bottom = (int) (mSeekBarRect.bottom + mCursorH / 2);
        mRightCursorBG.setBounds(mRightCursorRect);
        mRightCursorBG.draw(canvas);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getParent() != null && mRightHited) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        // For multiple touch
        final int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                handleTouchDown(event);

                break;
            case MotionEvent.ACTION_POINTER_DOWN:

                handleTouchDown(event);

                break;
            case MotionEvent.ACTION_MOVE:

                handleTouchMove(event);

                break;
            case MotionEvent.ACTION_POINTER_UP:

                handleTouchUp(event);

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                handleTouchUp(event);
                mClickIndex = -1;
                mClickDownLastX = -1;
                mClickDownLastY = -1;

                break;
        }

        return super.onTouchEvent(event);
    }

    private void handleTouchDown(MotionEvent event) {
        final int actionIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int downX = (int) event.getX(actionIndex);
        final int downY = (int) event.getY(actionIndex);


        float result = mDensity * SENSITIVITY_IN_DP;
        int space = (int) (result + 0.5f);
        Rect right = new Rect(mRightCursorRect.left - space, mRightCursorRect.top - space, mRightCursorRect.right + space, mRightCursorRect.bottom + space);
        if (right.contains(downX, downY)) {
            if (mRightHited) {
                return;
            }

            mRightPointerLastX = downX;
            mRightCursorBG.setState(mPressedEnableState);
            mRightPointerID = event.getPointerId(actionIndex);
            mRightHited = true;
            Log.d("32323239", "mRightCursorIndex : " + mRightCursorIndex + "   " + Log.getStackTraceString(new Throwable()));

            invalidate();
        } else {
            // If touch x-y not be contained in cursor,
            // then we check if it in click areas
            final int clickBoundaryTop = mClickRectArray[0].top;
            final int clickBoundaryBottom = mClickRectArray[0].bottom;
            mClickDownLastX = downX;
            mClickDownLastY = downY;

            // Step one : if in boundary of total Y.
            if (downY < clickBoundaryTop || downY > clickBoundaryBottom) {
                mClickIndex = -1;
                return;
            }

            // Step two: find nearest mark in x-axis
            final int partIndex = (int) ((downX - mSeekBarRect.left) / mPartLength);
            final int partDelta = (int) ((downX - mSeekBarRect.left) % mPartLength);
            if (partDelta < mPartLength / 2) {
                mClickIndex = partIndex;
            } else if (partDelta > mPartLength / 2) {
                mClickIndex = partIndex + 1;
            }

            if (mClickIndex == mRightCursorIndex) {
                mClickIndex = -1;
                return;
            }

            // Step three: check contain
            if (mClickIndex != -1 && !mClickRectArray[mClickIndex].contains(downX, downY)) {
                mClickIndex = -1;
            }
        }

    }

    private void handleTouchUp(MotionEvent event) {
        final int actionIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int actionID = event.getPointerId(actionIndex);

        if (actionID == mRightPointerID) {
            if (!mRightHited) {
                return;
            }

            final int lower = (int) Math.floor(mRightCursorIndex);
            final int higher = (int) Math.ceil(mRightCursorIndex);

            final float offset = mRightCursorIndex - lower;
            if (offset > 0.5f) {
                mRightCursorNextIndex = higher;
            } else if (offset < 0.5f) {
                mRightCursorNextIndex = lower;
            }

            if (!mScroller.computeScrollOffset()) {
                final int fromX = (int) (mRightCursorIndex * mPartLength);
                mScroller.startScroll(fromX, 0, mRightCursorNextIndex * mPartLength - fromX, 0, mDuration);
                triggerCallback(mRightCursorNextIndex);
            }

            if (mRightCursorIndex > 0.5 && mRightCursorIndex < 1.5) {
                mRightCursorIndex = 1;
            }

            if (mRightCursorIndex >= 1.5 && mRightCursorIndex < 2.5) {
                mRightCursorIndex = 2;
            }

            if (mRightCursorIndex >= 2.5 && mRightCursorIndex < 3.5) {
                mRightCursorIndex = 3;
            }

            if (mRightCursorIndex >= 3.5 && mRightCursorIndex < 4.5) {
                mRightCursorIndex = 4;
            }

            if (mRightCursorIndex >= 4.5 && mRightCursorIndex < 5.5) {
                mRightCursorIndex = 5;
            }
            if (mRightCursorIndex >= 5.5 && mRightCursorIndex < 6.5) {
                mRightCursorIndex = 6;
            }
            if (mRightCursorIndex >= 6.5 && mRightCursorIndex < 7.5) {
                mRightCursorIndex = 7;
            }

            if (mRightCursorIndex >= 7.5 && mRightCursorIndex < 8.5) {
                mRightCursorIndex = 8;
            }
            if (mRightCursorIndex >= 8.5 && mRightCursorIndex < 9.5) {
                mRightCursorIndex = 9;
            }
            mRightPointerLastX = 0;
            mRightPointerID = -1;
            mRightHited = false;

            invalidate();
        } else {
            final int pointerIndex = event.findPointerIndex(actionID);
            final int upX = (int) event.getX(pointerIndex);
            final int upY = (int) event.getY(pointerIndex);

            if (mClickIndex != -1 && mClickRectArray[mClickIndex].contains(upX, upY)) {
                if (!mScroller.computeScrollOffset()) {
                    mRightCursorNextIndex = mClickIndex;
                    int fromX = (int) (mRightCursorIndex * mPartLength);
                    mScroller.startScroll(fromX, 0, mRightCursorNextIndex * mPartLength - fromX, 0, mDuration);

                    triggerCallback(mRightCursorNextIndex);

                    invalidate();
                }

            }
        }

    }

    private void handleTouchMove(MotionEvent event) {
        if (mClickIndex != -1) {
            final int actionIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
            final int x = (int) event.getX(actionIndex);
            final int y = (int) event.getY(actionIndex);

            if (!mClickRectArray[mClickIndex].contains(x, y)) {
                mClickIndex = -1;
            }
        }

        if (mRightHited && mRightPointerID != -1) {

            final int index = event.findPointerIndex(mRightPointerID);
            final float x = event.getX(index);

            float deltaX = x - mRightPointerLastX;
            mRightPointerLastX = (int) x;

            DIRECTION direction = (deltaX < 0 ? DIRECTION.LEFT : DIRECTION.RIGHT);

            final int maxIndex = mTextArray.length - 1;
            if (direction == DIRECTION.RIGHT && mRightCursorIndex == maxIndex) {
                return;
            }

            if (mRightCursorRect.right + deltaX > mRightBoundary) {
                deltaX = mRightBoundary - mRightCursorRect.right;
            }

            final int maxMarkIndex = mTextArray.length - 1;
            if (direction == DIRECTION.RIGHT && mRightCursorIndex == maxMarkIndex) {
                return;
            }

            if (mRightCursorRect.left + deltaX < mLeftCursorRect.right) {
                deltaX = mLeftCursorRect.right - mRightCursorRect.left;
            }

            if (deltaX == 0) {
                return;
            }

            final float moveX = deltaX / mPartLength;
            mRightCursorIndex += moveX;

            invalidate();
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            final int deltaX = mScroller.getCurrX();
            mRightCursorIndex = (float) deltaX / mPartLength;
            invalidate();
        }
        super.computeScroll();
    }

    private void triggerCallback(int location) {
        if (mListener == null) {
            return;
        }
        mListener.onRightCursorChanged(location, mTextArray[location].toString());

    }


    public void setListener(OnCursorChangeListener listener) {
        mListener = listener;

    }

    public interface OnCursorChangeListener {
        void onRightCursorChanged(int location, String textMark);
    }
}
