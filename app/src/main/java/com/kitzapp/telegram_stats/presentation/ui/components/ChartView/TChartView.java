package com.kitzapp.telegram_stats.presentation.ui.components.ChartView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.Application.AndroidApp;
import com.kitzapp.telegram_stats.Application.AppManagers.ObserverManager;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.domain.model.chart.Chart;
import com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl.CellChartTitle;
import com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl.CellChart;
import com.kitzapp.telegram_stats.presentation.ui.components.TViewObserver;

import java.util.Observable;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TChartView extends LinearLayout implements TViewObserver {
    private CellChartTitle _titleCell;
    private CellChart _fullChartCell;

    @NonNull
    private Chart chart;

    private int _oldBackColor;

    public TChartView(Context context) {
        super(context);
        this.init();
    }

    public TChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    public TChartView(Context context, @NonNull Chart chart) {
        super(context);
        this.chart = chart;
        this.init();
    }

    @Override
    public void init() {
        this.setOrientation(VERTICAL);
        _oldBackColor = this.getCurrentBackColor();

        this.setBackgroundColor(_oldBackColor);

        int RightLeftPadding = ThemeManager.CHART_CELL_RIGHTLEFT_MARGIN_PX;
        setPadding(RightLeftPadding, 0, RightLeftPadding, 0);

        this.initChildViews();
    }

    private void initChildViews() {
        this.removeAllViews();
        _titleCell = new CellChartTitle(getContext());
        this.addView(_titleCell);

        _fullChartCell = new CellChart(getContext(), chart);
        this.addView(_fullChartCell);
    }

    @Override
    public void addObserver() {
        AndroidApp.observerManager.addObserver(this);
    }

    @Override
    public void deleteObserver() {
        AndroidApp.observerManager.deleteObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if ((int) arg == ObserverManager.KEY_OBSERVER_THEME_UPDATED) {
            int newBackColor = getCurrentBackColor();

            if (_oldBackColor != newBackColor) {
                // BACKGROUND CHANGE COLOR
                ValueAnimator backRGBAnim = AndroidUtilites.getArgbAnimator(
                        _oldBackColor,
                        newBackColor,
                        animation -> setBackgroundColor((int) animation.getAnimatedValue()));
                backRGBAnim.start();
                _oldBackColor = newBackColor;
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.addObserver();

        LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        layoutParams.bottomMargin = ThemeManager.CHART_CELL_BOTTOM_MARGIN_PX;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.deleteObserver();
    }

    private int getCurrentBackColor() {
        return ThemeManager.getColor(ThemeManager.key_cellBackColor);
    }
}
