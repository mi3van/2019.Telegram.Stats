package com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;
import com.kitzapp.telegram_stats.common.ArraysUtilites;
import com.kitzapp.telegram_stats.domain.model.chart.Chart;
import com.kitzapp.telegram_stats.common.MyLongPair;

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

    private ViewFollowersDelimiterVert _viewFollowersVert;

    private HashMap<String, long[]> _partAxisesY = new HashMap<>();
    private float[] _partAxisXForGraph = null;
    private float _leftCursor;
    private float _rightCursor;
    private ViewChartDates.Listener _datesListener;

    public ViewChartPart(Context context) {
        super(context);
    }

    public ViewChartPart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewChartPart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    ViewChartPart(Context context, @NonNull Chart chart, ViewChartDates.Listener datesListener) {
        super(context, chart);
        _datesListener = datesListener;
    }

    @Override
    protected void firstInitAxisesAndVariables(boolean isNeedInitForCanvas) {
        super.firstInitAxisesAndVariables(false);

        _viewFollowersVert = new ViewFollowersDelimiterVert(getContext());
        addView(_viewFollowersVert);
    }

    @Override
    protected void drawLines(Canvas canvas, float[] axisX, HashMap<String, long[]> partAxisesY) {
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

        if (_datesListener != null) {
            long[] dates = this.getDatesForSend(leftInArray, rightInArray);
            _datesListener.onDatesWasChanged(dates);
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
        float tempValueX;
        for (int i = 0; i < lengthX; i++) {
            tempValueX = originalAxisX[i] - leftInPx;
            tempValueX /= persent;
            newAxis[i] = tempValueX;
        }

        return newAxis;
    }

    private void updateMaxAndMin() {

        MyLongPair maxAndMinInPoint = this.getMaxAndMinInHashMap(_partAxisesY);

        long _tempMaxAxisY = maxAndMinInPoint.getMax();
        if (_tempMaxAxisY == INTEGER_MIN_VALUE) {
            return;
        }
        long _tempMinAxisY = maxAndMinInPoint.getMin();

        _viewFollowersVert.setDatesAndInit(_tempMaxAxisY, _tempMinAxisY);

        _partAxisesY = getAxisesForCanvas(_partAxisesY, _tempMaxAxisY, _tempMinAxisY);
    }

    private MyLongPair getMaxAndMinInHashMap(HashMap<String, long[]> hashMap) {
        long max = INTEGER_MIN_VALUE;
        long min = INTEGER_MAX_VALUE;
        boolean isActiveChart;
        long[] valuesArray;
        for (Map.Entry<String, long[]> entry: hashMap.entrySet()) {
            isActiveChart = getChartIsActive(entry.getKey());
            if (!isActiveChart) {
                continue;
            }
            valuesArray = hashMap.get(entry.getKey());
            for (long value : valuesArray) {
                if (value > max) {
                    max = value;
                }
                if (value < min) {
                    min = value;
                }
            }
        }
        return new MyLongPair(max, min);
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

    private long[] getDatesForSend(int leftCursorArray, int rightCursorArray) {
        long[] partdatesArray = ArraysUtilites.getRange(leftCursorArray, rightCursorArray, _chart.getAxisX().getData());
        int lengthPartArray = partdatesArray.length;

        int maxSizeSendingArray = 6;
        long[] sendingArray;

        if (lengthPartArray > maxSizeSendingArray) {
            sendingArray = new long[5];
            int index1 = 0;
            int index5 = lengthPartArray - 1;
            int index3 = index5 >> 1;
            int index2 = index3 >> 1;
            int index4 = index2 + index3;
            sendingArray[0] = partdatesArray[index1];
            sendingArray[1] = partdatesArray[index2];
            sendingArray[2] = partdatesArray[index3];
            sendingArray[3] = partdatesArray[index4];
            sendingArray[4] = partdatesArray[index5];
        } else {
            sendingArray = partdatesArray;
        }

        return sendingArray;
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
