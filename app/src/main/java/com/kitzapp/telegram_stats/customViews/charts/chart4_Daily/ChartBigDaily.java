package com.kitzapp.telegram_stats.customViews.charts.chart4_Daily;

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
class ChartBigDaily extends TAbstractChartBig {

    public ChartBigDaily(Context context, InterfaceChartBig.Listener bigChartInterface) {
        super(context, bigChartInterface);
    }
}
