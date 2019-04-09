package com.kitzapp.telegram_stats.core.mainChart.list;

import com.kitzapp.telegram_stats.pojo.chart.Chart;
import com.kitzapp.telegram_stats.pojo.chart.ChartsList;

/**
 * Created by Ivan Kuzmin on 09.04.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

class TChartListModel implements TChartListContract.TModel {

    private ChartsList _chartsList;

    TChartListModel(ChartsList chartsList) {
        this._chartsList = chartsList;
    }

    public Chart getChartOnPosition(int position) throws Exception {
        return _chartsList.getCharts().get(position);
    }

    @Override
    public int getItemCount() {
        return _chartsList.getCharts().size();
    }
}
