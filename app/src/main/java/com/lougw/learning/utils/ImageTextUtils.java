package com.lougw.learning.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;

import com.lougw.learning.R;


public class ImageTextUtils {

    public static Bitmap drawTextToImage(Context context, @DrawableRes int resId, String text) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId)
                .copy(Bitmap.Config.ARGB_8888, true);
        if (TextUtils.isEmpty(text)) {
            return bitmap;
        }
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Paint paint = new Paint();
        paint.setColor(context.getColor(R.color.transparent_per_80_white));  //设置画笔颜色
        paint.setStrokeWidth(5);//设置画笔宽度
        paint.setAntiAlias(true); //指定是否使用抗锯齿功能
        paint.setStyle(Paint.Style.FILL);//绘图样式，对于设文字和几何图形都有效
        paint.setTextAlign(Paint.Align.CENTER);//设置文字对齐方式，取值：align.CENTER、align.LEFT或align.RIGHT
        paint.setTextSize(21);//设置文字大小
        float baseLineY = Math.abs(paint.ascent() + paint.descent()) / 2;//文字baseline在y轴方向的位置
        canvas.drawText(text, 0, baseLineY, paint);
        return bitmap;
    }
}
