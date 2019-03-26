package com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kitzapp.telegram_stats.Application.AndroidApp;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.domain.model.chart.Chart;
import com.kitzapp.telegram_stats.domain.model.chart.impl.Line;
import com.kitzapp.telegram_stats.presentation.ui.components.TViewObserver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Ivan Kuzmin on 25.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

abstract public class ViewChartBase extends View implements TViewObserver {
    public static final int VALUE_Y_NOT_IN_RANGE = -5;

    @NonNull
    protected Chart _chart;

    protected float[] _axisXForGraph = null;
    protected HashMap<String, int[]> _axisesYArrays;
    protected HashMap<String, Paint> _paints;

    protected float _viewHeight, _viewWidth;
    protected int _maxAxisXx, _maxAxisY;

    public ViewChartBase(Context context) {
        super(context);
        this.init();
    }

    public ViewChartBase(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public ViewChartBase(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    public ViewChartBase(Context context, @NonNull Chart chart) {
        super(context);
        _chart = chart;
        this.init();
    }

    @Override
    public void init() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, this.getViewHeightForLayout());
        int padding = ThemeManager.CHART_CELL_RIGHTLEFT_MARGIN_PX;
        layoutParams.setMargins(padding, 0, padding, 0);
        this.setLayoutParams(layoutParams);
    }

    abstract int getViewHeightForLayout();

    abstract int getLinePaintWidth();

    abstract int getMaxCountDotsInScreen();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int canvasHeight = canvas.getHeight();
        int canvasWidth = canvas.getWidth();

        boolean isNeedInitAxises = true;
        if (_axisXForGraph != null) {
            if (_viewHeight == canvasHeight && _viewWidth == canvasWidth) {
                isNeedInitAxises = false;
            }
        }

        if (isNeedInitAxises){
            _viewHeight = canvasHeight;
            _viewWidth = canvasWidth;
            this.initAxisesAndVariables();
        }

        this.drawLines(canvas);
    }

//    isNeedInitPaints
    private void initAxisesAndVariables() {
        HashMap<String, float[]> tempResultsForY = new HashMap<>();
        _paints = new HashMap<>();
        for (Map.Entry<String, Line> entry : _chart.getLines().entrySet()) {
            Line line = entry.getValue();
            int currentColumnsCount = line.getCountDots();

            if (currentColumnsCount > 1) {

                // INIT MAX IN X AXIS
                if (_maxAxisXx < currentColumnsCount) {
                    _maxAxisXx = currentColumnsCount;
                }
                // INIT PAINT
                Paint linePaint = AndroidUtilites.getPaint(entry.getValue().getColor(), this.getLinePaintWidth());
                _paints.put(entry.getKey(), linePaint);

                // INIT AXIS Y AND FIND MAX Y
                float[] axisY = new float[currentColumnsCount];
                for (int i = 0; i < currentColumnsCount; i++) {
                    int currentY = line.getData()[i];
                    if (_maxAxisY < currentY) {
                        _maxAxisY = currentY;
                    }
                    axisY[i] = currentY;
                }
                tempResultsForY.put(entry.getKey(), axisY);
            }
        }

        this.initAxisX();

//      CONFIGURE Y ARRAYS FOR CANVAS
        _axisesYArrays = new HashMap<>();
        if (!tempResultsForY.isEmpty()) {
            float _stepY = _viewHeight / (_maxAxisY - 1);
            for (Map.Entry<String, float[]> entry : tempResultsForY.entrySet()) {
//                FILLING CURRENT AXISY ARRAY
                float[] tempAxisY = entry.getValue();
                int countDots = tempAxisY.length;
                int[] axisY = new int[countDots];

                for (int i = 0; i < countDots; i++) {
                    int convertedY = Math.round(_viewHeight - tempAxisY[i] * _stepY);
                    if (convertedY < 0) {
                        convertedY = 0;
                    }
                    axisY[i] = convertedY;
                }

//                APPROXIMATE POINTS AXISY ARRAY
                axisY = this.getApproximateArray(axisY,
                        this.getMaxCountDotsInScreen()
                );

                _axisesYArrays.put(entry.getKey(), axisY);
            }
        }
    }

    private int[] getApproximateArray(int[] arrayY, int maxCountDots) {
        int pointsCount = _axisXForGraph.length - 1; // -1 for save last point
        int[] approxArrayY = Arrays.copyOf(arrayY, arrayY.length);
        int countDotsForApprox = _axisXForGraph.length;
        float currentApproxRange = 0;

        while (countDotsForApprox > maxCountDots) {
            currentApproxRange += 1;

            float oldX = _axisXForGraph[0], oldY = approxArrayY[0];
            float currentX, currentY;
            for (int i = 1; i < pointsCount; i++) {
                currentY = approxArrayY[i];
                if (currentY >= 0) {
                    currentX = _axisXForGraph[i];
                    boolean rangePointsIsAvailable = AndroidUtilites.isRangeLineAvailable(
                            oldX, oldY, currentX, currentY, currentApproxRange);

                    if (!rangePointsIsAvailable) {
                        countDotsForApprox--;
                        approxArrayY[i] = ViewChartBase.VALUE_Y_NOT_IN_RANGE;
                        if (countDotsForApprox <= maxCountDots) {
                            break;
                        }
                    }
                    oldX = currentX;
                    oldY = currentY;
                }
            }
        }
        return approxArrayY;
    }

    private void initAxisX() {
        float stepX = _viewWidth / (_maxAxisXx - 1);
        // CONVERT X AND Y's TO CANVAS SIZE
        _axisXForGraph = new float[_maxAxisXx];
        _axisXForGraph[0] = 0;
        for (int i = 1; i < _maxAxisXx; i++) {
            _axisXForGraph[i] = _axisXForGraph[i - 1] + stepX;
        }
    }

    private void drawLines(Canvas canvas) {
        if (!_axisesYArrays.isEmpty()) {
            for (Map.Entry<String, int[]> entry : _axisesYArrays.entrySet()) {
                Line line = _chart.getLines().get(entry.getKey());
                Paint paint = _paints.get(entry.getKey());
                if (line != null && line.getIsActive() && paint != null) {
                    int[] axisYForGraph = entry.getValue();
                    int columnsCount = axisYForGraph.length;

                    float oldX = _axisXForGraph[0];
                    float oldY = axisYForGraph[0];
                    for (int i = 1; i < columnsCount; i++) {
                        float currentY = axisYForGraph[i];
                        if (currentY >= 0) {
                            float currentX = _axisXForGraph[i];
                            canvas.drawLine(oldX, oldY, currentX, currentY, paint);
                            oldX = currentX;
                            oldY = currentY;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.addObserver();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.deleteObserver();
    }

    @Override
    public void addObserver() {
        AndroidApp.observerManager.addObserver(this);
    }

    @Override
    public void deleteObserver() {
        AndroidApp.observerManager.deleteObserver(this);
    }

//    private void checkMax(float x, float y) {
//        if (x > _viewWidth + 0.01f) {
//            try {
//                throw new Exception("X is not a range: " + x + "; Max: " + _viewWidth);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        if (y > _viewHeight + 0.01f) {
//            try {
//                throw new Exception("Y is not a range: " + y + "; Max: " + _viewHeight);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
