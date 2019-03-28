package com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.Application.AndroidApp;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.common.ArraysUtilites;
import com.kitzapp.telegram_stats.domain.model.chart.Chart;
import com.kitzapp.telegram_stats.domain.model.chart.impl.Line;
import com.kitzapp.telegram_stats.presentation.ui.components.TViewObserver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ivan Kuzmin on 25.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

abstract class ViewChartBase extends FrameLayout implements TViewObserver {
    private final int FLAG_Y_NOT_AVAILABLE = -5;

    @NonNull
    private Chart _chart;

    private float[] _axisXForGraph = null;
    private HashMap<String, int[]> _axisesYArrays = new HashMap<>();
    private HashMap<String, Paint> _paints;

    private float _viewHeight;
    protected float _viewWidth;
    protected int _maxAxisXx;
    private int _maxAxisY;

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

    ViewChartBase(Context context, @NonNull Chart chart) {
        super(context);
        _chart = chart;
        this.init();
    }

    @Override
    public void init() {
        setWillNotDraw(false);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, this.getViewHeightForLayout());
        int padding = ThemeManager.CHART_CELL_RIGHTLEFT_MARGIN_PX;
        layoutParams.setMargins(padding, 0, padding, 0);
        this.setLayoutParams(layoutParams);
    }

    abstract int getViewHeightForLayout();

    abstract int getLinePaintWidth();

    abstract int getMaxDotsForApproxChart();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int canvasHeight = canvas.getHeight();
        int canvasWidth = canvas.getWidth();

        if (_axisXForGraph != null) {
//            RECALCULATE
            boolean isNeedInitAxises = _viewHeight != canvasHeight || _viewWidth != canvasWidth;
            if (isNeedInitAxises) {
                _viewHeight = canvasHeight;
                _viewWidth = canvasWidth;
                this.initAxisX();
            }
        } else {
//            FIRST LAUNCH
            _viewHeight = canvasHeight;
            _viewWidth = canvasWidth;
            this.firstInitAxisesAndVariables(true);
        }

        this.drawLines(canvas, _axisXForGraph, _axisesYArrays);
    }

//    isNeedInitPaints
    protected void firstInitAxisesAndVariables(boolean isNeedInitForCanvas) {

        _axisesYArrays = this.getInitAxysesYAndInitPaints();

        initAxisX();

        if (isNeedInitForCanvas) {
            _axisesYArrays = getAxisesForCanvas(_axisesYArrays, _maxAxisY);
        }
    }

    HashMap<String, int[]> getInitAxysesYAndInitPaints() {
        HashMap<String, int[]> tempAxyses = new HashMap<>();

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
                int[] axisY = new int[currentColumnsCount];
                for (int i = 0; i < currentColumnsCount; i++) {
                    int currentY = line.getData()[i];
                    if (_maxAxisY < currentY) {
                        _maxAxisY = currentY;
                    }
                    axisY[i] = currentY;
                }
                tempAxyses.put(entry.getKey(), axisY);
            }
        }
        return tempAxyses;
    }

    protected void initAxisX() {
        float stepX = _viewWidth / (_maxAxisXx - 1);
        // CONVERT X AND Y's TO CANVAS SIZE
        _axisXForGraph = new float[_maxAxisXx];
        _axisXForGraph[0] = 0;
        for (int i = 1; i < _maxAxisXx; i++) {
            _axisXForGraph[i] = _axisXForGraph[i - 1] + stepX;
        }
    }

    protected HashMap<String, int[]> getAxisesForCanvas(HashMap<String, int[]> tempArray, int maxAxisY) {
        if (tempArray == null) {
            tempArray = new HashMap<>();
        }
        if (!tempArray.isEmpty()) {
            float _stepY = _viewHeight / (maxAxisY - 1);
            for (Map.Entry<String, int[]> entry : tempArray.entrySet()) {
//                FILLING CURRENT AXISY ARRAY
                int[] tempAxisY = entry.getValue();
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
                axisY = this.getApproximateArray(axisY, this.getMaxDotsForApproxChart());

                entry.setValue(axisY);
            }
        }

        return tempArray;
    }

    private int[] getApproximateArray(int[] arrayY, int maxCountDots) {
        int countDotsForApprox = _axisXForGraph.length;
        if (countDotsForApprox > maxCountDots) {
            int pointsCount = _axisXForGraph.length - 1; // -1 for save last point
            int currentApproxRange = 0;

            while (countDotsForApprox > maxCountDots) {
                currentApproxRange += 1;

                float oldX = _axisXForGraph[0], currentX;
                int oldY = arrayY[0], currentY;
                for (int i = 1; i < pointsCount; i++) {
                    currentY = arrayY[i];
                    if (currentY >= 0) {
                        currentX = _axisXForGraph[i];
                        boolean rangePointsIsAvailable = AndroidUtilites.isRangeLineAvailable(
                                oldX, oldY, currentX, currentY, currentApproxRange);

                        if (!rangePointsIsAvailable) {
                            countDotsForApprox--;
                            arrayY[i] = FLAG_Y_NOT_AVAILABLE;
                            if (countDotsForApprox <= maxCountDots) {
                                break;
                            }
                        }
                        oldX = currentX;
                        oldY = currentY;
                    }
                }
            }
            return arrayY;
        } else {
            return arrayY;
        }
    }

    protected void drawLines(Canvas canvas, float[] axisX, HashMap<String, int[]> partAxisesY) {
        if (!partAxisesY.isEmpty()) {
            for (Map.Entry<String, int[]> entry : partAxisesY.entrySet()) {
                Line line = _chart.getLines().get(entry.getKey());
                if (line != null && line.getIsActive()) {
                    int[] axisYForGraph = entry.getValue();
                    int columnsCount = axisYForGraph.length;

                    Path pathLine = new Path();
                    float firstX = axisX[0];
                    int firstY = axisYForGraph[0];

                    pathLine.moveTo(firstX, firstY);

                    for (int i = 0; i < columnsCount; i++) {
                        int currentY = axisYForGraph[i];
                        if (currentY >= 0) {
                            float currentX = axisX[i];
                            pathLine.lineTo(currentX, currentY);
                        }
                    }
                    Paint paint = _paints.get(entry.getKey());
                    if (paint != null) {
                        canvas.drawPath(pathLine, paint);
                    }
                }
            }
        }
    }

//    METHOD FOR PART VIEW
    protected HashMap<String, int[]> getPartOfFullHashAxisY(int leftInArray, int rightInArray) {
        HashMap<String, int[]> tempHashMap = new HashMap<>();

        for (Map.Entry<String, Line> entry : _chart.getLines().entrySet()) {
            Line line = entry.getValue();
            int currentColumnsCount = line.getCountDots();

            if (currentColumnsCount > 1) {
                int[] partInt = ArraysUtilites.getRange(leftInArray, rightInArray, line.getData());
                tempHashMap.put(entry.getKey(), partInt);
            }
        }
        return tempHashMap;
    }

//    METHOD FOR PART VIEW
    protected float[] getPartOfFullAxisX(int leftInArray, int rightInArray) {
        float[] partArray = ArraysUtilites.getRange(leftInArray, rightInArray, _axisXForGraph);
        return partArray;
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

//    private void checkMax(int x, int y) {
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
