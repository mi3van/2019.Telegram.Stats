package com.kitzapp.telegram_stats.customViews.ChartView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.customViews.ChartView.impl.ViewChartBig;
import com.kitzapp.telegram_stats.customViews.ChartView.impl.ViewChartDatesHoriz;
import com.kitzapp.telegram_stats.customViews.ChartView.impl.ViewChartMiniature;
import com.kitzapp.telegram_stats.customViews.simple.TColorfulCheckBox;
import com.kitzapp.telegram_stats.pojo.chart.Chart;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TViewChartGpraphs extends LinearLayout implements TColorfulCheckBox.Listener, ViewChartBig.BigChartInterface {

    private Chart _chart = null;
    private ViewChartBig _bigChart;
    private ViewChartDatesHoriz _chartDates;
    private ViewChartMiniature _miniatureChart;
    private TViewChartCheckBox _chBoxChartIsActive;

    public TViewChartGpraphs(Context context) {
        super(context);
        this.init();
    }

    public TViewChartGpraphs(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TViewChartGpraphs(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    public void loadData(@NonNull Chart chart) {
        if (_chart == null) {
            this._chart = chart;
            _bigChart.loadData(_chart);
            _miniatureChart.loadData(_chart);
            _chBoxChartIsActive.loadData(_chart);
        }
    }

    protected void init() {
        this.setOrientation(VERTICAL);

        _bigChart = new ViewChartBig(getContext(), this);
        _chartDates = new ViewChartDatesHoriz(getContext());
        _miniatureChart = new ViewChartMiniature(getContext(), _bigChart.getRectListener());
        _chBoxChartIsActive = new TViewChartCheckBox(getContext(), this);

        addView(_bigChart);
        addView(_chartDates);
        addView(_miniatureChart);
        addView(_chBoxChartIsActive);

        setGravity(Gravity.CENTER_VERTICAL);
    }

    public void setBackgroundColor(int color) {
        _miniatureChart.setBackgroundColor(color);
    }

    @Override
    public void onBoxWasChecked(String key, boolean isChecked) {
        try {
            _chart.getLines().get(key).setIsActive(isChecked);
            _miniatureChart.invalidate();
            _bigChart.recalculateYAndUpdateView();
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
        _chartDates.setDatesAndInit(dates);
    }
}
