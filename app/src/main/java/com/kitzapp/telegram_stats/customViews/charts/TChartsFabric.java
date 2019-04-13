package com.kitzapp.telegram_stats.customViews.charts;

import android.content.Context;
import com.kitzapp.telegram_stats.customViews.charts.base.TChartBigView;
import com.kitzapp.telegram_stats.customViews.charts.base.TChartMiniatureView;

/**
 * Created by Ivan Kuzmin on 2019-04-11;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public abstract class TChartsFabric {
    public abstract TChartBigView createChartBig(Context context, TChartMiniatureView chartMiniature);

    public abstract TChartMiniatureView createChartMiniature(Context context);
}
