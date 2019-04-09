package com.kitzapp.telegram_stats.customViews.ChartView.impl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.AndroidApp;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.common.ArraysUtilites;
import com.kitzapp.telegram_stats.pojo.chart.Chart;
import com.kitzapp.telegram_stats.pojo.chart.impl.Line;
import com.kitzapp.telegram_stats.core.appManagers.TViewObserver;

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
    protected Chart _chart;

    private float[] _axisXForGraph = null;
    private HashMap<String, long[]> _axisesYArrays = new HashMap<>();
    private HashMap<String, Paint> _paints;

    private float _viewHeight;
    protected float _viewWidth;
    protected int _maxAxisXx;
    private long _maxAxisY;

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
    }

    abstract int getViewHeightForLayout();
    abstract int getLinePaintWidth();
    abstract int getMaxDotsForApproxChart();
    abstract int getChartVerticalPadding();
    abstract int getChartHalfVerticalPadding();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int canvasHeight = getHeight() - getChartVerticalPadding();
        int canvasWidth = getWidth();

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

    private HashMap<String, long[]> getInitAxysesYAndInitPaints() {
        HashMap<String, long[]> tempAxyses = new HashMap<>();

        _paints = new HashMap<>();
        Line line;
        int currentColumnsCount;
        for (Map.Entry<String, Line> entry : _chart.getLines().entrySet()) {
            line = entry.getValue();
            currentColumnsCount = line.getCountDots();

            if (currentColumnsCount > 1) {

                // INIT MAX IN X AXIS
                if (_maxAxisXx < currentColumnsCount) {
                    _maxAxisXx = currentColumnsCount;
                }

                // INIT PAINT
                Paint linePaint = AndroidUtilites.getPaint(entry.getValue().getColor(), this.getLinePaintWidth());
                _paints.put(entry.getKey(), linePaint);

                // INIT AXIS Y AND FIND MAX Y
                long[] axisY = new long[currentColumnsCount];
                long currentY;
                for (int i = 0; i < currentColumnsCount; i++) {
                    currentY = line.getData()[i];
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
        float stepX = (_viewWidth) / (_maxAxisXx - 1);
        // CONVERT X AND Y's TO CANVAS SIZE
        _axisXForGraph = new float[_maxAxisXx];
        _axisXForGraph[0] = 0;
        for (int i = 1; i < _maxAxisXx; i++) {
            _axisXForGraph[i] = _axisXForGraph[i - 1] + stepX;
        }
    }

    private HashMap<String, long[]> getAxisesForCanvas(HashMap<String, long[]> tempArray, long maxAxisY) {
        return this.getAxisesForCanvas(tempArray, maxAxisY, 0);
    }

    protected HashMap<String, long[]> getAxisesForCanvas(HashMap<String, long[]> tempArray,
                                                        long maxAxisY,
                                                        long minAxisY) {
        if (tempArray == null) {
            tempArray = new HashMap<>();
        }
        if (!tempArray.isEmpty()) {
            float widthInPx = maxAxisY - minAxisY;
            float persent = widthInPx / _viewHeight;
            long[] tempAxisY;
            int countDots;
            long[] axisY;
            float tempValue;
            long convertedY;
            for (Map.Entry<String, long[]> entry : tempArray.entrySet()) {
//                FILLING CURRENT AXISY ARRAY
                tempAxisY = entry.getValue();
                countDots = tempAxisY.length;
                axisY = new long[countDots];

                for (int i = 0; i < countDots; i++) {
                    tempValue = tempAxisY[i] - minAxisY;
                    tempValue /= persent;
                    convertedY = (long) (_viewHeight - tempValue + getChartHalfVerticalPadding());
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

    private long[] getApproximateArray(long[] arrayY, int maxCountDots) {
        int countDotsForApprox = _axisXForGraph.length;
        if (countDotsForApprox > maxCountDots) {
            int pointsCount = _axisXForGraph.length - 1; // -1 for save last point
            int currentApproxRange = 0;
            float oldX, currentX;
            long oldY, currentY;
            while (countDotsForApprox > maxCountDots) {
                currentApproxRange += 1;
                oldX = _axisXForGraph[0];
                oldY = arrayY[0];
                boolean rangePointsIsAvailable;

                for (int i = 1; i < pointsCount; i++) {
                    currentY = arrayY[i];
                    if (currentY >= 0) {
                        currentX = _axisXForGraph[i];
                        rangePointsIsAvailable = AndroidUtilites.isRangeLineAvailable(
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

    protected void drawLines(Canvas canvas, float[] axisX, HashMap<String, long[]> partAxisesY) {
        if (!partAxisesY.isEmpty()) {
            Line line;
            boolean isActiveChart;
            for (Map.Entry<String, long[]> entry : partAxisesY.entrySet()) {
                line = _chart.getLines().get(entry.getKey());
                isActiveChart = getChartIsActive(entry.getKey());
                if (line != null && isActiveChart) {
                    long[] axisYForGraph = entry.getValue();
                    int columnsCount = axisYForGraph.length;

                    Path pathLine = new Path();
                    float firstX = axisX[0];
                    long firstY = axisYForGraph[0];

                    pathLine.moveTo(firstX, firstY);
                    long currentY; float currentX;
                    for (int i = 0; i < columnsCount; i++) {
                        currentY = axisYForGraph[i];
                        if (currentY >= 0) {
                            currentX = axisX[i];
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
    protected HashMap<String, long[]> getPartOfFullHashAxisY(int leftInArray, int rightInArray) {
        HashMap<String, long[]> tempHashMap = new HashMap<>();
        Line line;
        int currentColumnsCount;
        long[] partInt;
        for (Map.Entry<String, Line> entry : _chart.getLines().entrySet()) {
            line = entry.getValue();
            currentColumnsCount = line.getCountDots();

            if (currentColumnsCount > 1) {
                partInt = ArraysUtilites.getRange(leftInArray, rightInArray, line.getData());
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

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = this.getViewHeightForLayout();
        int marginPx = ThemeManager.CHART_CELL_RIGHTLEFT_MARGIN_PX;
        layoutParams.setMargins(marginPx, 0, marginPx, 0);
        this.setLayoutParams(layoutParams);

        this.addObserver();
    }

    protected boolean getChartIsActive(String key) {
        boolean isActive = false;
        try {
            isActive = _chart.getLines().get(key).getIsActive();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return isActive;
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

}
