package com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl;

import android.content.Context;
import android.util.AttributeSet;

import android.widget.Toast;
import com.kitzapp.telegram_stats.Application.AndroidApp;
import com.kitzapp.telegram_stats.Application.AppManagers.ObserverManager;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;
import com.kitzapp.telegram_stats.domain.model.chart.Chart;

import java.util.Observable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

class ViewChartPart extends ViewChartBase implements ViewRectSelect.RectListener {
    private final int MAX_DOTS_FOR_APPROX_CHART_PART = 512;

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
    int getViewHeightForLayout() {
        return ThemeManager.CHART_PART_HEIGHT_PX;
    }

    @Override
    int getLinePaintWidth() {
        return ThemeManager.CHART_LINE_IN_PART_WIDTH_PX;
    }

    @Override
    int getMaxDotsForApproxChart() {
        return MAX_DOTS_FOR_APPROX_CHART_PART;
    }

    @Override
    public void onRectCursorsWasChanged(float leftCursor, float rightCursor) {
        Toast.makeText(AndroidApp.context,
                String.format("Left cursor: \"%.3f\"; Right cursor: \"%.3f\"", leftCursor, rightCursor),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    ViewRectSelect.RectListener getRectListener() {
        return this;
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
