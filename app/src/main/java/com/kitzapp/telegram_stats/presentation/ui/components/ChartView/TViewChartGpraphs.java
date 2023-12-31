package com.kitzapp.telegram_stats.presentation.ui.components.ChartView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.kitzapp.telegram_stats.domain.model.chart.Chart;
import com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl.ViewChartBig;
import com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl.ViewChartDatesHoriz;
import com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl.ViewChartMiniature;
import com.kitzapp.telegram_stats.presentation.ui.components.simple.TColorfulCheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TViewChartGpraphs extends LinearLayout implements TColorfulCheckBox.Listener, ViewChartDatesHoriz.Listener {

    private Chart _chart = null;
    private ViewChartBig _partChart;
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
            _partChart = new ViewChartBig(getContext(), _chart, this);
            _chartDates = new ViewChartDatesHoriz(getContext());
            _fullChart = new ViewChartMiniature(getContext(), _chart, _partChart.getRectListener());
            _chBoxChartIsActive = new TViewChartCheckBox(getContext(), _chart, this);

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
