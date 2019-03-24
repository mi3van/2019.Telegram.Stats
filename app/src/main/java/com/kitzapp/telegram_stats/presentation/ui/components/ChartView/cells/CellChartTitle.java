package com.kitzapp.telegram_stats.presentation.ui.components.ChartView.cells;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;
import com.kitzapp.telegram_stats.R;
import com.kitzapp.telegram_stats.presentation.ui.components.impl.TTextPaint;
import com.kitzapp.telegram_stats.presentation.ui.components.impl.TTextView;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class CellChartTitle extends CellChartBaseSimple {

    private TTextView tTextView;

    public CellChartTitle(Context context) {
        super(context);
    }

    public CellChartTitle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CellChartTitle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setText(String text) {
        tTextView.setText(text);
    }

    public void setTextColor(int color) {
        tTextView.setTextColor(color);
    }

    @Override
    protected void init() {
        tTextView = new TTextView(getContext());
        TTextPaint chartTitleTextPaint = ThemeManager.chartTitleTextPaint;
        tTextView.setTypeface(chartTitleTextPaint.getTypeface());
        tTextView.setTextColor(chartTitleTextPaint.getColor());
        tTextView.setTextSizeDP(chartTitleTextPaint.getTextSize());
        tTextView.setText(getResources().getString(R.string.followers_title));

        setGravity(Gravity.CENTER_VERTICAL);
        addView(tTextView);
    }
}
