package com.kitzapp.telegram_stats.customViews.ChartView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.kitzapp.telegram_stats.pojo.chart.Chart;
import com.kitzapp.telegram_stats.customViews.ChartView.impl.ViewChartBig;
import com.kitzapp.telegram_stats.customViews.ChartView.impl.ViewChartDatesHoriz;
import com.kitzapp.telegram_stats.customViews.ChartView.impl.ViewChartMiniature;
import com.kitzapp.telegram_stats.customViews.simple.TColorfulCheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TViewChartGpraphs extends LinearLayout implements TColorfulCheckBox.Listener, ViewChartBig.BigChartInterface {

    private Chart _chart = null;
    private ViewChartBig _bigChart;
    private ViewChartDatesHoriz _chartDates;
    private ViewChartMiniature _fullChart;
    private TViewChartCheckBox _chBoxChartIsActive;

    public TViewChartGpraphs(Context context) {
        super(context);
    }

    public TViewChartGpraphs(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TViewChartGpraphs(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TViewChartGpraphs(Context context, @NonNull Chart chart) {
        super(context);
        this._chart = chart;
        this.init();
    }

    protected void init() {
        this.setOrientation(VERTICAL);

        if (_chart != null) {
            _bigChart = new ViewChartBig(getContext(), _chart, this);
            _chartDates = new ViewChartDatesHoriz(getContext());
            _fullChart = new ViewChartMiniature(getContext(), _chart, _bigChart.getRectListener());
            _chBoxChartIsActive = new TViewChartCheckBox(getContext(), _chart, this);

            addView(_bigChart);
            addView(_chartDates);
            addView(_fullChart);
            addView(_chBoxChartIsActive);

            setGravity(Gravity.CENTER_VERTICAL);
        }
    }

    public void setBackgroundColor(int color) {
        _fullChart.setBackgroundColor(color);
    }

    @Override
    public void onBoxWasChecked(String key, boolean isChecked) {
        try {
            _chart.getLines().get(key).setIsActive(isChecked);
            _fullChart.invalidate();
            _bigChart.recalculateYAndUpdateView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMiniatureViewIsLocked(boolean isLocked) {
        if (_fullChart != null) {
            _fullChart.setMiniatureIsLocked(isLocked);
        }
    }

    @Override
    public void onDatesWasChanged(long[] dates) {
        _chartDates.setDatesAndInit(dates);
    }
}
