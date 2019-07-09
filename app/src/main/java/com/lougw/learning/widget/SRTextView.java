package com.lougw.learning.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SRTextView extends View {

    private final Paint mTextPaint;
    private TextPaint textPaint;

    private String directors = "导演：金哲勇 李雅弢";
    private String actors = "主演：陈赫 王子文 唐晓天 孔宋今 黄一琳 谭泉 黄天元 龙斌 郭京飞 凤小岳 张子贤 陈芋米";
    private String desc = "简介：郝运开了一家私人动物诊所，却因为一场猫咪配种事故，意外发现这个世界上有转化者存在。动物管理局带走郝运，但用各种方法都无法洗掉他的记忆，只好迫使他加入动物管理局，成为探员。从此，郝运的生活发生了天翻地覆的变化，原来，他的身边到处都是转化者。孤独、愤怒、嫉妒、固执、缺心眼……一桩桩动物案件，让郝运意识到，转化者世界也不外乎柴米油盐、悲欢离合，精于人情世故的郝运和战斗力超强的吴爱爱组成搭档，从水火不容变成干柴烈火，就在火苗一触即发之际，一桩牵扯郝运身世的转化者大案浮出水面……";
    private String history = "播放至 22集 00:01";

    public SRTextView(Context context) {
        this(context, null);

    }

    public SRTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SRTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(0xff1e1f21);
        mTextPaint.setAntiAlias(true);
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(directors, 0, 28 / 2 + 20, mTextPaint);
        canvas.drawText(TextUtils.ellipsize(actors, new TextPaint(mTextPaint), getMeasuredWidth(), TextUtils.TruncateAt.END).toString(), 0, 28 + 40 + 28 / 2, mTextPaint);
        canvas.translate(0, 28 + 40 + 28 / 2 + 40);
        StaticLayout staticLayout = getStaticLayout(desc);
        //StaticLayout staticLayout = new StaticLayout(desc, textPaint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        staticLayout.draw(canvas);

    }


    private StaticLayout getStaticLayout(String desc) {
        StaticLayout sl = null;
        Class clazz = null;
        try {
            clazz = Class.forName("android.text.StaticLayout");
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        Constructor<?> cons[] = null;
        cons = clazz.getConstructors(); //第一步，取得全部构造方法并赋给数组
        Constructor con = null;
        StaticLayout tmp = null;
        try {
            //**13个参数 ，注意int.class 不是interge.class**
            con = clazz.getConstructor(CharSequence.class, int.class, int.class, TextPaint.class, int.class,
                    Layout.Alignment.class, TextDirectionHeuristic.class, float.class, float.class, boolean.class,
                    TextUtils.TruncateAt.class, int.class, int.class);
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        }

        try {
            tmp = (StaticLayout) con.newInstance(desc, 0, desc.length(), textPaint, getMeasuredWidth(), Layout.Alignment.ALIGN_NORMAL,

                    TextDirectionHeuristics.FIRSTSTRONG_LTR, 1.0f, 0.0f, true, TextUtils.TruncateAt.MIDDLE, 0, 1);
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        }
        sl = tmp;

        return sl;
    }


    /**
     *
     *
     *  public StaticLayout(CharSequence source, TextPaint paint,
     *                         int width,
     *                         Alignment align, float spacingmult, float spacingadd,
     *                         boolean includepad) {
     *
     *
     */


/**
 *
 *
 *     public StaticLayout(CharSequence source, int bufstart, int bufend,
 *                         TextPaint paint, int outerwidth,
 *                         Alignment align, TextDirectionHeuristic textDir,
 *                         float spacingmult, float spacingadd,
 *                         boolean includepad,
 *                         TextUtils.TruncateAt ellipsize, int ellipsizedWidth, int maxLines) {
 *
 *
 */

}
