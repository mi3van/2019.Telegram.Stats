package com.kitzapp.telegram_stats.customViews.charts.chart3_Stacked;

import android.content.Context;
import com.kitzapp.telegram_stats.customViews.charts.base.TChartBigView;
import com.kitzapp.telegram_stats.customViews.charts.base.TChartMiniatureView;
import com.kitzapp.telegram_stats.customViews.charts.TChartsFabric;

/**
 * Created by Ivan Kuzmin on 2019-04-11;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class FabricStacked extends TChartsFabric {
    @Override
    public TChartBigView createChartBig(Context context, TChartMiniatureView chartMiniature) {
        return new ChartBigStacked(context, chartMiniature);
    }

    @Override
    public TChartMiniatureView createChartMiniature(Context context) {
        return new ChartMiniatureStacked(context);
    }
}
