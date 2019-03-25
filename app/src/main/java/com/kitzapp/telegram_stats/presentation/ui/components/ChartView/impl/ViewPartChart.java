package com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.Application.AndroidApp;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;
import com.kitzapp.telegram_stats.domain.model.chart.Chart;
import com.kitzapp.telegram_stats.domain.model.chart.impl.Line;
import com.kitzapp.telegram_stats.presentation.ui.components.TViewObserver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class ViewPartChart extends View implements TViewObserver {
//    private static final float APPROX_RANGE = 1.5f;

    @NonNull
    private Chart _chart;

    private HashMap<String, float[]> _axisesYArrays = null;
    private HashMap<String, Paint> _hashPaints;
    private float[] _axisXForGraph;

    private float _viewHeight = 0;
    private float _smallViewHeight = 0;
    private float _viewWidth = 0;
    private int _columnsCount;

    private float _stepX;
    private float _stepY;

    private boolean isNeedInitY = true;

    public ViewPartChart(Context context) {
        super(context);
    }

    public ViewPartChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewPartChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewPartChart(Context context,
                         Chart chart) {
        super(context);
        _chart = chart;
        this.init();
    }

    public void setData(
            HashMap<String, float[]> axisesYArrays,
            HashMap<String, Paint> hashPaints,
            float[] axisXForGraph,
            int columnsCount,
            float viewHeight) {

        _axisesYArrays = new HashMap<>();
        Map tmp = new HashMap(axisesYArrays);
        tmp.keySet().removeAll(_axisesYArrays.keySet());
        _axisesYArrays.putAll(tmp);

        _hashPaints = new HashMap<>();
        tmp = new HashMap(hashPaints);
        tmp.keySet().removeAll(_hashPaints.keySet());
        _hashPaints.putAll(tmp);

        _axisXForGraph = Arrays.copyOf(axisXForGraph, axisesYArrays.size());
        _columnsCount = new Integer(columnsCount);
        _smallViewHeight = new Float(viewHeight);
        this.invalidate();
    }

    @Override
    public void init() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ThemeManager.CHART_CELL_ZOOM_HEIGHT_PX);
        this.setLayoutParams(layoutParams);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (_axisesYArrays != null) {
            this.initVariables(canvas);
            drawLines(canvas);
        }
    }

    private void initVariables(Canvas canvas) {
        int canvasWidth = canvas.getWidth();
        if (_viewWidth != canvasWidth) {
            if (isNeedInitY) {
                _viewHeight = canvas.getHeight();
            }
            _viewWidth = canvasWidth;

            this.initLinesPaths();
        }
    }

    private void initLinesPaths() {
        this.initY();
        this.initX();
    }

    private void initX() {
        _stepX = _viewWidth / (_columnsCount - 1);

        _axisXForGraph = new float[_columnsCount];
        _axisXForGraph[0] = 0;
        for (int i = 1; i < _columnsCount; i++) {
            _axisXForGraph[i] = _axisXForGraph[i - 1] + _stepX;
        }
    }

    private void initY() {
        if (isNeedInitY) {
            synchronized (this) {
                _stepY = _viewHeight / _smallViewHeight;

                // CONVERT Y's TO CANVAS SIZE
                for (Map.Entry<String, float[]> entry : _axisesYArrays.entrySet()) {
                    float[] axisY = entry.getValue();
                    int count = axisY.length;

                    for (int i = 0; i < count; i++) {
                        axisY[i] = axisY[i] * _stepY;
                    }
                }
                isNeedInitY = false;
            }
        }
    }

    private void drawLines(Canvas canvas) {
        for (Map.Entry<String, float[]> entry: _axisesYArrays.entrySet()) {
            Line line = _chart.getLines().get(entry.getKey());
            Paint paint = _hashPaints.get(entry.getKey());
            if (line != null && line.getIsActive() && paint != null) {
                float[] axisYForGraph = entry.getValue();
                int columnsCount = axisYForGraph.length;

//                this.checkMax(_axisXForGraph[0], axisYForGraph[0]);
                float oldX = _axisXForGraph[0];
                float oldY = axisYForGraph[0];
                for (int i = 1; i < columnsCount; i++) {
//                    this.checkMax(_axisXForGraph[i], axisYForGraph[i]);
                    float currentX = _axisXForGraph[i];
                    float currentY = axisYForGraph[i];
                    canvas.drawLine(oldX, oldY, currentX, currentY, paint);
                    oldX = currentX;
                    oldY = currentY;
                }
            }
        }
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

//    private void drawText(Canvas canvas){
//        canvas.save();
//        Rect bounds = new Rect();
//        float widthText = _textPaint.measureText(String.valueOf(_number));
//        float heightHeight = measureHeight();
//        canvas.drawText(String.valueOf(_number), _centerX - widthText * 0.5f, _centerY + heightHeight, _textPaint);
//        canvas.restore();
//    }

    @Override
    public void addObserver() {
        AndroidApp.observerManager.addObserver(this);
    }

    @Override
    public void deleteObserver() {
        AndroidApp.observerManager.deleteObserver(this);
    }

    private int getCurrentColor() {
        return ThemeManager.simpleTextPaint.getColor();
    }

    @Override
    public void update(Observable o, Object arg) {
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
}
