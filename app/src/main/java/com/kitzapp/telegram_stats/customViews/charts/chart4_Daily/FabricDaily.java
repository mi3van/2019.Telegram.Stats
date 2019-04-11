package com.kitzapp.telegram_stats.customViews.charts.chart4_Daily;

import android.content.Context;
import com.kitzapp.telegram_stats.customViews.charts.impl.TAbstractChartBig;
import com.kitzapp.telegram_stats.customViews.charts.impl.TAbstractChartMiniature;
import com.kitzapp.telegram_stats.customViews.charts.impl.TChartsFabric;
import com.kitzapp.telegram_stats.customViews.simple.TViewRectSelect;

/**
 * Created by Ivan Kuzmin on 2019-04-11;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class FabricDaily extends TChartsFabric {
    @Override
    public TAbstractChartBig createChartBig(Context context, TAbstractChartBig.BigChartInterface bigChartInterface) {
        return new ChartBigDaily(context, bigChartInterface);
    }

    @Override
    public TAbstractChartMiniature createChartMiniature(Context context, TViewRectSelect.RectListener listener) {
        return new ChartMiniatureDaily(context, listener);
    }
}
