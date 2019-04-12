package com.kitzapp.telegram_stats.customViews.charts.impl;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.kitzapp.telegram_stats.AndroidApp;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.common.AppConts;
import com.kitzapp.telegram_stats.core.appManagers.ObserverManager;
import com.kitzapp.telegram_stats.core.appManagers.TViewObserver;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
import com.kitzapp.telegram_stats.customViews.simple.TViewRectSelect;
import com.kitzapp.telegram_stats.pojo.chart.Chart;
import com.kitzapp.telegram_stats.pojo.chart.impl.Line;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import static com.kitzapp.telegram_stats.common.AppConts.*;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public abstract class TAbstractChartMiniature extends TPrivateChartBAAse {
    private int MAX_DOTS_FOR_APPROX_CHART_FULL = 1024;
    private final int FLAG_Y_NOT_AVAILABLE = -5;

    private Chart _chart = null;
    private HashMap<String, long[]> _axisesYOriginalArrays = new HashMap<>();
    private HashMap<String, Paint> _paints;

    private float[] _axisXForCanvas = null;
    private HashMap<String, long[]> _axisesYForCanvas = new HashMap<>();
    private HashMap<String, Path> _linesPathes = new HashMap<>();

    private int _maxAxisXx;
    private long _maxAxisY;

    private TViewRectSelect _viewRectSelect;
    private TViewRectSelect.RectListener _rectListener;

    public TAbstractChartMiniature(Context context) {
        super(context);
    }

    public TAbstractChartMiniature(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TAbstractChartMiniature(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TAbstractChartMiniature(Context context, TViewRectSelect.RectListener rectListener) {
        super(context);
        _rectListener = rectListener;
        this.init();
    }

    @Override
    protected void init() {
        super.init();
        if (_rectListener != null) {
            super.init();
            _viewRectSelect = new TViewRectSelect(getContext(), _rectListener);
            this.addView(_viewRectSelect);
        }
    }

    public void loadData(Chart chart) {
        _chart = chart;
        _axisesYOriginalArrays = null;
        _viewWidth = 0;

        this.invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (_chart != null) {
            int canvasHeight = getHeight() - getChartVerticalPadding();
            int canvasWidth = getWidth() - ((getPaddingRightLeft() + 1) << 1);

            boolean isNeedPathAnimation = true;
//                  FIRST LAUNCH INIT
            if (_axisesYOriginalArrays == null) {
                _axisesYOriginalArrays = this.getInitAxysesYAndInitPaints();
                isNeedPathAnimation = false;
            }

            boolean isNeedInitAxises = _viewWidth != canvasWidth;
            if (isNeedInitAxises) {
                _viewHeight = canvasHeight;
                _viewWidth = canvasWidth;
                MAX_DOTS_FOR_APPROX_CHART_FULL = (int)_viewWidth >> 1;

                this.initAxisesForCanvas();

                _linesPathes = this.getLinesPathes(_axisXForCanvas, _axisesYForCanvas);
            }

            this.drawPathes(canvas, _linesPathes, isNeedPathAnimation);
        }
    }

    //    isNeedInitPaints
    protected void initAxisesForCanvas() {

        _axisXForCanvas = this.recalculateAxisX();

        _axisesYForCanvas = getAxisesForCanvas(_axisesYOriginalArrays, _maxAxisY);
    }

    private float[] recalculateAxisX() {
        float[] newAxisX = new float[_maxAxisXx];
        float stepX = (_viewWidth) / (_maxAxisXx - 1);
        // CONVERT X AND Y's TO CANVAS SIZE
        newAxisX = new float[_maxAxisXx];
        newAxisX[0] = getPaddingRightLeft() + 1;
        for (int i = 1; i < _maxAxisXx; i++) {
            newAxisX[i] = newAxisX[i - 1] + stepX;
        }
        return newAxisX;
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

    private HashMap<String, long[]> getAxisesForCanvas(HashMap<String, long[]> originalArray, long maxAxisY) {
        return this.getAxisesForCanvas(originalArray, maxAxisY, 0);
    }

    protected HashMap<String, long[]> getAxisesForCanvas(HashMap<String, long[]> originalArray,
                                                         long maxAxisY,
                                                         long minAxisY) {
        HashMap<String, long[]> tempArray = new HashMap<>();

        if (!originalArray.isEmpty()) {
            float widthInPx = maxAxisY - minAxisY;
            float persent = widthInPx / _viewHeight;
            long[] tempAxisY;
            int countDots;
            long[] axisY;
            float tempValue;
            long convertedY;
            for (Map.Entry<String, long[]> entry : originalArray.entrySet()) {
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

                tempArray.put(entry.getKey(), axisY);
            }
        }

        return tempArray;
    }

    protected HashMap<String, Path> getLinesPathes(float[] axisXForCanvas, HashMap<String, long[]> axisesYForCanvas) {
        HashMap<String, Path> newPathesLines = new HashMap<>();
        if (axisesYForCanvas.isEmpty()) {
            return newPathesLines;
        }

        Line line;
        Path pathLine;
        for (Map.Entry<String, long[]> entry : axisesYForCanvas.entrySet()) {
            line = _chart.getLines().get(entry.getKey());
            if (line == null) {
                continue;
            }

            long[] axisYForCanvas = entry.getValue();
            int columnsCount = axisYForCanvas.length;

            pathLine = new Path();
            float firstX = axisXForCanvas[0];
            long firstY = axisYForCanvas[0];

            pathLine.moveTo(firstX, firstY);
            long currentY;
            float currentX;

            for (int i = 1; i < columnsCount; i++) {
                currentY = axisYForCanvas[i];
                if (currentY > FLAG_Y_NOT_AVAILABLE) {
                    currentX = axisXForCanvas[i];

                    pathLine.lineTo(currentX, currentY);
                }
            }

            newPathesLines.put(entry.getKey(), pathLine);
        }
        return newPathesLines;
    }

    private void drawPathes(Canvas canvas, HashMap<String, Path> pathHashMap, boolean withAnimation) {
        boolean isActiveChart;
        Path path;
        Paint paint;
        for (Map.Entry<String, Path> entry : pathHashMap.entrySet()) {
            paint = _paints.get(entry.getKey());
            path = entry.getValue();
            isActiveChart = getChartIsActive(entry.getKey());

            if (paint == null && entry.getValue() == null) {
                continue;
            }

            if (withAnimation) {
                canvas.drawPath(path, paint);

                int newAlpha = paint.getAlpha();
                if ((isActiveChart && newAlpha == MAX_VALUE_ALPHA) || (!isActiveChart && newAlpha == MIN_VALUE_ALPHA)) {
                    continue;
                }


                if (isActiveChart) {
                    if (newAlpha + STEP_ALPHA_FOR_ANIM <= MAX_VALUE_ALPHA) {
                        newAlpha += STEP_ALPHA_FOR_ANIM;
                    } else {
                        newAlpha = MAX_VALUE_ALPHA;
                    }
                } else {
                    if (newAlpha - STEP_ALPHA_FOR_ANIM >= MIN_VALUE_ALPHA) {
                        newAlpha -= STEP_ALPHA_FOR_ANIM;
                    } else {
                        newAlpha = MIN_VALUE_ALPHA;
                    }
                }
                paint.setAlpha(newAlpha);

                postInvalidateDelayed(AppConts.DELAY_STEP_ELEMENTS_ANIM);
            } else {
                int alpha = isActiveChart? MAX_VALUE_ALPHA : MIN_VALUE_ALPHA;
                paint.setAlpha(alpha);
                canvas.drawPath(path, paint);
            }
        }
    }

    public void setMiniatureIsLocked(boolean isLocked) {
        _viewRectSelect.setMiniatureIsLocked(isLocked);
    }

    @Override
    protected int getViewHeightForLayout() {
        return ThemeManager.CELL_HEIGHT_48DP_IN_PX;
    }

    @Override
    protected int getLinePaintWidth() {
        return ThemeManager.CHART_LINE_IN_MINIATURE_WIDTH_PX;
    }

    private long[] getApproximateArray(long[] arrayY, int maxCountDots) {
        int countDotsForApprox = _axisXForCanvas.length;
        if (countDotsForApprox > maxCountDots) {
            int pointsCount = _axisXForCanvas.length - 1; // -1 for save last point
            int currentApproxRange = 0;
            float oldX, currentX;
            long oldY, currentY;
            while (countDotsForApprox > maxCountDots) {
                currentApproxRange += 1;
                oldX = _axisXForCanvas[0];
                oldY = arrayY[0];
                boolean rangePointsIsAvailable;

                for (int i = 1; i < pointsCount; i++) {
                    currentY = arrayY[i];
                    if (currentY >= 0) {
                        currentX = _axisXForCanvas[i];
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

    @Override
    public void wasChangedActiveChart() {
        this.invalidate();
    }

    @Override
    protected int getChartVerticalPadding() {
        return ThemeManager.CHART_MINIATURE_VERTICAL_PADDING_SUM_PX;
    }

    @Override
    protected int getChartHalfVerticalPadding() {
        return ThemeManager.CHART_MINIATURE_VERTICAL_PADDING_HALF_PX;
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

    private int getMaxDotsForApproxChart() {
        return MAX_DOTS_FOR_APPROX_CHART_FULL;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
        layoutParams.height -= (ThemeManager.CHART_FULL_TOP_BOTTOM_MARGIN_PX << 1);
        layoutParams.topMargin = ThemeManager.CHART_FULL_TOP_BOTTOM_MARGIN_PX;
        layoutParams.bottomMargin = ThemeManager.CHART_FULL_TOP_BOTTOM_MARGIN_PX;
    }
}
