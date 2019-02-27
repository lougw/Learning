package com.lougw.learning.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.OverScroller;

public class ExtendHeadLayout extends FrameLayout {
    private View mExtendHead;
    private RecyclerView mRecyclerView;
    private boolean isExtendHead = true;
    private int mExtendHeadHeight = 0;
    private OverScroller mScroller;

    public ExtendHeadLayout(@NonNull Context context) {
        super(context);
        mScroller = new OverScroller(context);

    }

    public ExtendHeadLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScroller = new OverScroller(context);

    }

    public ExtendHeadLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new OverScroller(context);

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
boolean init=false;
    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        //滑动停止
                        if(!init){
                            init=true;
                            return;
                        }
                       // mRecyclerView.setPadding(0,0,0,0);
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        //正在滚动

                        break;

                }
        }

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
//            mExtendHead.setTranslationY(ScrollY);
//            scrollBy(0,UIUtils.dip2px(1));
            int top=dy>0?-10:10;
//            if(getScrollY()<UIUtils.dip2px(200)){
                scrollBy(0, top);
//            }


//            boolean hiddenLeft = dx > 0 && getScrollY() < mExtendHeadHeight && !ViewCompat.canScrollVertically(mRecyclerView, -1);
//
//            boolean showLeft = dx < 0 && !ViewCompat.canScrollVertically((mRecyclerView), -1);
//            boolean hiddenRight = dx < 0 && getScrollY() > mExtendHeadHeight && !ViewCompat.canScrollVertically(mRecyclerView, 1);
//            boolean showRight = dx > 0 && !ViewCompat.canScrollVertically(mRecyclerView, 1);
//            if (hiddenLeft || showLeft || hiddenRight || showRight) {
//                smoothScrollBy(0, dy);
//            }

        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) + UIUtils.dip2px(200), MeasureSpec.EXACTLY));

//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);   //获取宽的尺寸
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec); //获取高的尺寸
//        setMeasuredDimension(widthSize, heightSize+ UIUtils.dip2px(200));
//        LayoutParams params = (LayoutParams) mRecyclerView.getLayoutParams();
//        params.height=getMeasuredHeight()+ UIUtils.dip2px(200);
//        mRecyclerView.setLayoutParams(params);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mExtendHead != null && mRecyclerView != null) {
            mExtendHeadHeight = mExtendHead.getMeasuredHeight();
//            mRecyclerView.setPadding(0, mExtendHeadHeight, 0, 0);
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


    //调用此方法滚动到目标位置
    public void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }

    //调用此方法设置滚动的相对偏移
    public void smoothScrollBy(int dx, int dy) {

        //设置mScroller的滚动偏移量
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }

//    @Override
//    public void computeScroll() {
//
//        //先判断mScroller滚动是否完成
//        if (mScroller.computeScrollOffset()) {
//            //这里调用View的scrollTo()完成实际的滚动
//            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
//
//            //必须调用该方法，否则不一定能看到滚动效果
//            postInvalidate();
//        }
//        super.computeScroll();
//    }

}
