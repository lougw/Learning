package com.lougw.learning.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.lougw.learning.R;


/**
 * <pre>
 *     author : lougw
 *     e-mail : gaoweilou@pptv.com
 *     time   : 2017/12/26
 *     desc   :
 * </pre>
 */

public class ShimmerView extends View {

    private static final int DEFAULT_DURATION_TIME = 800;
    private static final int DEFAULT_DELAY_TIME = 400;
    private static final int DEFAULT_DELAY_COLOR = 0x66FFFFFF;

    protected int mShimmerDuration = DEFAULT_DURATION_TIME;
    protected int mShimmerDelay = DEFAULT_DELAY_TIME;
    private int mShimmerColor = DEFAULT_DELAY_COLOR;
    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;
    private Paint mPaint;
    protected RectF mTempRectF = new RectF();
    protected RectF mFrameRectF = new RectF();
    protected RectF mPaddingOffsetRectF = new RectF();

    private ObjectAnimator mShimmerAnimator;
    private float mShimmerTranslate = 0;

    private boolean mShimmerAnimating = false;

    public ShimmerView(Context context) {
        super(context);
    }

    public ShimmerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public ShimmerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.ShimmerView);
            mShimmerDuration = type.getInt(R.styleable.ShimmerView_shimmer_duration, DEFAULT_DURATION_TIME);
            mShimmerDelay = type.getInt(R.styleable.ShimmerView_shimmer_delay, DEFAULT_DELAY_TIME);
            mShimmerColor = type.getColor(R.styleable.ShimmerView_shimmer_color, mShimmerColor);
            int paddingVertical = type.getDimensionPixelSize(R.styleable.ShimmerView_padding_vertical, 0);
            int paddingHorizontal = type.getDimensionPixelSize(R.styleable.ShimmerView_padding_horizontal, 0);
            mPaddingOffsetRectF.set(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
            type.recycle();
        }
        mPaint = new Paint();
        mGradientMatrix = new Matrix();
    }

    private void setGradient() {
        mTempRectF.set(mFrameRectF);
        mLinearGradient = new LinearGradient(
                0, 0, mTempRectF.width(), mTempRectF.height(),
                new int[]{0x00FFFFFF, 0x1AFFFFFF, mShimmerColor, 0x1AFFFFFF, 0x00FFFFFF},
                new float[]{0f, 0.2f, 0.5f, 0.8f, 1f}, Shader.TileMode.CLAMP);
        mPaint.setShader(mLinearGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        onDrawShimmer(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mFrameRectF.set(mPaddingOffsetRectF.left, mPaddingOffsetRectF.top, w - mPaddingOffsetRectF.right, h - mPaddingOffsetRectF.bottom);
        setGradient();
    }

    protected void onDrawShimmer(Canvas canvas) {
        if (mShimmerAnimating) {
            canvas.save();
            mTempRectF.set(mFrameRectF);
            float shimmerTranslateX = mTempRectF.width() * mShimmerTranslate;
            float shimmerTranslateY = mTempRectF.height() * mShimmerTranslate;
            mGradientMatrix.setTranslate(shimmerTranslateX, shimmerTranslateY);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            canvas.drawRoundRect(mTempRectF, 0, 0, mPaint);
            canvas.restore();
        }
    }

    protected void setShimmerTranslate(float shimmerTranslate) {
        if (mShimmerTranslate != shimmerTranslate) {
            mShimmerTranslate = shimmerTranslate;
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    protected float getShimmerTranslate() {
        return mShimmerTranslate;
    }

    public void startShimmerAnimation() {
        Animator animator = getShimmerAnimation();
        animator.start();
    }

    public void stopShimmerAnimation() {
        if (mShimmerAnimator != null) {
            mShimmerAnimator.end();
            mShimmerAnimator.removeAllUpdateListeners();
            mShimmerAnimator.cancel();
        }
        mShimmerAnimator = null;
    }

    private ObjectAnimator getShimmerAnimation() {
        if (null == mShimmerAnimator) {
            mShimmerAnimator = ObjectAnimator.ofFloat(this, "shimmerTranslate", -1f, 1f);
            mShimmerAnimator.setInterpolator(new LinearInterpolator());
            mShimmerAnimator.setDuration(mShimmerDuration);
            mShimmerAnimator.setStartDelay(mShimmerDelay);
            mShimmerAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    setShimmerAnimating(true);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    setShimmerAnimating(false);
                }
            });
        }
        return mShimmerAnimator;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopShimmerAnimation();
    }

    public void setShimmerAnimating(boolean shimmerAnimating) {
        this.mShimmerAnimating = shimmerAnimating;
    }
}
