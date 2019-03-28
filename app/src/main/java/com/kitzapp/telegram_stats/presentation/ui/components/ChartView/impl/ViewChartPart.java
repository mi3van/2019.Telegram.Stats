package com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;
import com.kitzapp.telegram_stats.domain.model.chart.Chart;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import static com.kitzapp.telegram_stats.common.AppConts.INTEGER_MAX_VALUE;
import static com.kitzapp.telegram_stats.common.AppConts.INTEGER_MIN_VALUE;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

class ViewChartPart extends ViewChartBase implements ViewRectSelect.RectListener {
//    private final int MAX_DOTS_FOR_APPROX_CHART_PART = 512;
    private HashMap<String, int[]> _partAxisesY = new HashMap<>();
    private float[] _partAxisXForGraph = null;
    private float _leftCursor;
    private float _rightCursor;

    public ViewChartPart(Context context) {
        super(context);
    }

    public ViewChartPart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewChartPart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    ViewChartPart(Context context, @NonNull Chart chart) {
        super(context, chart);
    }

    @Override
    protected void firstInitAxisesAndVariables(boolean isNeedInitForCanvas) {
        super.firstInitAxisesAndVariables(false);
        this.setBackgroundColor(Color.CYAN);
    }

    @Override
    protected void drawLines(Canvas canvas, float[] axisX, HashMap<String, int[]> partAxisesY) {
        if (_partAxisXForGraph != null) {
            super.drawLines(canvas, _partAxisXForGraph, _partAxisesY);
        }
    }

    void recalculateYAndUpdateView() {
        this.onRectCursorsWasChanged(_leftCursor, _rightCursor);
    }

    @Override
    public void onRectCursorsWasChanged(float leftCursor, float rightCursor) {
        _leftCursor = leftCursor;
        _rightCursor = rightCursor;
        int leftInArray = (int) (_maxAxisXx * leftCursor);
        int rightInArray = (int) (_maxAxisXx * rightCursor);

        int countPoints = rightInArray - leftInArray;

        if (countPoints < 2) {
            return;
        }

        // Get new part arrays for draw Y
        _partAxisesY = getPartOfFullHashAxisY(leftInArray, rightInArray);
        // Get new part arrays for draw X
        _partAxisXForGraph = getPartOfFullAxisX(leftInArray, rightInArray);

        float leftInPx = leftCursor * _viewWidth;
        float rightInPx = rightCursor * _viewWidth;
        _partAxisXForGraph = getAxisXCalculated(leftInPx, rightInPx, _partAxisXForGraph);

        this.updateMaxAndMin();

        invalidate();
    }

    private float[] getAxisXCalculated(float leftInPx, float rightInPx, float[] originalAxisX) {
        int lengthX = originalAxisX.length;
        float[] newAxis = new float[lengthX];

        float widthInPx = rightInPx - leftInPx;
        float persent = widthInPx / _viewWidth;

        for (int i = 0; i < lengthX; i++) {
            float tempValueX = originalAxisX[i] - leftInPx;
            tempValueX /= persent;
            newAxis[i] = tempValueX;
        }

        return newAxis;
    }

    private void updateMaxAndMin() {

        Point maxAndMinInPoint = this.getMaxAndMinInHashMap(_partAxisesY);

        int _tempMaxAxisY = maxAndMinInPoint.x;
        if (_tempMaxAxisY == INTEGER_MIN_VALUE) {
            return;
        }
        int _tempMinAxisY = maxAndMinInPoint.y;

        _partAxisesY = getAxisesForCanvas(_partAxisesY, _tempMaxAxisY, _tempMinAxisY);
    }

    private Point getMaxAndMinInHashMap(HashMap<String, int[]> hashMap) {
        int max = INTEGER_MIN_VALUE;
        int min = INTEGER_MAX_VALUE;
        for (Map.Entry<String, int[]> entry: hashMap.entrySet()) {
            boolean isActiveChart = getChartIsActive(entry.getKey());
            if (!isActiveChart) {
                continue;
            }
            int[] valuesArray = hashMap.get(entry.getKey());
            for (int value : valuesArray) {
                if (value > max) {
                    max = value;
                }
                if (value < min) {
                    min = value;
                }
            }
        }
        return new Point(max, min);
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    ViewRectSelect.RectListener getRectListener() {
        return this;
    }

    @Override
    int getViewHeightForLayout() {
        return ThemeManager.CHART_PART_HEIGHT_PX;
    }

    @Override
    int getLinePaintWidth() {
        return ThemeManager.CHART_LINE_IN_PART_WIDTH_PX;
    }

    @Override
    int getMaxDotsForApproxChart() {
        return _maxAxisXx;
    }

//    private void drawText(Canvas canvas){
//        canvas.save();
//        Rect bounds = new Rect();
//        float widthText = _textPaint.measureText(String.valueOf(_number));
//        float heightHeight = measureHeight();
//        canvas.drawText(String.valueOf(_number), _centerX - widthText * 0.5f, _centerY + heightHeight, _textPaint);
//        canvas.restore();
//    }
}
