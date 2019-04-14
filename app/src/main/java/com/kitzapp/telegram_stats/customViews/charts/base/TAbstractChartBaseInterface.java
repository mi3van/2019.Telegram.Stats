package com.kitzapp.telegram_stats.customViews.charts.base;

import com.kitzapp.telegram_stats.pojo.chart.Chart;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public interface TAbstractChartBaseInterface {

    void loadData(Chart chart);

    void wasChangedIsActiveChart();
}
