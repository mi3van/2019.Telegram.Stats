package com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.domain.model.chart.Chart;
import com.kitzapp.telegram_stats.presentation.ui.components.impl.TCheckBox;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class CellChartGpraphs extends LinearLayout implements TCheckBox.Listener {

    private Chart _chart = null;
    private ViewPartChart _partChart;
    private ViewFullChart _fullChart;
    private ViewChBoxIsActive _chBoxChartIsActive;

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
            _partChart = new ViewPartChart(getContext(), _chart);
            _fullChart = new ViewFullChart(getContext(), _chart);
            _chBoxChartIsActive = new ViewChBoxIsActive(getContext(), _chart, this);
            addView(_partChart);
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
            _partChart.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
