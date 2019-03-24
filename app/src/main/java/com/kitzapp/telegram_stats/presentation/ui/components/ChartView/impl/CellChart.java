package com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.domain.model.chart.Chart;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class CellChart extends LinearLayout {

    private Chart _chart = null;
    private ViewPartChart _partChart;
    private ViewFullChart _fullChart;
    private ViewChBoxChartIsActive _chBoxChartIsActive;

    public CellChart(Context context) {
        super(context);
    }

    public CellChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CellChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CellChart(Context context, @NonNull Chart chart) {
        super(context);
        this._chart = chart;
        this.init();
    }

    protected void init() {
        this.setOrientation(VERTICAL);

        if (_chart != null) {
//            _fullChart = new ViewFullChart(getContext(), _chart, (axisesYArrays,
//                                                                  _hashPaints,
//                                                                  _axisXForGraph,
//                                                                  columnsCount,
//                                                                  maxAsixY,
//                                                                  _viewHeight), () - >)
//                    _partChart.setData(axisesYArrays, _hashPaints, _axisXForGraph, _viewHeight));
            setGravity(Gravity.CENTER_VERTICAL);

            _partChart = new ViewPartChart(getContext(), _chart);
            _chBoxChartIsActive = new ViewChBoxChartIsActive(getContext(), _chart, (key, isChecked) -> {
                try {
                    _chart.getLines().get(key).setIsActive(isChecked);
//                    _fullChart.invalidate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            addView(_partChart);
//            addView(_fullChart);
            addView(_chBoxChartIsActive);
        }
    }

    public void setBackgroundColor(int color) {
        _fullChart.setBackgroundColor(color);
    }
}
