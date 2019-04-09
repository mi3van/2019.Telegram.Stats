package com.kitzapp.telegram_stats.customViews.ChartView.impl;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
import com.kitzapp.telegram_stats.customViews.simple.TColorfulChartCircle;

/**
 * Created by Ivan Kuzmin on 29.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class CellContainerForCircleViews extends FrameLayout {
    public CellContainerForCircleViews(Context context) {
        super(context);
        this.init();
    }

    public CellContainerForCircleViews(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public CellContainerForCircleViews(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        setWillNotDraw(false);
    }

    void addCircle(float x, float y, int color) {
        TColorfulChartCircle tChartCircle = new TColorfulChartCircle(getContext(), color);
        this.addView(tChartCircle);
        float x1 = x - (ThemeManager.CHART_CIRCLE_SIZE_PX >> 1);
        tChartCircle.setX(x1);
        float y1 = y - (ThemeManager.CHART_CIRCLE_SIZE_PX >> 1);
        tChartCircle.setY(y1);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
        layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
    }
}
