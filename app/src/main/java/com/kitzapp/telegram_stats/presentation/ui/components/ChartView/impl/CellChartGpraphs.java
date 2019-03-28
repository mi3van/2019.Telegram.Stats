package com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.kitzapp.telegram_stats.domain.model.chart.Chart;
import com.kitzapp.telegram_stats.presentation.ui.components.simple.TCheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class CellChartGpraphs extends LinearLayout implements TCheckBox.Listener, ViewChartDates.Listener {

    private Chart _chart = null;
    private ViewChartPart _partChart;
    private ViewChartDates _chartDates;
    private ViewChartFull _fullChart;
    private ViewChartCheckBox _chBoxChartIsActive;

    public CellChartGpraphs(Context context) {
        super(context);
    }

    public CellChartGpraphs(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CellChartGpraphs(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CellChartGpraphs(Context context, @NonNull Chart chart) {
        super(context);
        this._chart = chart;
        this.init();
    }

    protected void init() {
        this.setOrientation(VERTICAL);

        if (_chart != null) {
            _partChart = new ViewChartPart(getContext(), _chart, this);
            _chartDates = new ViewChartDates(getContext());
            _fullChart = new ViewChartFull(getContext(), _chart, _partChart.getRectListener());
            _chBoxChartIsActive = new ViewChartCheckBox(getContext(), _chart, this);

            addView(_partChart);
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
            _partChart.recalculateYAndUpdateView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDatesWasChanged(long[] dates) {
        _chartDates.setDatesAndInit(dates);
    }
}
