package com.kitzapp.telegram_stats.customViews.charts.chart2_TwoY;

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
class ChartBigTwoY extends TAbstractChartBig {

    ChartBigTwoY(Context context, TAbstractChartBigInterface.Listener bigChartInterface) {
        super(context, bigChartInterface);
    }
}
