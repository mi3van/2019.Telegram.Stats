package com.kitzapp.telegram_stats.customViews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.kitzapp.telegram_stats.AndroidApp;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.core.appManagers.ObserverManager;
import com.kitzapp.telegram_stats.core.appManagers.TViewObserver;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
import com.kitzapp.telegram_stats.customViews.ChartView.TViewChartGpraphs;
import com.kitzapp.telegram_stats.customViews.ChartView.TViewChartTitle;
import com.kitzapp.telegram_stats.pojo.chart.Chart;

import java.util.Observable;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TFullCellView extends LinearLayout implements TViewObserver {
    private TViewChartTitle _titleCell;
    private TViewChartGpraphs _fullChartCell;

    private Chart chart;

    private int _oldBackColor;

    public TFullCellView(Context context) {
        super(context);
        this.init();
    }

    public TFullCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TFullCellView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    public void loadData( Chart chart) {
        if (_fullChartCell != null) {
            _fullChartCell.loadData(chart);
        } else {
            this.initChildViews();
        }
    }

    @Override
    public void init() {
        this.setOrientation(VERTICAL);

        _oldBackColor = this.getCurrentBackColor();
        this.setBackgroundColor(_oldBackColor);

        this.initChildViews();
    }

    private void initChildViews() {
        this.removeAllViews();
        _titleCell = new TViewChartTitle(getContext());
        this.addView(_titleCell);

        _fullChartCell = new TViewChartGpraphs(getContext());
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
        if ((byte) arg == ObserverManager.KEY_OBSERVER_THEME_UPDATED) {
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
