package com.kitzapp.telegram_stats.customViews.charts.impl;

/**
 * Created by Ivan Kuzmin on 2019-04-11;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public interface TAbstractChartBigInterface {

    interface Listener {

        void onMiniatureViewIsLocked(boolean isLocked);

        void onDatesWasChanged(long[] dates);
    }
}
