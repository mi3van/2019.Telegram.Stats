package com.kitzapp.telegram_stats.customViews.charts.impl;

import com.kitzapp.telegram_stats.customViews.simple.TViewRectSelect;

/**
 * Created by Ivan Kuzmin on 2019-04-11;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public interface TAbstractChartBigInterface {

    TViewRectSelect.RectListener getRectListener();

    interface Listener {

        void onMiniatureViewIsLocked(boolean isLocked);

        void onDatesWasChanged(long[] dates);
    }
}
