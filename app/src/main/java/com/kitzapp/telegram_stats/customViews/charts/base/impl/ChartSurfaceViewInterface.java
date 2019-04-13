package com.kitzapp.telegram_stats.customViews.charts.base.impl;

import android.graphics.Canvas;

/**
 * Created by Ivan Kuzmin on 13.04.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public interface ChartSurfaceViewInterface {

    interface Listener {

        void onSurfaceDraw(Canvas canvas);

    }
}
