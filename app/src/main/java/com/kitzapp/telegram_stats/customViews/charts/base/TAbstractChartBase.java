package com.kitzapp.telegram_stats.customViews.charts.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.common.MyLongPair;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
import com.kitzapp.telegram_stats.core.appManagers.animation.AnimationManager;
import com.kitzapp.telegram_stats.core.appManagers.animation.TAlphaAnim;
import com.kitzapp.telegram_stats.core.appManagers.animation.TScaleYAnim;
import com.kitzapp.telegram_stats.pojo.chart.Chart;
import com.kitzapp.telegram_stats.pojo.chart.impl.Line;

import java.util.HashMap;
import java.util.Map;

import static com.kitzapp.telegram_stats.common.AppConts.*;

/**
 * Created by Ivan Kuzmin on 25.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

abstract class TAbstractChartBase extends FrameLayout implements TAbstractChartBaseInterface,
                                                                        AnimationManager.ListenerForView {
    protected final int FLAG_Y_NOT_AVAILABLE = -5;

    protected Chart _chart = null;
    private HashMap<String, Paint> _paints = new HashMap<>();
    protected HashMap<String, Path> _linesPathes = new HashMap<>();

    protected HashMap<String, long[]> _axisesYOriginalArrays = new HashMap<>();
    protected float[] _axisXForCanvas = null;

    protected boolean _isFirstDraw = true;

    protected float _viewWidth;
    protected float _calculatingViewHeight;
    protected float _calculatingViewWidth;

    protected int _constMaxAxisXx;
    protected long _constMaxAxisY;
    protected long _tempMaxAxisY;
    protected long _tempMinimumAxisY;

    protected AnimationManager _animationManager = null;
    protected float _scaleY = 1f;

    public TAbstractChartBase(Context context) {
        super(context);
        this.init();
    }

    public TAbstractChartBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TAbstractChartBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    protected void init() {
        setWillNotDraw(false);
    }

    public void loadData(Chart chart) {
        if (chart == null) {
            return;
        }
        this._chart = chart;

        _paints = this.getNewPaints(_chart);
        this.initAnimationManager(_chart);

        _isFirstDraw = true;
    }

    private void initAnimationManager(Chart chart) {
        HashMap<String, TAlphaAnim> alphaAnimsMap = new HashMap<>();

        for (Map.Entry<String, Line> entry: chart.getLines().entrySet()) {
            TAlphaAnim alphaAnim = new TAlphaAnim(entry.getKey(), this::updatePaintAlpha);
            alphaAnimsMap.put(entry.getKey(), alphaAnim);
        }
        TScaleYAnim scaleYAnim = new TScaleYAnim(this::needRecalculatePathYScale);

        _animationManager = new AnimationManager(alphaAnimsMap, scaleYAnim, this);
    }

    protected void updateSizeValues() {
        _calculatingViewHeight = getHeight() - getChartVerticalPadding();
        _calculatingViewWidth = getWidth() - getChartHorizPaddingSum();
        _viewWidth = getWidth();
    }

    protected abstract int getLinePaintWidth();
    protected abstract int getViewHeightForLayout();

    protected abstract int getChartVerticalPadding();
    protected abstract int getChartHalfVerticalPadding();

    protected abstract int getLeftInArray();
    protected abstract int getRightInArray();
    protected abstract HashMap<String, long[]> getCalculatedYArrays();
    protected abstract int getPxForMatrix();
    protected abstract float getPyForMatrix();
    protected abstract float getLeftCursor();
    protected abstract float getRightCursor();

    protected void needRecalculatePathYScale(float newYScale) {
        _scaleY = newYScale;
    }

    protected void updateLinesPathes(float[] axisXForCanvas, HashMap<String, long[]> axisesYForCanvas) {
        _linesPathes = this.getLinesPathesArea(axisXForCanvas, axisesYForCanvas, 0, _constMaxAxisXx);
    }

    protected HashMap<String, Path> getLinesPathesArea(float[] axisXForCanvas,
                                                       HashMap<String, long[]> axisesYForCanvas,
                                                       int leftCursorArray,
                                                       int rightCursorArray) {
        HashMap<String, Path> newPathesLines = new HashMap<>();
        if (axisesYForCanvas.isEmpty()) {
            return newPathesLines;
        }
        int columnsCount = axisXForCanvas.length;

        int myLeftIndex = leftCursorArray;
        if (myLeftIndex < 0) {
            myLeftIndex = 0;
        }
        int myRightIndex = rightCursorArray;
        if (myRightIndex > columnsCount) {
            myRightIndex = columnsCount;
        }
        Line line; Path pathLine;
        for (Map.Entry<String, long[]> entry : axisesYForCanvas.entrySet()) {
            line = _chart.getLines().get(entry.getKey());
            if (line == null) {
                continue;
            }

            long[] axisYForCanvas = entry.getValue();

            pathLine = new Path();
            long currentY;
            float currentX;

            for (int i = myLeftIndex; i < myRightIndex; i++) {
                currentX = axisXForCanvas[i];
                currentY = axisYForCanvas[i];
                if (i == myLeftIndex) {
                    pathLine.moveTo(currentX, currentY);
                }
                if (currentY > FLAG_Y_NOT_AVAILABLE) {
                    pathLine.lineTo(currentX, currentY);
                }
            }

            newPathesLines.put(entry.getKey(), pathLine);
        }
        return newPathesLines;
    }

    protected void drawPathes(Canvas canvas, HashMap<String, Path> pathHashMap) {
        this.drawPathesCheckAnim(canvas, pathHashMap, _paints, _isFirstDraw);
        if (_isFirstDraw) {
            _isFirstDraw = false;
        }
    }

    private void drawPathesCheckAnim(Canvas canvas,
                                     HashMap<String, Path> pathHashMap,
                                     HashMap<String, Paint> paintsMap,
                                     boolean isFirstDraw) {
        Path path;  Paint paint;

        for (Map.Entry<String, Path> entry : pathHashMap.entrySet()) {
            paint = paintsMap.get(entry.getKey());
            path = entry.getValue();

            if (paint == null || entry.getValue() == null) {
                continue;
            }

            if (isFirstDraw) {
                boolean isActiveChart = this.getChartIsActive(entry.getKey());
                int alpha = isActiveChart ? MAX_VALUE_ALPHA : MIN_VALUE_ALPHA;
                paint.setAlpha(alpha);
            }
            canvas.drawPath(path, paint);
        }
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

    private HashMap<String, Paint> getNewPaints(Chart chart) {
        HashMap<String, Paint> tempPaints = new HashMap<>();

        if (chart == null) {
            return tempPaints;
        }

        Line line;
        for (Map.Entry<String, Line> entry : chart.getLines().entrySet()) {
            line = entry.getValue();
            // INIT PAINT
            Paint linePaint = AndroidUtilites.getPaint(line.getColor(), this.getLinePaintWidth());
            tempPaints.put(entry.getKey(), linePaint);
        }
        return tempPaints;
    }

    private void updatePaintAlpha(String key, int alpha) {
        if (_paints.isEmpty()) {
            return;
        }
        Paint paint = _paints.get(key);

        if (paint != null) {
            paint.setAlpha(alpha);
        }
    }

    protected long[] getApproximateArray(long[] arrayY, int maxCountDots) {
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

    protected int getChartHorizPaddingSum() {
        return ThemeManager.MARGIN_32DP_IN_PX;
    }

    protected int getChartHorizPadding() {
        return ThemeManager.MARGIN_16DP_IN_PX;
    }

    @Override
    public void wasChangedIsActiveChart() {
        if (_chart == null || _animationManager == null) {
            return;
        }

        for (Map.Entry<String, Line> entry: _chart.getLines().entrySet()) {
            boolean isLineActive = entry.getValue().getIsActive();
            int newAlpha = isLineActive ? MAX_VALUE_ALPHA : MIN_VALUE_ALPHA;
            _animationManager.setNewAlpha(entry.getKey(), newAlpha);
        }

        this.scaleYAnimationStart();
    }

    protected void initPathsForDraw() {
        this.calculateMaxAndMin(_axisesYOriginalArrays, getLeftInArray(), getRightInArray());

        updatePathsForMatrix(getLeftInArray(), getRightInArray(), getCalculatedYArrays());

        this.configureMatrixAndApply(getLeftCursor(), getRightCursor(), _scaleY, _linesPathes);
    }

    protected void scaleYAnimationStart() {
        float newScaleY = this.getNewScaleAndUpdateMaxAndMin(_axisesYOriginalArrays, getLeftInArray(), getRightInArray());

        _animationManager.setNewScaleY(_scaleY, newScaleY);
    }

    protected void updatePathsForMatrix( int _leftInArray, int _rightInArray,
                                         HashMap<String, long[]> _axisesYCalculated) {
        int countPoints = _rightInArray - _leftInArray;
        HashMap<String, Path> tempMap;

        float pointsWidth = _calculatingViewWidth / countPoints;
        int addingCountDots = Math.round(getChartHorizPadding() / pointsWidth) + 2;

        tempMap = getLinesPathesArea(_axisXForCanvas, _axisesYCalculated,
                _leftInArray - addingCountDots,
                _rightInArray + addingCountDots);

        _linesPathes = tempMap;
    }

    private void configureMatrixAndApply(float leftCursor, float rightCursor, float scaleY, HashMap<String, Path> pathHashMap) {
        Matrix matrixTranslate = new Matrix();
        Matrix matrixScale = new Matrix();

        float needScaleX = 1f / (rightCursor - leftCursor);
        float translateX = leftCursor * _calculatingViewWidth;
        float translateY = getChartHalfVerticalPadding();

        matrixScale.setScale(needScaleX, scaleY, getPxForMatrix(), getPyForMatrix());
        matrixTranslate.setTranslate(-translateX, translateY);

        for (Map.Entry<String, Path> entry: pathHashMap.entrySet()) {
            Path path = entry.getValue();
            if (path == null) {
                continue;
            }

            path.transform(matrixTranslate);
            path.transform(matrixScale);
        }
    }

    private float getNewScaleAndUpdateMaxAndMin(HashMap<String, long[]> hashMap, int leftInArray, int rightInArray) {
        this.calculateMaxAndMin(hashMap, leftInArray, rightInArray);

        long difference = _tempMaxAxisY - 0;//_tempMinimumAxisY;
        float scaleY = (float) _constMaxAxisY / difference;
        return scaleY;
    }

    protected void calculateMaxAndMin(HashMap<String, long[]> hashMap, int leftInArray, int rightInArray) {
        MyLongPair maxAndMinInPoint = this.getMaxAndMinInHashMap(hashMap, leftInArray, rightInArray);

        long tempMaxAxisY = maxAndMinInPoint.getMax();
        if (tempMaxAxisY == INTEGER_MIN_VALUE) {
            return;
        }
        _tempMaxAxisY = tempMaxAxisY;
        _tempMinimumAxisY = maxAndMinInPoint.getMin();
    }

    protected MyLongPair getMaxAndMinInHashMap(HashMap<String, long[]> hashMap, int leftInArray, int rightInArray) {
        long max = INTEGER_MIN_VALUE;   long min = INTEGER_MAX_VALUE;
        boolean isActiveChart;
        long[] valuesArray;

        if (leftInArray < 0) {
            leftInArray = 0;
        }
        if (rightInArray > _constMaxAxisXx) {
            rightInArray = _constMaxAxisXx;
        }
        for (Map.Entry<String, long[]> entry: hashMap.entrySet()) {
            isActiveChart = getChartIsActive(entry.getKey());
            if (!isActiveChart) {
                continue;
            }
            valuesArray = hashMap.get(entry.getKey());
            if (valuesArray == null) {
                continue;
            }
            for (int i = leftInArray; i < rightInArray; i++) {
                long value = valuesArray[i];
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
    public void animNeedInvalidateView() {
        postInvalidateOnAnimation();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = this.getViewHeightForLayout();
        this.setLayoutParams(layoutParams);

        int paddingPx = this.getChartHorizPadding();
        setPadding(paddingPx, 0, paddingPx, 0);
    }
}
