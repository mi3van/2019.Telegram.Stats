package com.kitzapp.telegram_stats.customViews.charts.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
import com.kitzapp.telegram_stats.customViews.simple.TViewRectSelect;
import com.kitzapp.telegram_stats.pojo.chart.Chart;
import com.kitzapp.telegram_stats.pojo.chart.impl.Line;

import java.util.HashMap;
import java.util.Map;

public abstract class TChartMiniatureView extends TAbstractChartBase implements TViewRectSelect.RectListener {
    private int MAX_DOTS_FOR_APPROX_CHART_FULL = 1024;
    private final int FLAG_Y_NOT_AVAILABLE = -5;

    private HashMap<String, long[]> _axisesYOriginalArrays = new HashMap<>();

    private float[] _axisXForCanvas = null;
    private HashMap<String, long[]> _axisesYForCanvas = new HashMap<>();

    private TViewRectSelect _viewRectSelect;
    private TChartMiniatureInterface.Listener _miniatureListener;

    protected HashMap<String, Path> _linesPathesForMiniature = new HashMap<>();

    public TChartMiniatureView(Context context) {
        super(context);
    }

    public TChartMiniatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TChartMiniatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMiniatureListener(TChartMiniatureInterface.Listener miniatureListener) {
        _miniatureListener = miniatureListener;
    }

    @Override
    protected void init() {
        super.init();
        _viewRectSelect = new TViewRectSelect(getContext());
        _viewRectSelect.setRectDelegate(this);
        this.addView(_viewRectSelect);
    }

    public void loadData(Chart chart) {
        super.loadData(chart);

        _axisesYOriginalArrays = this.getOriginalAxysesYAndInitMaxs();

        _isDrawing = true;
    }

    @Override
    public void onSurfaceDraw(Canvas canvas) {
        if (_isDrawing) {
            Log.d("DRAW", String.format("MINIATURE draw: %d", _chart.getCountDots()));

            int width = getWidth();// - ((getPaddingRightLeft()) << 1);
            boolean isNeedInitAxises = _viewWidth != width;
            if (isNeedInitAxises) {
                _viewWidth = width;
                _viewHeight = getHeight() - getChartVerticalPadding();
                this.initAxisesForCanvas();
            }
            drawPathes(canvas, _linesPathes, _isFirstDraw);
            _isFirstDraw = false;
        }
    }

    //    isNeedInitPaints
    protected void initAxisesForCanvas() {
        MAX_DOTS_FOR_APPROX_CHART_FULL = (int)_viewWidth >> 1;

        _axisXForCanvas = this.recalculateAxisX(_maxAxisXx);

        _axisesYForCanvas = this.getAxisesForCanvas(_axisesYOriginalArrays, _maxAxisY);

        _linesPathes = this.getLinesPathes(_axisXForCanvas, _axisesYForCanvas);
        _linesPathesForMiniature = this.getLinesPathes(_axisXForCanvas, _axisesYForCanvas);
        if (_miniatureListener != null) {
            _miniatureListener.onLinesPathesWasChanged(_linesPathesForMiniature, _maxAxisXx);
        }
    }

    private float[] recalculateAxisX(int maxAxisXx) {
        float[] newAxisX = new float[maxAxisXx];
        float stepX = (_viewWidth) / (maxAxisXx - 1);
        newAxisX[0] = getPaddingRightLeft();
        for (int i = 1; i < maxAxisXx; i++) {
            newAxisX[i] = newAxisX[i - 1] + stepX;
        }
        return newAxisX;
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

    private HashMap<String, long[]> getAxisesForCanvas(HashMap<String, long[]> originalArray, long maxAxisY) {
        return this.getAxisesForCanvas(originalArray, maxAxisY, 0);
    }

    private HashMap<String, long[]> getAxisesForCanvas(HashMap<String, long[]> originalArray,
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
    protected int getChartVerticalPadding() {
        return ThemeManager.CHART_MINIATURE_VERTICAL_PADDING_SUM_PX;
    }

    @Override
    protected int getChartHalfVerticalPadding() {
        return ThemeManager.CHART_MINIATURE_VERTICAL_PADDING_HALF_PX;
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

    @Override
    public void onRectCursorsWasChanged(float leftCursor, float rightCursor) {
        if (_miniatureListener != null) {
            _miniatureListener.onRectCursorsWasChanged(leftCursor, rightCursor);
        }
    }
}
