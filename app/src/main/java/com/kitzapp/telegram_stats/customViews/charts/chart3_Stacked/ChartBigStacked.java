package com.kitzapp.telegram_stats.customViews.charts.chart3_Stacked;

import android.annotation.SuppressLint;
import android.content.Context;
import com.kitzapp.telegram_stats.customViews.charts.impl.InterfaceChartBig;
import com.kitzapp.telegram_stats.customViews.charts.impl.TAbstractChartBig;

/**
 * Created by Ivan Kuzmin on 2019-04-11;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

@SuppressLint("ViewConstructor")
class ChartBigStacked extends TAbstractChartBig {

    public ChartBigStacked(Context context, InterfaceChartBig.Listener bigChartInterface) {
        super(context, bigChartInterface);
    }
}
