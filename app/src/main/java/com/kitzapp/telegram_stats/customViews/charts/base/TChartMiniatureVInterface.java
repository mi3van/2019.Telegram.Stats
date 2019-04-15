package com.kitzapp.telegram_stats.customViews.charts.base;

import java.util.HashMap;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public interface TChartMiniatureVInterface {

    void setMiniatureIsLocked(boolean isLocked);

    interface Listener {

        void onRectCursorsWasChanged(float leftCursor, float rightCursor);

        void onDataWasRecalculated(float[] _axisXForCanvas, HashMap<String, long[]> orginalYArray,
                                            int maxConstAxisX, long maxConstaAxisY);

    }
}
