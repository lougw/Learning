package com.lougw.learning.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lougw.learning.R;

public class RefreshLayout extends ViewGroup implements NestedScrollingParent2 {
    private static final String TAG = "RefreshLayout";

    ImageView imageView;
    RecyclerView recyclerView;
    int headerHeight = 0;
    LinearLayoutManager linearLayoutManager;

    public RefreshLayout(Context context) {
        super(context);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View view, @NonNull View view1, int i, int i1) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View view, @NonNull View view1, int i, int i1) {

    }

    @Override
    public void onStopNestedScroll(@NonNull View view, int i) {

    }

    @Override
    public void onNestedScroll(@NonNull View view, int i, int i1, int i2, int i3, int i4) {

    }

    int[] position = new int[2];
    Rect viewRect = new Rect();

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        //if (type == ViewCompat.TYPE_TOUCH) {//手指触发的滑动
        // dy>0向下scroll dy<0向上scroll
        boolean hiddenTop = dy > 0 && getScrollY() < headerHeight && !target.canScrollVertically(-1);
        boolean showTop = dy < 0 && !target.canScrollVertically(-1);
        if (hiddenTop || showTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
        if (type == ViewCompat.TYPE_TOUCH) {
            imageView.getLocationInWindow(position);
            if (position[1] > 0) {
               // onOpHeader(false);
            }
            Log.d(TAG, "onMeasure: getScrollY() : " + getScrollY() + "  " + position[1] + " type : " + type + " " + viewRect + " " + showTop);

        }

        //}
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y <= 0) {
            y = 0;
        } else if (y > headerHeight) {
            y = headerHeight;
        }
        super.scrollTo(x, y);
    }

    public void onOpHeader(boolean show) {
        Log.d(TAG, "onOpHeader: "+getScrollY() );
        if (show && getScrollY() == 0) {
            return;
        }
        scrollTo(0, show ? -headerHeight : headerHeight);
        recyclerView.scrollBy(0, headerHeight);
        //recyclerView.scrollBy(0, -headerHeight);
//        if (linearLayoutManager.findFirstVisibleItemPosition() == 0) {
//            recyclerView.scrollToPosition(0);
//        }

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imageView = findViewById(R.id.header);
        recyclerView = findViewById(R.id.recycler_view);
//        recyclerView.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                if (getScrollY() == 0) {
////            imageView.getLocationInWindow(position);
//                    imageView.getGlobalVisibleRect(viewRect);
//                    if(viewRect.top>0){
//                        onOpHeader(false);
//                    }
//
//                }
//                return false;
//            }
//        });

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        headerHeight = imageView.getMeasuredHeight();
        linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int count = getChildCount();
        int height = 0;
        int width = 0;
        int lineWidth = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            width = Math.max(lineWidth, width);
            height += childHeight;
            lineWidth = childWidth;
            Log.d(TAG, "onMeasure: childHeight : " + childHeight);

        }

        Log.d(TAG, "onMeasure: height : " + height + " measureHeight : " + measureHeight);
        setMeasuredDimension((measureWidthMode == MeasureSpec.EXACTLY) ? measureWidth : width,
                (measureHeightMode == MeasureSpec.EXACTLY) ? measureHeight : height);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int top = 0;
        int left = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            int lc = left + lp.leftMargin;
            int tc = top + lp.topMargin;
            int rc = lc + child.getMeasuredWidth();
            int bc = tc + child.getMeasuredHeight();
            child.layout(lc, tc, rc, bc);
            top += childHeight;

        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }

    public static class MarginLayoutParams extends ViewGroup.MarginLayoutParams {

        public MarginLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public MarginLayoutParams(int width, int height) {
            super(width, height);
        }

        public MarginLayoutParams(LayoutParams source) {
            super(source);
        }
    }
}
