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
import com.kitzapp.telegram_stats.common.AppConts;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
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

abstract class TAbstractChartBase extends FrameLayout implements TAbstractChartBaseInterface {
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

    protected int _maxAxisXx;
    protected long _maxAxisY;

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

        _paints = getNewPaints(_chart);
        _axisesYOriginalArrays = this.getOriginalAxysesYAndInitMaxs();

        _isFirstDraw = true;
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

    protected void drawPathes(Canvas canvas, HashMap<String, Path> pathHashMap) {
        if (_isFirstDraw) {
            this.drawPathesWithoutAnim(canvas, pathHashMap, _paints);
            _isFirstDraw = false;
        } else {
            this.drawPathesAlphaAnim(canvas, pathHashMap, _paints);
        }
    }

    private void drawPathesAlphaAnim(Canvas canvas, HashMap<String, Path> pathHashMap, HashMap<String, Paint> paintsMap) {
        boolean isActiveChart;
        Path path;  Paint paint;
        int newAlpha, oldAlpha;

        for (Map.Entry<String, Path> entry : pathHashMap.entrySet()) {
            paint = paintsMap.get(entry.getKey());
            path = entry.getValue();
            isActiveChart = this.getChartIsActive(entry.getKey());

            if (paint == null && entry.getValue() == null) {
                continue;
            }

            canvas.drawPath(path, paint);

            oldAlpha = paint.getAlpha();

            boolean isDrawEnded = (isActiveChart && oldAlpha == MAX_VALUE_ALPHA) ||
                    (!isActiveChart && oldAlpha == MIN_VALUE_ALPHA);

            if (isDrawEnded) {
                continue;
            }

            newAlpha = this.getNewAlpha(isActiveChart, oldAlpha);

            paint.setAlpha(newAlpha);
            postInvalidateDelayed(AppConts.DELAY_STEP_ELEMENTS_ANIM);
        }
    }

    protected void drawPathesWithoutAnim(Canvas canvas, HashMap<String, Path> pathHashMap, HashMap<String, Paint> paintsMap) {
        boolean isActiveChart;
        Path path;
        Paint paint;

        for (Map.Entry<String, Path> entry : pathHashMap.entrySet()) {
            paint = paintsMap.get(entry.getKey());
            path = entry.getValue();
            isActiveChart = this.getChartIsActive(entry.getKey());

            if (paint == null && entry.getValue() == null) {
                continue;
            }
            int alpha = isActiveChart ? MAX_VALUE_ALPHA : MIN_VALUE_ALPHA;
            paint.setAlpha(alpha);
            canvas.drawPath(path, paint);
        }
    }

    private int getNewAlpha(boolean isActiveChart, int alpha) {
        int newAlpha = alpha;

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

        return newAlpha;
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

    private HashMap<String, long[]> getOriginalAxysesYAndInitMaxs() {
        HashMap<String, long[]> tempAxyses = new HashMap<>();

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

    protected int getChartHorizPaddingSum() {
        return ThemeManager.MARGIN_32DP_IN_PX;
    }

    protected int getChartHorizPadding() {
        return ThemeManager.MARGIN_16DP_IN_PX;
    }

    @Override
    public void wasChangedIsActiveChart() {
        invalidate();
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
