package com.kitzapp.telegram_stats.customViews.charts.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
import com.kitzapp.telegram_stats.core.appManagers.animation.AnimationManager;
import com.kitzapp.telegram_stats.core.appManagers.animation.TAlphaAnim;
import com.kitzapp.telegram_stats.core.appManagers.animation.TScaleYAnim;
import com.kitzapp.telegram_stats.pojo.chart.Chart;
import com.kitzapp.telegram_stats.pojo.chart.impl.Line;

import java.util.HashMap;
import java.util.Map;

import static com.kitzapp.telegram_stats.common.AppConts.MAX_VALUE_ALPHA;
import static com.kitzapp.telegram_stats.common.AppConts.MIN_VALUE_ALPHA;

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
    protected volatile HashMap<String, Path> _linesPathes = new HashMap<>();

    protected HashMap<String, long[]> _axisesYOriginalArrays = new HashMap<>();
    protected float[] _axisXForCanvas = null;

    protected boolean _isFirstDraw = true;

    protected float _viewWidth;
    protected float _calculatingViewHeight;
    protected float _calculatingViewWidth;

    protected int _constMaxAxisXx;
    protected long _constMaxAxisY;

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
        TScaleYAnim scaleYAnim = new TScaleYAnim(newScaleY -> {
            _scaleY = newScaleY;
            this.needRecalculatePathYScale(_scaleY);
        });

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
    protected abstract void needRecalculatePathYScale(float newYScale);

    protected HashMap<String, Path> getLinesPathes(float[] axisXForCanvas,
                                                   HashMap<String, long[]> axisesYForCanvas) {
        return this.getLinesPathesArea(axisXForCanvas, axisesYForCanvas, 0, axisXForCanvas.length);
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
