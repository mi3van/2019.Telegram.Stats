package com.kitzapp.telegram_stats.customViews.charts;

import com.kitzapp.telegram_stats.customViews.charts.chart2_TwoY.FabricTwoY;
import com.kitzapp.telegram_stats.customViews.charts.chart3_Stacked.FabricStacked;
import com.kitzapp.telegram_stats.customViews.charts.chart4_Daily.FabricDaily;
import com.kitzapp.telegram_stats.customViews.charts.chart5_Percentage.FabricPercentage;
import com.kitzapp.telegram_stats.customViews.charts.impl.TChartsFabric;
import com.kitzapp.telegram_stats.customViews.charts.chart1_OnceY.FabricOnceY;

import static com.kitzapp.telegram_stats.pojo.chart.Chart.*;

/**
 * Created by Ivan Kuzmin on 2019-04-11;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

class TChartsAbstractFabric {

    static TChartsFabric createChartFabric(byte typeChart) throws Exception {
        if (typeChart == CHART_TYPE_ONCE_Y) {
            return new FabricOnceY();
        } else if (typeChart == CHART_TYPE_TWO_Y) {
            return new FabricTwoY();
        } else if (typeChart == CHART_TYPE_STACKED) {
            return new FabricStacked();
        } else if (typeChart == CHART_TYPE_DAILY) {
            return new FabricDaily();
        } else if (typeChart == CHART_TYPE_PERCENTAGE) {
            return new FabricPercentage();
        } else {
            throw new Exception(String.format("Chart with type: \"%d\" is not found", typeChart));
        }
    }
}