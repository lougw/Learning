package com.lougw.learning.widget.tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;


import com.lougw.learning.R;
import com.lougw.learning.utils.FMConst;

import java.util.logging.Logger;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * 微调控件:
 * 自定义一个 scrollY属性 没有使用View本身的scrollY.
 * see https://blog.csdn.net/junzia/article/details/50979382
 */

public class WheelView extends View {

    private static final int LONG_PRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
    private static final String TAG = WheelView.class.getSimpleName();
    private static final int showSize = 5;             //展示Item的个数

    //是否为全屏(宽度的) UI2Fragment为true, UIFragment为false
    private boolean isFullScreen = true;
    private int scrollY = 0;

    private boolean isCircle = false;     //是否为环形/循环展示
    private int mTouchSlop;
    private boolean clickAction = false;

    private int cacheNowItem = -1;        //预设当前item的位置，负数表示不设定

    private int centerIndex = -1;         //当前item位置

    private int width;                  //view的宽度
    private int height;                 //view的高度
    private int itemHeight = 105;       //item的高度
    private int centerX;                //item的X位置.(中点)

    private int realHeight;             //内容的实际高度
    private int minScrollY;             //最小滚动高度
    private int maxScrollY;             //最大滚动高度

    //    private final ArrayList<Integer> data;   //数据集合
    private int mLow = FMConst.LOW_RADIO, mHigh = FMConst.HIGH_RADIO; // 最低,最高的频率范围, 不需要存 list.
    private SelectHzChanged selectHzChangedListener;
    private final Paint paint;
    private final Paint bgPaint;

    private int lastY;
    private int hideDelaySecond = 5;
    private final OverScroller mScroller;

    public boolean needMeasure = true;
    private SoundPool wheelSound;
    private int soundId;
    private boolean isSoundLoadComplete = false;
    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new OverScroller(getContext());
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStrokeWidth(5f);
        Typeface tf = context.getResources().getFont(R.font.barlow_condensed_regular);
        paint.setTypeface(tf);
        bgPaint = new Paint();
        bgPaint.setStrokeWidth(4f);
        bgPaint.setColor(isFullScreen ? 0x22FFFFFF : 0xff4d6382);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setPadding(0, 30, 0, 30);//MHMI-2235
    }

    private void setPaintTextParam(int deltaToCenter) {
        int minSize = 40, maxSize = 130;
        int minColor = 0x00, maxColor = 0xff;
        float tmp = 1 - (deltaToCenter * 2f / height);
        int size = (int) (minSize + (maxSize - minSize) * tmp);
        int color = (int) (minColor + (maxColor - minColor) * tmp);
        if (deltaToCenter > (itemHeight * 2.5)) {
            color = 0;
        }
        paint.setTextSize(dip2px(getContext(), size));
        paint.setColor(Color.argb(color, 0xff, 0xff, 0xff));
    }

    private int dip2px(Context c, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.getResources().getDisplayMetrics());
    }

    /**
     * 设置区间.单位 khz, 如 [87500, 108000]
     */
    public void setRange(int low, int high) {
        mLow = low + 100;
        mHigh = high - 100;
        needMeasure = true;
    }

    private int rangeSize() {
        return (mHigh - mLow) / 100 + 1;
    }

    private void measureData() {
        if (needMeasure) {
            width = getWidth();
            centerX = width / 2;
            height = getHeight();
            itemHeight = (height - getPaddingTop() - getPaddingBottom()) / showSize;
            realHeight = rangeSize() * itemHeight;
            minScrollY = -(rangeSize() - 1 - (showSize - 1) / 2) * itemHeight;
            maxScrollY = (showSize - 1) / 2 * itemHeight;
            needMeasure = false;
            Log.d(TAG, " width : " + width + ", height:" + height + "; itemHeight:" + itemHeight + ", realHeight:"
                    + realHeight + ", scrollY range : (" + minScrollY + ", " + maxScrollY + ")");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (rangeSize() <= 0) {
            return;
        }
        measureData();
//        drawProcess(canvas);
        //如果设置了当前选中
        if (cacheNowItem >= 0) {
            scrollY = -(cacheNowItem - (showSize - 1) / 2) * itemHeight;
            cacheNowItem = -1;
        }
        if (itemHeight == 0) {
            return;
        }//MHMI-1048. monkey test.
        drawLine(canvas);
        int startItemPos = -scrollY / itemHeight;      //绘制的数据的起始位置
        updateCenterIndex(scrollY, itemHeight);
        float baseLine;
        for (int i = startItemPos, j = 0; j < showSize + 1; j++, i++) {
            float topY = j * itemHeight + scrollY % itemHeight + getPaddingTop();
            setPaintTextParam((int) Math.abs(height * 0.4 - topY));
            baseLine = getBaseLine(paint, topY, itemHeight);
            if (i >= 0 && i < rangeSize()) {
                canvas.drawText(showString(i), centerX, baseLine, paint);
            } else {
                if (isCircle) {
                    int pos = i % rangeSize();
                    canvas.drawText(showString(pos < 0 ? pos + rangeSize() : pos), centerX,
                            baseLine, paint);
                }
            }
        }
    }

    private void drawLine(Canvas canvas) {
        if (isFullScreen) {
            int width = getWidth();
            if (width <= 0) {
                width = 1920;
            }
            int lineLength = 300, space = 220;
            canvas.drawLine(width / 2 - lineLength - space, height / 2,
                    width / 2 - space, height / 2, bgPaint);
            canvas.drawLine(width / 2 + space, height / 2,
                    width / 2 + space + lineLength, height / 2, bgPaint);
        } else {
            canvas.drawLine(80, height / 2, 150, height / 2, bgPaint);
            canvas.drawLine(510, height / 2, 580, height / 2, bgPaint);
        }
    }

    public void show(boolean show) {
        if (show) {
            if (mLow == 0 || rangeSize() < 2) {
                Log.e(TAG, "can not show. low : " + mLow + ", high : " + mHigh);
                setVisibility(GONE);
            } else {
                setVisibility(VISIBLE);
                postToHide();
            }
        } else {
            setVisibility(GONE);
        }
    }

    private final Runnable hideRunnable = () -> show(false);

    private void postToHide() {
        removeCallbacks(hideRunnable);
        postDelayed(hideRunnable, hideDelaySecond * 1000);
    }

    /**
     * 设置选中
     */
    public void setCenterItem(int targetKhz) {
        cacheNowItem = targetKhz == 0 ? 0 : (targetKhz - mLow) / 100;
        Log.d(TAG, "set cache center item/index : " + cacheNowItem);
        invalidate();
    }

    @Override
    public void computeScroll() {
        //scroller的滚动是否完成
        if (mScroller.computeScrollOffset()) {
            scrollY = mScroller.getCurrY();
            checkScrollY();
            invalidate();
        }
        super.computeScroll();
    }

    private final Runnable notify = this::notifyIfSelectChange;

    private void notifyIfSelectChange() {
        if (isAttachedToWindow()) { //MHMI-1424
            if (centerIndex > -1 && centerIndex < rangeSize()) {
                int khz = mLow + 100 * centerIndex;
                selectHzChangedListener.onSelectHzChanged(khz);
            }
        }
    }

    public void setFullScreen(boolean full) {
        isFullScreen = full;
        bgPaint.setColor(full ? 0x22FFFFFF : 0xff4d6382);
    }

    public void setSelectChangeListener(SelectHzChanged lis) {
        selectHzChangedListener = lis;
    }

    // startPos [-2, max], dy= [-itemHeight+1, itemHeight-1]. target: [0, size-1]
    private void updateCenterIndex(int scrollY, int itemHeight) {
        int startPos = Math.round(-scrollY * 1f / itemHeight);
        int targetIndex = startPos + showSize / 2;
        if (targetIndex != centerIndex) {
//            Logger.vF("scrollY=%d, itemHeight=%d,start Pos = %d ,current=%d, target=%d",
//                    scrollY, itemHeight, startPos, centerIndex, targetIndex);
            if (centerIndex != -1) {    //首次不notify,因为首次onDraw()必定没有khz变化.
                playSound();
                removeCallbacks(notify);
                postDelayed(notify, 500);
            }
            centerIndex = targetIndex;
        }
    }

    private void playSound() {
//        if (wheelSound == null) {
//            wheelSound = new SoundPool.Builder()
//                    .setMaxStreams(1)   //MHMI-11135 避免声音叠加
//                    .setAudioAttributes(new AudioAttributes.Builder()
//                            .setLegacyStreamType(11)//AudioSystem.STREAM_NAVI 使用地图导航音流.
//                            .build())
//                    .build();
//            wheelSound.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
//                if (status == 0 && wheelSound != null) {///MHMI-4429. load complete ,release.
//                    isSoundLoadComplete = true;
//                    playSound(sampleId);
//                } else {    // load failed OR release already!
//                    Log.d(TAG,"load Sound result: " + status);
//                }
//            });
//            soundId = wheelSound.load(getContext(), R.raw.wheel_sound, 1);
//        } else {
//            if (isSoundLoadComplete) {
//                playSound(soundId);
//            }
//        }
    }

    private void playSound(int _soundId) {
//        int streamId =
//        Single.fromCallable(() -> {
//            if (wheelSound != null) {
//                wheelSound.play(_soundId, 1f, 1f, 1, 0, 1f);
//            }
//            return 1;
//        }).subscribeOn(Schedulers.single()).subscribe();
//        Logger.vF("play sound ! soundId= " + _soundId + ", streamId= " + streamId);
    }

    private void releaseSoundPool() {
        if (wheelSound != null) {
//            Logger.vF("release sound pool!");
            wheelSound.release();
            wheelSound = null;
        }
        isSoundLoadComplete = false;
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility != View.VISIBLE) {
            releaseSoundPool();
        } else {
//            Runnable checkDelay = () -> {
////                ConfigRepo cr = Injection.provideConfigRepo();
////                int delay = cr.getWheelViewHideDelay();
////                if (delay != hideDelaySecond) {
////                    hideDelaySecond = delay;
////                    postToHide();
////                }
//                Log.d(TAG,"visible check hide delay : " + hideDelaySecond);
//            };
//            post(checkDelay);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releaseSoundPool();
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Logger.d(TAG, "onTouchEvent. " + event);
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        boolean addMovement = true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                }
                lastY = y;
                clickAction = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(y - lastY) < 1) {
                    break;
                }
                clickAction = false;
                if (!isInCenterArea(x)) {
                    addMovement = false;
                    break;
                }
                scrollY += y - lastY;
                checkScrollY();
                invalidate();
                lastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                checkStateAndPosition();
                invalidate();
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                if (clickAction && pointInView(x, y, mTouchSlop)
                        && (event.getEventTime() - event.getDownTime() < LONG_PRESS_TIMEOUT)) {
                    doClick(x, y);
                }
                break;
        }
        postToHide();
        if (mVelocityTracker != null && addMovement) {
            mVelocityTracker.addMovement(event);
        }
        return true;
    }

    private void doClick(int x, int y) { //660宽度, 151-510;;宽度视UI2,UI1不同.
        mScroller.forceFinished(true);
        if (isInCenterArea(x)) {
            int yIndex = y / itemHeight; // yIndex 取值范围 [0, 5]
            if (yIndex == showSize) {
                yIndex -= 1;
            }
            int clickedIndex = centerIndex + (yIndex - showSize / 2);
            Log.d(TAG, "clicked center=" + centerIndex + ", yIndex=" + yIndex + ", clickIndex=" + clickedIndex);
            if (clickedIndex >= 0 && clickedIndex < rangeSize() && clickedIndex != centerIndex) {
//                setCenterItem(mLow + 100 * clickedIndex);//这个缺少平滑滑动.
                mScroller.startScroll(0, scrollY, 0,
                        (showSize / 2 - yIndex) * itemHeight, 200);
            }
        } else {
            show(false);//hide self.
        }
    }

    public boolean pointInView(float localX, float localY, float slop) {
        return localX >= -slop && localY >= -slop && localX < ((getRight() - getLeft()) + slop) &&
                localY < ((getBottom() - getTop()) + slop);
    }

    //是否再中间的可滑动区域. false表示再两边的点击隐藏区域.true表示滑动区域,点击调频区域.
    private boolean isInCenterArea(int x) {
        return x > (0.23 * width) && x < (0.77 * width);    //常量值跟 drawLine()相关.
    }

    private int getRealHeight() {
        if (realHeight == 0) {
            realHeight = rangeSize() * itemHeight;
        }
        return realHeight;
    }

    @Nullable
    private VelocityTracker mVelocityTracker;

    private void checkStateAndPosition() {
        if (rangeSize() == 0) {
            return;
        }
//        mScroller.fling(0,(int)scrollY,0,vy,0,0,Integer.MIN_VALUE,Integer.MAX_VALUE); // fling 无法定位stop时的坐标
        if (!isCircle && scrollY < minScrollY) {            //上拉超出
            mScroller.startScroll(0, scrollY, 0, (showSize + 1) / 2 * itemHeight - getRealHeight() - scrollY, 400);
        } else if (!isCircle && scrollY > maxScrollY) {     //下拉超出
            mScroller.startScroll(0, scrollY, 0, (showSize - 1) / 2 * itemHeight - scrollY, 400);
        } else {
            //滑动距离，和手指滑动距离成正比，和滑动时间成反比
//                int finalY = (int) ((scrollY + rate * (lastY - downY) / (endTime - downTime))) / itemHeight * itemHeight;
            //使用速度计算滑动距离. 并确保是 item height 的整数倍.
            if (mVelocityTracker == null) {
                return;
            }
            mVelocityTracker.computeCurrentVelocity(200);
            int vy = (int) mVelocityTracker.getYVelocity();
            if (vy > -100 && vy < 100) {
                vy = 0;
            } //忽略低速时的速度.
            int finalY = (scrollY - 10 * itemHeight + vy - itemHeight / 2) / itemHeight * itemHeight + 10 * itemHeight;
            // 这里先 减去10倍height,再加10倍height,是因为scrollY取值范围是 [-size*height, 2*height]
            // 而在0附近取四舍五入的值有差异, 这里先减再加,是为了避免正数,只考虑负数.  MHMI-2274
            if (!isCircle) {
                if (finalY < minScrollY) {
                    finalY = minScrollY;
                } else if (finalY > maxScrollY) {
                    finalY = maxScrollY;
                }
            }
//            Logger.iF("scrollY: %d,vy:%d, finalY: %d, center:%d", scrollY, vy, finalY, (2 - finalY / itemHeight));
            mScroller.startScroll(0, scrollY, 0, finalY - scrollY, 400);
        }
    }

    private float getBaseLine(Paint paint, float top, float height) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        return (2 * top + height - fontMetrics.bottom - fontMetrics.top) / 2;
    }

    private void checkScrollY() {
        if (isCircle) { //循环模式不需要检查scrollY
            return;
        }
        if (scrollY > maxScrollY) {
            scrollY = maxScrollY;
        } else if (scrollY < minScrollY) {
            scrollY = minScrollY;
        }
    }

    private String showString(int index) {
        int khz = mLow + index * 100;
        return FMConst.kHz2mHz(khz);
    }
}
