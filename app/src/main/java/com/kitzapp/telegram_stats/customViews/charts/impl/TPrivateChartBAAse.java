package com.kitzapp.telegram_stats.customViews.charts.impl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.common.ArraysUtilites;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
import com.kitzapp.telegram_stats.pojo.chart.Chart;
import com.kitzapp.telegram_stats.pojo.chart.impl.Line;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ivan Kuzmin on 25.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

abstract class TPrivateChartBAAse extends FrameLayout {

    protected float _viewHeight;
    protected float _viewWidth;

    public TPrivateChartBAAse(Context context) {
        super(context);
        this.init();
    }

    public TPrivateChartBAAse(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TPrivateChartBAAse(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    protected void init() {
        setWillNotDraw(false);
    }

    protected abstract int getLinePaintWidth();
    protected abstract int getChartVerticalPadding();
    protected abstract int getChartHalfVerticalPadding();
    protected abstract int getViewHeightForLayout();
    public abstract void wasChangedActiveChart();

////    METHOD FOR PART VIEW
//    protected HashMap<String, long[]> getPartOfFullHashAxisY(int leftInArray, int rightInArray) {
//        HashMap<String, long[]> tempHashMap = new HashMap<>();
//        Line line;
//        int currentColumnsCount;
//        long[] partInt;
//        for (Map.Entry<String, Line> entry : _chart.getLines().entrySet()) {
//            line = entry.getValue();
//            currentColumnsCount = line.getCountDots();
//
//            if (currentColumnsCount > 1) {
//                partInt = ArraysUtilites.getRange(leftInArray, rightInArray, line.getData());
//                tempHashMap.put(entry.getKey(), partInt);
//            }
//        }
//        return tempHashMap;
//    }
//
////    METHOD FOR PART VIEW
//    protected float[] getPartOfFullAxisX(int leftInArray, int rightInArray) {
//        float[] partArray = ArraysUtilites.getRange(leftInArray, rightInArray, _axisXForGraph);
//        return partArray;
//    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = this.getViewHeightForLayout();

        int paddingPx = this.getPaddingRightLeft();
        setPadding(paddingPx, 0, paddingPx, 0);
        this.setLayoutParams(layoutParams);
    }

    protected int getPaddingRightLeft() {
        return ThemeManager.MARGIN_16DP_IN_PX;
    }
}
