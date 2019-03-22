package com.kitzapp.telegram_stats.presentation.ui.components;

import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;

import com.kitzapp.telegram_stats.AndroidApplication;
import com.kitzapp.telegram_stats.BuildConfig;

import java.util.Hashtable;

/**
 * Created by Ivan Kuzmin on 2019-03-22.
 * Copyright © 2019 Example. All rights reserved.
 */

public class AndroidUtilites {

    private static final Hashtable<String, Typeface> typefaceCache = new Hashtable<>();
    public static float density = 1;

    public static Typeface getTypeface(String assetPath) {
        synchronized (typefaceCache) {
            if (!typefaceCache.containsKey(assetPath)) {
                try {
                    Typeface t;
                    if (Build.VERSION.SDK_INT >= 26) {
                        Typeface.Builder builder = new Typeface.Builder(AndroidApplication.applicationContext.getAssets(), assetPath);
                        if (assetPath.contains("medium")) {
                            builder.setWeight(700);
                        }
                        if (assetPath.contains("italic")) {
                            builder.setItalic(true);
                        }
                        t = builder.build();
                    } else {
                        t = Typeface.createFromAsset(AndroidApplication.applicationContext.getAssets(), assetPath);
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

    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(density * value);
    }
}
