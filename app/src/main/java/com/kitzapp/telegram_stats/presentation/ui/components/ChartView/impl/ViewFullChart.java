package com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.Application.AppManagers.ObserverManager;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.domain.model.chart.Chart;

import java.util.Observable;

;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class ViewFullChart extends ViewBaseChart {
    private static final float APPROX_RANGE = 0f;

    private int _oldFullChartBackColor;

    public ViewFullChart(Context context) {
        super(context);
    }

    public ViewFullChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewFullChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewFullChart(Context context, @NonNull Chart chart) {
        super(context, chart);
    }

    @Override
    public void init() {
        super.init();

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
        layoutParams.topMargin = ThemeManager.CHART_FULL_TOP_BOTTOM_MARGIN_PX;
        layoutParams.bottomMargin = ThemeManager.CHART_FULL_TOP_BOTTOM_MARGIN_PX;

        _oldFullChartBackColor = this.getFullChartBackColor();
        this.setBackgroundColor(_oldFullChartBackColor);
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
    float getApproxRange() {
        return APPROX_RANGE;
    }

    @Override
    public void update(Observable o, Object arg) {
        if ((int) arg == ObserverManager.KEY_OBSERVER_THEME_UPDATED) {
            int newFullChartBackColor = getFullChartBackColor();

            // FULL CHART CHANGE BACK COLOR
            ValueAnimator fullChartRGBAnim = AndroidUtilites.getArgbAnimator(
                    _oldFullChartBackColor,
                    newFullChartBackColor,
                    animation -> this.setBackgroundColor((int) animation.getAnimatedValue()));
            fullChartRGBAnim.start();
            _oldFullChartBackColor = newFullChartBackColor;
        }
    }

    private int getFullChartBackColor() {
        return ThemeManager.getColor(ThemeManager.key_cellSubBackColor);
    }
}
