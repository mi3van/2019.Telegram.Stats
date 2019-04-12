package com.kitzapp.telegram_stats.customViews.charts.impl;

import android.graphics.Path;

import java.util.HashMap;

/**
 * Created by Ivan Kuzmin on 2019-04-11;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public interface TAbstractChartMiniatureInterface {

    interface Listener {

        void onRectCursorsWasChanged(float leftCursor, float rightCursor);

        void onLinesPathesWasChanged(HashMap<String, Path> linesPathes, int maxAxisXx);

    }
}
