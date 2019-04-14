package com.kitzapp.telegram_stats.customViews.charts.base;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
import com.kitzapp.telegram_stats.customViews.simple.TViewRectSelect;

import java.util.HashMap;
import java.util.Map;

public abstract class TChartMiniatureView extends TAbstractChartBase implements TChartMiniatureVInterface,
                                                                                        TViewRectSelect.RectListener {
    private int MAX_DOTS_FOR_APPROX_CHART_FULL = 1024;

    private HashMap<String, long[]> _axisesYForCanvas = new HashMap<>();

    private TViewRectSelect _viewRectSelect;
    private TChartMiniatureVInterface.Listener _miniatureListener;

    public TChartMiniatureView(Context context) {
        super(context);
    }

    public TChartMiniatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TChartMiniatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMiniatureListener(TChartMiniatureVInterface.Listener miniatureListener) {
        _miniatureListener = miniatureListener;
    }

    @Override
    protected void init() {
        super.init();
        _viewRectSelect = new TViewRectSelect(getContext());
        _viewRectSelect.setRectDelegate(this);
        this.addView(_viewRectSelect);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (_isFirstDraw || _viewWidth != getWidth()) {
            this.initAxisesForCanvas();
        }

        drawPathes(canvas, _linesPathes);
    }

    //    isNeedInitPaints
    private void initAxisesForCanvas() {
        this.updateSizeValues();

        MAX_DOTS_FOR_APPROX_CHART_FULL = (int) _calculatingViewWidth >> 1;

        _axisXForCanvas = this.recalculateAxisX(_maxAxisXx);
        _axisesYForCanvas = this.getAxisesForCanvas(_axisesYOriginalArrays, _maxAxisY);

        _linesPathes = this.getLinesPathes(_axisXForCanvas, _axisesYForCanvas);

        if (_miniatureListener != null) {
            _miniatureListener.onDataWasRecalculated(_axisXForCanvas);
        }
    }

    private final int paintWidthForCalculateX = getLinePaintWidth();
    private final int paintHalfWidthForCalculateX = paintWidthForCalculateX >> 1;
    private float[] recalculateAxisX(int maxAxisXx) {
        float[] newAxisX = new float[maxAxisXx];
        float stepX = (_calculatingViewWidth - paintWidthForCalculateX) / (maxAxisXx - 1);
        newAxisX[0] = getChartHorizPadding() + paintHalfWidthForCalculateX;
        for (int i = 1; i < maxAxisXx; i++) {
            newAxisX[i] = newAxisX[i - 1] + stepX;
        }
        return newAxisX;
    }

    private HashMap<String, long[]> getAxisesForCanvas(HashMap<String, long[]> originalArray, long maxAxisY) {
        return this.getAxisesForCanvas(originalArray, maxAxisY, 0);
    }

    private HashMap<String, long[]> getAxisesForCanvas(HashMap<String, long[]> originalArray,
                                                         long maxAxisY,
                                                         long minAxisY) {
        HashMap<String, long[]> tempArray = new HashMap<>();

        if (!originalArray.isEmpty()) {
            float segment = maxAxisY - minAxisY;
            float persent = segment / _calculatingViewHeight;
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
                    convertedY = (long) (_calculatingViewHeight - tempValue + getChartHalfVerticalPadding());
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
    public void onRectCursorsWasChanged(float leftCursor, float rightCursor) {
        if (_miniatureListener != null) {
            _miniatureListener.onRectCursorsWasChanged(leftCursor, rightCursor);
        }
    }

    @Override
    public void setMiniatureIsLocked(boolean isLocked) {
        _viewRectSelect.setMiniatureIsLocked(isLocked);
    }

    private int getMaxDotsForApproxChart() {
        return MAX_DOTS_FOR_APPROX_CHART_FULL;
    }

    @Override
    protected int getViewHeightForLayout() {
        return ThemeManager.CELL_HEIGHT_48DP_IN_PX;
    }

    @Override
    protected int getLinePaintWidth() {
        return ThemeManager.CHART_LINE_IN_MINIATURE_WIDTH_PX;
    }

    @Override
    protected int getChartVerticalPadding() {
        return ThemeManager.CHART_MINIATURE_VERTICAL_PADDING_SUM_PX;
    }

    @Override
    protected int getChartHalfVerticalPadding() {
        return ThemeManager.CHART_MINIATURE_VERTICAL_PADDING_HALF_PX;
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
