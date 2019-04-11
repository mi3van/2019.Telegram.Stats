package com.kitzapp.telegram_stats.customViews.charts.chart3_Stacked;

import android.annotation.SuppressLint;
import android.content.Context;
import com.kitzapp.telegram_stats.customViews.charts.impl.TAbstractChartMiniature;
import com.kitzapp.telegram_stats.customViews.simple.TViewRectSelect;

/**
 * Created by Ivan Kuzmin on 2019-04-11;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

@SuppressLint("ViewConstructor")
class ChartMiniatureStacked extends TAbstractChartMiniature {

    public ChartMiniatureStacked(Context context, TViewRectSelect.RectListener rectListener) {
        super(context, rectListener);
    }
}
