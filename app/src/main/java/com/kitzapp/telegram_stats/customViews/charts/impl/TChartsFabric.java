package com.kitzapp.telegram_stats.customViews.charts.impl;

import android.content.Context;
import com.kitzapp.telegram_stats.customViews.simple.TViewRectSelect;

/**
 * Created by Ivan Kuzmin on 2019-04-11;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public abstract class TChartsFabric {
    public abstract TAbstractChartBig createChartBig(Context context, InterfaceChartBig.Listener bigChartInterface);

    public abstract TAbstractChartMiniature createChartMiniature(Context context, TViewRectSelect.RectListener listener);
}
