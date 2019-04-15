package com.kitzapp.telegram_stats.common;

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import com.kitzapp.telegram_stats.BuildConfig;

import java.util.Hashtable;

import static com.kitzapp.telegram_stats.common.AppConts.DELAY_COLOR_ANIM;
import static com.kitzapp.telegram_stats.common.AppConts.DELAY_ELEMENTS_ANIM;

/**
 * Created by Ivan Kuzmin on 2019-03-22.
 * Copyright © 2019 Example. All rights reserved.
 */

public class AndroidUtilites {
    private static final Hashtable<String, Typeface> typefaceCache = new Hashtable<>();

    public static ValueAnimator getArgbAnimator(int fromColor, int toColor, ValueAnimator.AnimatorUpdateListener listener){
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
        colorAnimation.setDuration(DELAY_COLOR_ANIM);
        colorAnimation.addUpdateListener(listener);
        return colorAnimation;
    }

    public static ValueAnimator getIntAnimator(int fromValue, int toValue, ValueAnimator.AnimatorUpdateListener listener){
        ValueAnimator intAnimator = ValueAnimator.ofObject(new IntEvaluator(), fromValue, toValue);
        intAnimator.setDuration(DELAY_ELEMENTS_ANIM);
        intAnimator.addUpdateListener(listener);
        return intAnimator;
    }

    public static ValueAnimator getFloatAnimator(float fromValue, float toValue, ValueAnimator.AnimatorUpdateListener listener){
        ValueAnimator intAnimator = ValueAnimator.ofObject(new FloatEvaluator(), fromValue, toValue);
        intAnimator.setDuration(DELAY_ELEMENTS_ANIM);
        intAnimator.addUpdateListener(listener);
        return intAnimator;
    }

    public static Typeface getTypeface(Context context, String assetPath) {
        synchronized (typefaceCache) {
            if (!typefaceCache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(context.getAssets(), assetPath);
                    typefaceCache.put(assetPath, t);
                } catch (Exception e) {
                    if (BuildConfig.DEBUG) {
                        Log.e("Typeface error","Could not get typeface '" + assetPath + "' because " + e.getMessage());
                    }
                    return null;
                }
            }
            return typefaceCache.get(assetPath);
        }
    }

    public static void setDrawFilterATOP(Drawable drawable, int color) {
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    public static int convertDpToPx(Resources r, float dpValue) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue,
                r.getDisplayMetrics());
        return (int) px;
    }

    public static Paint getPaint(int color, int lineWidth) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(lineWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        return paint;
    }

    public static Paint getPaintFill(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        return paint;
    }

    public static boolean isRangeLineAvailable(float xStart, float yStart,
                                               float xEnd, float yEnd, float range) {
        boolean isAvailableForDraw;
        float sub = Math.abs(xEnd - xStart) + Math.abs(yEnd - yStart);
        if (sub >= range) {
            isAvailableForDraw = true;
        } else {
            isAvailableForDraw = false;
        }
        return isAvailableForDraw;
    }
}
