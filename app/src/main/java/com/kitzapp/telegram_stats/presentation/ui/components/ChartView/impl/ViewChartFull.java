package com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.Application.AppManagers.ObserverManager;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.domain.model.chart.Chart;

import java.util.Observable;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

class ViewChartFull extends ViewChartBase {
    private final int MAX_DOTS_FOR_APPROX_CHART_FULL = 256;

    private int _oldFullChartBackColor;
    private ViewRectSelect _viewRectSelect;
    private ViewRectSelect.RectListener _rectListener;

    public ViewChartFull(Context context) {
        super(context);
    }

    public ViewChartFull(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewChartFull(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    ViewChartFull(Context context, @NonNull Chart chart, ViewRectSelect.RectListener rectListener) {
        super(context, chart);
        _rectListener = rectListener;
        this.init();
    }

    @Override
    public void init() {
        if (_rectListener != null) {
            super.init();
            _viewRectSelect = new ViewRectSelect(getContext(), _rectListener);
            this.addView(_viewRectSelect);

            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
            layoutParams.height -= (ThemeManager.CHART_FULL_TOP_BOTTOM_MARGIN_PX << 1);
            layoutParams.topMargin = ThemeManager.CHART_FULL_TOP_BOTTOM_MARGIN_PX;
            layoutParams.bottomMargin = ThemeManager.CHART_FULL_TOP_BOTTOM_MARGIN_PX;

            _oldFullChartBackColor = this.getFullChartBackColor();
            this.setBackgroundColor(_oldFullChartBackColor);
        }
    }

    @Override
    int getViewHeightForLayout() {
        return ThemeManager.CHART_CELL_HEIGHT_PX;
    }

    @Override
    int getLinePaintWidth() {
        return ThemeManager.CHART_LINE_FULL_WIDTH_PX;
    }

    @Override
    int getMaxDotsForApproxChart() {
        return MAX_DOTS_FOR_APPROX_CHART_FULL;
    }

    @Override
    public void update(Observable o, Object arg) {
        if ((int) arg == ObserverManager.KEY_OBSERVER_THEME_UPDATED) {
            int newFullChartBackColor = getFullChartBackColor();

            // FULL CHART CHANGE BACK COLOR
            if (newFullChartBackColor != _oldFullChartBackColor) {
                ValueAnimator fullChartRGBAnim = AndroidUtilites.getArgbAnimator(
                        _oldFullChartBackColor,
                        newFullChartBackColor,
                        animation -> this.setBackgroundColor((int) animation.getAnimatedValue()));
                fullChartRGBAnim.start();
                _oldFullChartBackColor = newFullChartBackColor;
            }
        }
    }

    private int getFullChartBackColor() {
        return ThemeManager.getColor(ThemeManager.key_cellChartFullBackColor);
    }
}
