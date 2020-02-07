package com.lougw.learning.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import com.lougw.learning.R;

public class StickyLayout extends LinearLayout implements NestedScrollingParent {
    private static final String TAG = "StickyLayout";

    private static final int MAX_WIDTH = 400;
    private static final int ANIMATION_DURATION = 400;
    private RecyclerView mRecyclerView;
    private boolean isRunAnim;
    private int mDrag = 2;

    public StickyLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public StickyLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public StickyLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LayoutParams params = (LayoutParams) mRecyclerView.getLayoutParams();
        params.leftMargin = MAX_WIDTH;
        params.rightMargin = MAX_WIDTH;
//        scrollBy(MAX_WIDTH, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LayoutParams params = (LayoutParams) mRecyclerView.getLayoutParams();
        params.width = getMeasuredWidth()+MAX_WIDTH*2;

    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if (target instanceof RecyclerView && !isRunAnim) {
            return true;
        }
        return false;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {

    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        // 如果在自定义ViewGroup之上还有父View交给我来处理
        getParent().requestDisallowInterceptTouchEvent(true);
        // dx>0 往左滑动 dx<0往右滑动
        boolean hiddenLeft = dx > 0 && getScrollX() < MAX_WIDTH && !ViewCompat.canScrollHorizontally(target, -1);
        boolean showLeft = dx < 0 && !ViewCompat.canScrollHorizontally(target, -1);
        boolean hiddenRight = dx < 0 && getScrollX() > MAX_WIDTH && !ViewCompat.canScrollHorizontally(target, 1);
        boolean showRight = dx > 0 && !ViewCompat.canScrollHorizontally(target, 1);
        if (hiddenLeft || showLeft || hiddenRight || showRight) {
            scrollBy(dx / mDrag, 0);
            consumed[0] = dx;
        }
        // 限制错位问题
        if (dx > 0 && getScrollX() > MAX_WIDTH && !ViewCompat.canScrollHorizontally(target, -1)) {
            scrollTo(MAX_WIDTH, 0);
        }
        if (dx < 0 && getScrollX() < MAX_WIDTH && !ViewCompat.canScrollHorizontally(target, 1)) {
            scrollTo(MAX_WIDTH, 0);
        }
    }


    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        // 当RecyclerView在界面之内交给它自己惯性滑动
        if (getScrollX() == MAX_WIDTH) {
            return false;
        }
        return true;
    }

    @Override
    public int getNestedScrollAxes() {
        return SCROLL_AXIS_NONE;
    }

    /**
     * 限制滑动 移动x轴不能超出最大范围
     */
    @Override
    public void scrollTo(int x, int y) {
        if (x < 0) {
            x = 0;
        } else if (x > MAX_WIDTH * 2) {
            x = MAX_WIDTH * 2;
        }
        super.scrollTo(x, y);
    }

    @Override
    public void onStopNestedScroll(View target) {
        setStickyAnimation();
    }

    /**
     * 回弹动画
     */
    protected void setStickyAnimation() {
        ValueAnimator stickyAnimation = ValueAnimator.ofFloat(0, 1);
        stickyAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animFraction = ((float) animation.getAnimatedValue());
                scrollBy((int) ((MAX_WIDTH - getScrollX()) * animFraction), 0);
                if (animFraction == 1) {
                    isRunAnim = false;
                }

            }
        });
        stickyAnimation.setDuration(ANIMATION_DURATION);
        isRunAnim = true;
        stickyAnimation.start();

    }


}
