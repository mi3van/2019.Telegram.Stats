package com.kitzapp.telegram_stats.customViews.charts.chart1_OnceY;

import android.annotation.SuppressLint;
import android.content.Context;
import com.kitzapp.telegram_stats.customViews.charts.impl.TAbstractChartBigInterface;
import com.kitzapp.telegram_stats.customViews.charts.impl.TAbstractChartBig;

/**
 * Created by Ivan Kuzmin on 2019-04-11;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

@SuppressLint("ViewConstructor")
class ChartBigOnceY extends TAbstractChartBig {

    public ChartBigOnceY(Context context, TAbstractChartBigInterface.Listener bigChartInterface) {
        super(context, bigChartInterface);
    }
}
