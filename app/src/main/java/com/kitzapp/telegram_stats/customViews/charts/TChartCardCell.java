package com.kitzapp.telegram_stats.customViews.charts;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import com.kitzapp.telegram_stats.AndroidApp;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.core.appManagers.ObserverManager;
import com.kitzapp.telegram_stats.core.appManagers.TViewObserver;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
import com.kitzapp.telegram_stats.customViews.charts.base.*;
import com.kitzapp.telegram_stats.customViews.simple.TColorfulCheckBox;
import com.kitzapp.telegram_stats.customViews.simple.TViewChartDatesHoriz;
import com.kitzapp.telegram_stats.pojo.chart.Chart;

import java.util.Observable;

import static com.kitzapp.telegram_stats.common.AppConts.ELEVATION_CHART_VIEW;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TChartCardCell extends LinearLayout implements TViewObserver,
                                                            TColorfulCheckBox.Listener,
                                                            TChartBigInterface.Listener {
    private int _oldBackColor;

    private Chart _chart = null;
    private TViewChartTitle _titleCell;
    private TChartBigView _bigChart;
    private TChartMiniatureView _miniatureChart;
    private TViewChartDatesHoriz _chartDates;
    private TViewChartCheckBoxes _chBoxChartIsActive;

    public TChartCardCell(Context context) {
        super(context);
        this.init();
    }

    public TChartCardCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TChartCardCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    public void loadData( Chart chart) throws Exception {
        if (_chart == null || _chart.hashCode() != chart.hashCode()) {
            this._chart = chart;
            if (_titleCell == null) {
                this.createViews();
            }

            _bigChart.loadData(_chart);
            _miniatureChart.loadData(_chart);
            _chBoxChartIsActive.loadData(_chart);
        }
    }

    public void init() {
        setOrientation(VERTICAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setElevation(ELEVATION_CHART_VIEW);
        }

        setGravity(Gravity.CENTER_VERTICAL);
    }

    private void createViews() throws Exception {
        this.removeAllViews();

        byte type = _chart.getChartType();
        TChartsFabric chartsFabric = TChartsAbstractFabric.createChartFabric(type);

        _titleCell = new TViewChartTitle(getContext());
        _miniatureChart = chartsFabric.createChartMiniature(getContext());
        _bigChart = chartsFabric.createChartBig(getContext(), _miniatureChart);
        _bigChart.setDelegate(this);
        _chartDates = new TViewChartDatesHoriz(getContext());
        _chBoxChartIsActive = new TViewChartCheckBoxes(getContext(), this);

        addView(_titleCell);
        addView(_bigChart);
        addView(_chartDates);
        addView(_miniatureChart);
        addView(_chBoxChartIsActive);
    }

    @Override
    public void onBoxWasChecked(String key, boolean isChecked) {
        try {
            _chart.getLines().get(key).setIsActive(isChecked);
            _miniatureChart.wasChangedActiveChart();
            _bigChart.wasChangedActiveChart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMiniatureViewIsLocked(boolean isLocked) {
        if (_miniatureChart != null) {
            _miniatureChart.setMiniatureIsLocked(isLocked);
        }
    }

    @Override
    public void onDatesWasChanged(long[] dates) {
        if (_chartDates != null) {
            _chartDates.setNewDates(dates);
        }
    }

    @Override
    public void onDatesChangeSection(int leftInArray, int rightInArray) {
        if (_chartDates != null) {
            _chartDates.onDatesChangeSection(leftInArray, rightInArray);
        }
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

        _oldBackColor = this.getCurrentBackColor();
        this.setBackgroundColor(_oldBackColor);
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
