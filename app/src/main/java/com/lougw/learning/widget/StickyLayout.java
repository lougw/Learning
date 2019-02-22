package com.lougw.learning.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.lougw.learning.R;

public class StickyLayout extends FrameLayout implements NestedScrollingParent {
    private static final int MAX_WIDTH = 200;
    private static final int ANIMATION_DURATION = 260;
    private int mDrag = 2;
    private View mChildView;
    private boolean isRunAnim;
    RecyclerView recyclerView;
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
//        setOrientation(LinearLayout.VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        recyclerView=findViewById(R.id.recycler_view);
//        mChildView = getChildAt(0);
//        LayoutParams params = (LayoutParams) mChildView.getLayoutParams();
//        params.leftMargin = MAX_WIDTH;
//        params.rightMargin = MAX_WIDTH;
//        scrollBy(MAX_WIDTH, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        LayoutParams params = (LayoutParams) mChildView.getLayoutParams();
//        params.width = getMeasuredWidth();
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() + getChildAt(0).getMeasuredHeight() + getChildAt(1).getMeasuredHeight()+500);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.d("LgwTag","onStartNestedScroll : nestedScrollAxes "+nestedScrollAxes);
        if (target instanceof android.support.v7.widget.RecyclerView) {
            return true;
        }
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        Log.d("LgwTag","onNestedScrollAccepted : axes "+axes);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.d("LgwTag","onNestedFling : consumed "+consumed);
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.d("LgwTag","onNestedPreFling : velocityY "+velocityY);
//        // 当RecyclerView在界面之内交给它自己惯性滑动
//        if (getScrollX() == MAX_WIDTH) {
//            return false;
//        }
        return false;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        Log.d("LgwTag","onNestedPreScroll : dy "+dy);
//        Log.d("LgwTag", "dx : " + dx);
        // 如果在自定义ViewGroup之上还有父View交给我来处理
        getParent().requestDisallowInterceptTouchEvent(true);
        boolean hiddenTop=dy>0 && getScrollY()<50;
//        LogUtil.i("scrolly="+getScrollY()+"=="+topHeight);
        boolean showTop=dy<0 && getScrollY()>=0 && !ViewCompat.canScrollVertically(target,-1);
        Log.d("LgwTag","hiddenTop : "+hiddenTop+" showTop : "+showTop);

        boolean showImg=showImg(dy);
        boolean  hideImg=hideImg(dy);
        Log.d("LgwTag","showImg : "+showImg+" hideImg : "+hideImg+" dy "+dy+" getScrollY() : "+getScrollY()+" recyclerView.getScrollY() : "+recyclerView.getScrollY());
        if ( showImg|| hideImg) {//如果需要显示或隐藏图片，即需要自己(parent)滚动
            scrollBy(0, dy);//滚动
            consumed[1] = 0;//告诉child我消费了多少
        }

    }

    //下拉的时候是否要向下滚动以显示图片
    public boolean showImg(int dy) {
        if (dy > 0) {
//            if (getScrollY() > 0 && recyclerView.getScrollY() == 0) {
                return true;
//            }
        }

        return false;
    }

    //上拉的时候，是否要向上滚动，隐藏图片
    public boolean hideImg(int dy) {
        if (dy < 0) {
//            if (getScrollY() < 50) {
                return true;
//            }
        }
        return false;
    }



    @Override
    public int getNestedScrollAxes() {
        return SCROLL_AXIS_NONE;
    }

    /**
//     * 限制滑动 移动x轴不能超出最大范围
//     */
//    @Override
//    public void scrollTo(int x, int y) {
//        if (x < 0) {
//            x = 0;
//        } else if (x > MAX_WIDTH * 2) {
//            x = MAX_WIDTH * 2;
//        }
//        super.scrollTo(x, y);
//    }

    @Override
    public void onStopNestedScroll(View target) {
        Log.d("LgwTag","onStopNestedScroll : ");
//        startAnimation(new ProgressAnimation());
    }
    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > 50) {
            y = 50;
        }
        Log.d("LgwTag","y : "+y);

        super.scrollTo(x, y);
    }
//---------------------
//    作者：三杯两盏
//    来源：CSDN
//    原文：https://blog.csdn.net/al4fun/article/details/53889075
//    版权声明：本文为博主原创文章，转载请附上博文链接！
    /**
     * 回弹动画
     */
    private class ProgressAnimation extends Animation {
        // 预留
        private float startProgress = 0;
        private float endProgress = 1;

        private ProgressAnimation() {
            isRunAnim = true;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float progress = ((endProgress - startProgress) * interpolatedTime) + startProgress;
            scrollBy((int) ((MAX_WIDTH - getScrollX()) * progress), 0);
            if (progress == 1) {
                isRunAnim = false;
            }
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            setDuration(ANIMATION_DURATION);
            setInterpolator(new AnticipateOvershootInterpolator());
        }
    }
}
