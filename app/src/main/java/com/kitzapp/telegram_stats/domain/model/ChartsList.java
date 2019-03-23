package com.kitzapp.telegram_stats.domain.model;

import com.kitzapp.telegram_stats.domain.model.chart.Chart;

import java.util.ArrayList;

/**
 * Created by Ivan Kuzmin on 23.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class ChartsList {

    private ArrayList<Chart> charts = new ArrayList<>();

    public void setCharts(ArrayList<Chart> charts) {
        this.charts = charts;
    }

    public void addChart(Chart chart) {
        charts.add(chart);
    }

    public ArrayList<Chart> getCharts() {
        return charts;
    }
}
