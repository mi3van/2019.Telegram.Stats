package com.kitzapp.telegram_stats.customViews.simple;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;

/**
 * Created by Ivan Kuzmin on 29.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TChartTextView extends TTextView {

    public TChartTextView(Context context) {
        super(context);
        this.init();
    }

    public TChartTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TChartTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    public void init() {
        int titleColor = getCurrentColor();

        TTextPaint chartDescrTextPaint = ThemeManager.chartDescrTextPaint;
        setTypeface(chartDescrTextPaint.getTypeface());
        setTextColor(titleColor);
        setTextSizeDP(chartDescrTextPaint.getTextSize());
        setSingleLine(true);
        setEllipsize(TextUtils.TruncateAt.END);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        setLayoutParams(layoutParams);
    }

    private int getCurrentColor() {
        return ThemeManager.chartDescrTextPaint.getColor();
    }

}
