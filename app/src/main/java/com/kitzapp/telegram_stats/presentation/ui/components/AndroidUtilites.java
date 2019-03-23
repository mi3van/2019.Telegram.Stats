package com.kitzapp.telegram_stats.presentation.ui.components;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;

import android.util.TypedValue;
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator;
import com.kitzapp.telegram_stats.Application.AndroidApp;
import com.kitzapp.telegram_stats.BuildConfig;

import java.util.Hashtable;

import static com.kitzapp.telegram_stats.common.AppConts.DELAY_COLOR_ANIM;

/**
 * Created by Ivan Kuzmin on 2019-03-22.
 * Copyright © 2019 Example. All rights reserved.
 */

public class AndroidUtilites {

    private static final Hashtable<String, Typeface> typefaceCache = new Hashtable<>();
    public static float density = 1;

    static ValueAnimator getArgbAnimator(int fromColor, int toColor, ValueAnimator.AnimatorUpdateListener listener){
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
        colorAnimation.setDuration(DELAY_COLOR_ANIM);
        colorAnimation.addUpdateListener(listener);
        return colorAnimation;
    }

    public static Typeface getTypeface(String assetPath) {
        synchronized (typefaceCache) {
            if (!typefaceCache.containsKey(assetPath)) {
                try {
                    Typeface t;
                    if (Build.VERSION.SDK_INT >= 26) {
                        Typeface.Builder builder = new Typeface.Builder(AndroidApp.applicationContext.getAssets(), assetPath);
                        if (assetPath.contains("medium")) {
                            builder.setWeight(700);
                        }
                        if (assetPath.contains("italic")) {
                            builder.setItalic(true);
                        }
                        t = builder.build();
                    } else {
                        t = Typeface.createFromAsset(AndroidApp.applicationContext.getAssets(), assetPath);
                    }
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

    public static int convertDpToPx(Resources r, float dpValue) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue,
                r.getDisplayMetrics());
        return (int) px;
    }
}
