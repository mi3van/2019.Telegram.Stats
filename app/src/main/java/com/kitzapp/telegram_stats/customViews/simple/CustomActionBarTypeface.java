package com.kitzapp.telegram_stats.customViews.simple;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

/**
 * Created by Ivan Kuzmin on 06.04.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class CustomActionBarTypeface extends TypefaceSpan {

    private final Typeface _newType;

    public CustomActionBarTypeface(Typeface type) {
        super("");
        _newType = type;
    }

    @Override
    public void updateDrawState(TextPaint paint) {
        applyCustomTypeFace(paint, _newType);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeFace(paint, _newType);
    }

    private static void applyCustomTypeFace(Paint paint, Typeface tf) {
        paint.setTypeface(tf);
    }
}
