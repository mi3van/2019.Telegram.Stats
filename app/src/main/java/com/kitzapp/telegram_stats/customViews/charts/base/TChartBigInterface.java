package com.kitzapp.telegram_stats.customViews.charts.base;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public interface TChartBigInterface {

    interface Listener {

        void onMiniatureViewIsLocked(boolean isLocked);

        void onDatesWasChanged(long[] dates);

        void onDatesChangeSection(int leftInArray, int rightInArray);
    }
}
