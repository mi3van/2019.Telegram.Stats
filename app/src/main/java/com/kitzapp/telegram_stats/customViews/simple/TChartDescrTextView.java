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

public class TChartDescrTextView extends TTextView {

    public TChartDescrTextView(Context context) {
        super(context);
    }

    public TChartDescrTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TChartDescrTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init() {
        super.init();

        int titleColor = getCurrentColor();

        setTextColor(titleColor);
        setTextSizeDP(ThemeManager.TEXT_SMALL_SIZE_DP);
        setSingleLine(true);
        setEllipsize(TextUtils.TruncateAt.END);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        setLayoutParams(layoutParams);
    }

    private int getCurrentColor() {
        return ThemeManager.getColor(ThemeManager.key_grayTextColor);
    }

}
