package com.kitzapp.telegram_stats.presentation.ui.components.ChartView.cells;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.domain.model.chart.Chart;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class CellFullChart extends CellChartBaseSimple {

    private Chart _chart = null;
    private ViewFullChart _fullChart;

    public CellFullChart(Context context) {
        super(context);
    }

    public CellFullChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CellFullChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CellFullChart(Context context, @NonNull Chart chart) {
        super(context);
        this._chart = chart;
        this.init();
    }

    @Override
    protected void init() {
        if (_chart != null) {
            _fullChart = new ViewFullChart(getContext(), _chart);

            setGravity(Gravity.CENTER_VERTICAL);
            addView(_fullChart);
        }
    }

    public void setBackgroundColor(int color) {
        _fullChart.setBackgroundColor(color);
    }
}
