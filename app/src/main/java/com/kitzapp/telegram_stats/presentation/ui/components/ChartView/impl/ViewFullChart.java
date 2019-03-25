package com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl;

import android.animation.ValueAnimator;
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
import com.kitzapp.telegram_stats.Application.AppManagers.ObserverManager;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.domain.model.chart.Chart;
import com.kitzapp.telegram_stats.domain.model.chart.impl.Line;
import com.kitzapp.telegram_stats.presentation.ui.components.TViewObserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class ViewFullChart extends View implements TViewObserver {
    private static final float APPROX_RANGE = 8f;

    public interface Loading {
        void complete(HashMap<String, float[]> axisesYArrays,
                      HashMap<String, Paint> _hashPaints,
                      float[] _axisXForGraph,
                      int columnsCount,
                      float _viewHeight);
    }
    private Loading loading;

    @NonNull
    private Chart _chart;
    private HashMap<String, float[]> _axisesYArrays;
    private HashMap<String, Paint> _hashPaints;
    private float[] _axisXForGraph;

    private float _viewHeight = 0;
    private float _viewWidth = 0;
    private int _columnsCount;
    private int _maxAxisY = Integer.MIN_VALUE;

    private float _stepX;
    private float _stepY;

    private int _oldFullChartBackColor;
    private boolean isNeedInitPartChart = true;
    private boolean isNeedInitY = true;

    public ViewFullChart(Context context) {
        super(context);
    }

    public ViewFullChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewFullChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewFullChart(Context context, Chart chart, Loading loading) {
        super(context);
        this._chart = chart;
        this.loading = loading;
        this.init();
    }

    @Override
    public void init() {
        _oldFullChartBackColor = this.getFullChartBackColor();
        this.setBackgroundColor(_oldFullChartBackColor);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ThemeManager.CHART_CELL_HEIGHT_PX);
        layoutParams.topMargin = ThemeManager.CHART_FULL_TOP_BOTTOM_MARGIN_PX;
        layoutParams.bottomMargin = ThemeManager.CHART_FULL_TOP_BOTTOM_MARGIN_PX;
        this.setLayoutParams(layoutParams);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.initVariables(canvas);
        drawLines(canvas);
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

        _stepX = _viewWidth / (_columnsCount - 1);
        // CONVERT X AND Y's TO CANVAS SIZE
        _axisXForGraph = new float[_columnsCount];
        _axisXForGraph[0] = 0;
        for (int i = 1; i < _columnsCount; i++) {
            _axisXForGraph[i] = _axisXForGraph[i - 1] + _stepX;
        }

        if (isNeedInitPartChart) {
            loading.complete(_axisesYArrays, _hashPaints, _axisXForGraph, _columnsCount, _viewHeight);
            isNeedInitPartChart = false;
        }
    }

    private void initY() {
        if (isNeedInitY) {
            _axisesYArrays = new HashMap<>();
            _hashPaints = new HashMap<>();
            for (Map.Entry<String, Line> entry : _chart.getLines().entrySet()) {
                Line line = entry.getValue();
                int columnsCount = line.getCountDots();

                if (columnsCount > 1) {

                    if (_columnsCount < columnsCount) {
                        _columnsCount = columnsCount;
                    }
                    // INIT PAINT
                    Paint linePaint = AndroidUtilites.getPaint(
                            entry.getValue().getColor(),
                            ThemeManager.CHART_LINE_FULL_WIDTH_PX);
                    _hashPaints.put(entry.getKey(), linePaint);

                    // INIT AXIS Y
                    float[] axisY = new float[columnsCount];
                    for (int i = 0; i < columnsCount; i++) {
                        int currentY = line.getData()[i];
                        if (_maxAxisY < currentY) {
                            _maxAxisY = currentY;
                        }
                        axisY[i] = currentY;
                    }
                    _axisesYArrays.put(entry.getKey(), axisY);
                }
            }
            _stepY = _viewHeight / (_maxAxisY - 1);

            for (Map.Entry<String, float[]> entry: _axisesYArrays.entrySet()) {
                float[] axisY = entry.getValue();
                int count = axisY.length;

                for (int i = 0; i < count; i++) {
                    axisY[i] = _viewHeight - axisY[i] * _stepY;
                }
            }
            isNeedInitY = false;
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
                    if (isRangeTwoPointsAvailable(oldX, oldY, currentX, currentY)) {
                        canvas.drawLine(oldX, oldY, currentX, currentY, paint);
                        oldX = currentX;
                        oldY = currentY;
                    }
                }
            }
        }
    }

    private boolean isRangeTwoPointsAvailable(float xStart, float yStart,
                                              float xEnd, float yEnd) {
        boolean isAvailableForDraw;
        float sub = Math.abs(xEnd - xStart) + Math.abs(yEnd - yStart);
        if (sub >= APPROX_RANGE) {
            isAvailableForDraw = true;
        } else {
            isAvailableForDraw = false;
        }
        return isAvailableForDraw;
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
        if ((int) arg == ObserverManager.KEY_OBSERVER_THEME_UPDATED) {
            int newFullChartBackColor = getFullChartBackColor();

            // FULL CHART CHANGE BACK COLOR
            ValueAnimator fullChartRGBAnim = AndroidUtilites.getArgbAnimator(
                    _oldFullChartBackColor,
                    newFullChartBackColor,
                    animation -> this.setBackgroundColor((int) animation.getAnimatedValue()));
            fullChartRGBAnim.start();
            _oldFullChartBackColor = newFullChartBackColor;
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

    private int getFullChartBackColor() {
        return ThemeManager.getColor(ThemeManager.key_cellSubBackColor);
    }
}
