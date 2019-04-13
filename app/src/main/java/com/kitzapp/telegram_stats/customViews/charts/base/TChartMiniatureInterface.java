package com.kitzapp.telegram_stats.customViews.charts.base;

import android.graphics.Path;

import java.util.HashMap;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public interface TChartMiniatureInterface {

    interface Listener {

        void onRectCursorsWasChanged(float leftCursor, float rightCursor);

        void onLinesPathesWasChanged(HashMap<String, Path> linesPathes, int maxAxisXx);

    }
}
