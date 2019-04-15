package com.kitzapp.telegram_stats.customViews.charts.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.kitzapp.telegram_stats.AndroidApp;
import com.kitzapp.telegram_stats.R;
import com.kitzapp.telegram_stats.common.MyLongPair;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
import com.kitzapp.telegram_stats.core.appManagers.motions.MotionManagerForBigChart;
import com.kitzapp.telegram_stats.customViews.popup.TCellDescriptionTexts;
import com.kitzapp.telegram_stats.customViews.simple.TColorfulTextView;
import com.kitzapp.telegram_stats.customViews.simple.TDelimiterLine;
import com.kitzapp.telegram_stats.customViews.simple.TViewChartInfoVert;
import com.kitzapp.telegram_stats.pojo.chart.Chart;
import com.kitzapp.telegram_stats.pojo.chart.impl.Line;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.kitzapp.telegram_stats.common.AppConts.INTEGER_MAX_VALUE;
import static com.kitzapp.telegram_stats.common.AppConts.INTEGER_MIN_VALUE;

public abstract class TChartBigView extends TAbstractChartBase implements TChartBigViewInterface,
                                                                            TChartMiniatureVInterface.Listener,
                                                                            MotionManagerForBigChart.OnMyTouchListener {
    private TViewChartInfoVert _tViewChartInfoVert;
    private TDelimiterLine _verticalDelimiter;

    private float _leftCursor;
    private float _rightCursor;

    private int _leftInArray;
    private int _rightInArray;

    private MotionManagerForBigChart _motionManagerBig;
    private TChartBigViewInterface.Listener _chartBigListener;

    private Matrix _matrix = new Matrix();


//    private TViewContainerCircleViews _containerCircleViewsPopup;
//    private int _verticalDelimiterHeightForPopup;
//    private int _xoffForPopup;
//    private int _oldIndexShowedPopup;
//    private boolean _isCoeffXForPopupNeedCalculate = true;
//    private float _coeffXForPopup = 0;

    public TChartBigView(Context context) {
        super(context);
    }

    public TChartBigView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TChartBigView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TChartBigView(Context context, TChartMiniatureView chartMiniature) {
        super(context);
        chartMiniature.setMiniatureListener(this);
    }

    public void setDelegate(TChartBigViewInterface.Listener chartBigListener) {
        _chartBigListener = chartBigListener;
    }

    @Override
    protected void init() {
        super.init();

        _tViewChartInfoVert = new TViewChartInfoVert(getContext());
        addView(_tViewChartInfoVert);

        this.initPopup();
    }

    @Override
    public void loadData(Chart chart) {
        if (chart == null) {
            return;
        }
        super.loadData(chart);

//        _matrixAnimMath.reset();
        _matrix.reset();

        if (_chartBigListener != null) {
            long[] axisX = _chart.getAxisX().getData();
            _chartBigListener.onDatesWasChanged(axisX);
        }
    }

    private boolean isOriginalArrayWasFlipped = false;
    @Override
    public void onDataWasRecalculated(float[] axisXForCanvas) {

        this.updateSizeValues();

        if (!isOriginalArrayWasFlipped) {
            this.calcArrayForViewFlipVert(_axisesYOriginalArrays);
            isOriginalArrayWasFlipped = true;
        }
        _axisXForCanvas = axisXForCanvas;
        _linesPathes = getLinesPathes(_axisXForCanvas, _axisesYOriginalArrays);

        _isFirstDraw = true;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (_linesPathes.isEmpty()) {
            return;
        }
        drawPathes(canvas, _linesPathes);
    }

    @Override
    public void onXTouchWasDetected(float newX) {
//        if (_partAxisXForGraph != null) {
//            int tempIndex = -1;
//            if (_isCoeffXForPopupNeedCalculate) {
//                _isCoeffXForPopupNeedCalculate = false;
//                _coeffXForPopup = (_partAxisXForGraph[1] - _partAxisXForGraph[0]) / 2;
//            }
//            for (int i = 0; i < _partAxisXForGraph.length; i++) {
//                float currentPoint = _partAxisXForGraph[i] - _coeffXForPopup;
//                float nextPoint = _partAxisXForGraph[i] + _coeffXForPopup;
//                if (newX > currentPoint && newX <= nextPoint) {
//                    tempIndex = i;
//                    break;
//                }
//            }
//            if (tempIndex >=0 && tempIndex != _oldIndexShowedPopup) {
//                _oldIndexShowedPopup = tempIndex;
//                this.drawPopupViews(_oldIndexShowedPopup);
//            }
//        }
    }

    @Override
    public void onRectCursorsWasChanged(float leftCursor, float rightCursor) {
        _leftCursor = leftCursor;_rightCursor = rightCursor;

        _leftInArray = (int) (_maxAxisXx * leftCursor);
        _rightInArray = (int) (_maxAxisXx * rightCursor);

//        _isCoeffXForPopupNeedCalculate = true;
//
        int countPoints = _rightInArray - _leftInArray;

        if (countPoints < 2) {
            return;
        }

        this.hidePopupViews();

        if (_chartBigListener != null) {
            _chartBigListener.onDatesChangeSection(_leftInArray, _rightInArray);
        }

        // Get new part arrays for draw Y
//        _partAxisesY = getPartOfFullHashAxisY(_leftInArray, _rightInArray);
//         Get new part arrays for draw X
//        _partAxisXForGraph = getPartOfFullAxisX(_leftInArray, _rightInArray);
//
//        float leftInPx = leftCursor * _calculatingViewWidth;
//        float rightInPx = rightCursor * _calculatingViewWidth;
//        _partAxisXForGraph = getAxisXCalculated(leftInPx, rightInPx, _partAxisXForGraph);

//        this.updateMaxAndMin();

        this.configureMatrix(leftCursor, rightCursor);

        this.invalidate();
    }

    private void configureMatrix(float leftCursor, float rightCursor) {
        float needScaleX = (rightCursor - leftCursor);

//        _matrixAnimMath.setScaleXNeed(needScaleX);
    }

    private void applyMatrixForPathes(HashMap<String, Path> pathHashMap) {
        for (Map.Entry<String, Path> entry: pathHashMap.entrySet()) {
            Path path = entry.getValue();
            if (path == null) {
                continue;
            }
            path.transform(_matrix);
        }
    }

//    private float[] getAxisXCalculated(float leftInPx, float rightInPx, float[] originalAxisX) {
//        int lengthX = originalAxisX.length;
//        float[] newAxis = new float[lengthX];
//
//        float widthInPx = rightInPx - leftInPx;
//        float persent = widthInPx / _calculatingViewWidth;
//        float tempValueX;
//        for (int i = 0; i < lengthX; i++) {
//            tempValueX = originalAxisX[i] - leftInPx;
//            tempValueX /= persent;
//            newAxis[i] = tempValueX;
//        }
//
//        return newAxis;
//    }

//    private void updateMaxAndMin() {
//
//        MyLongPair maxAndMinInPoint = this.getMaxAndMinInHashMap(_partAxisesY);
//
//        long _tempMaxAxisY = maxAndMinInPoint.getMax();
//        if (_tempMaxAxisY == INTEGER_MIN_VALUE) {
//            return;
//        }
//        long _tempMinAxisY = maxAndMinInPoint.getMin();
//
//        _tViewChartInfoVert.setNewDates(_tempMaxAxisY, _tempMinAxisY);
//
//        _partAxisesY = getAxisesForCanvas(_partAxisesY, _tempMaxAxisY, _tempMinAxisY);
//    }

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

    private void calcArrayForViewFlipVert(HashMap<String, long[]> axisesYOriginalArrays) {
        if (axisesYOriginalArrays.isEmpty()) {
            return;
        }

        float coeff = _maxAxisY / _calculatingViewHeight;

        for (Map.Entry<String, long[]> entry : axisesYOriginalArrays.entrySet()) {
            long[] array = entry.getValue();
            float tempValue;
            for (int i = 0; i < array.length; i++) {
                tempValue = array[i] / coeff;
                array[i] = (long) (_calculatingViewHeight - tempValue + getChartHalfVerticalPadding());
            }
            entry.setValue(array);
        }
    }

    @Override
    public void onMiniatureViewIsLocked(boolean isLocked) {
        if (_chartBigListener != null) {
            _chartBigListener.onMiniatureViewIsLocked(isLocked);
        }
    }

    @Override
    protected int getViewHeightForLayout() {
        return ThemeManager.CHART_PART_HEIGHT_PX;
    }

    @Override
    protected int getLinePaintWidth() {
        return ThemeManager.CHART_LINE_IN_BIG_WIDTH_PX;
    }

    private void initPopup() {
//        SETUP VERTICAL DELIMITER
        _verticalDelimiter = new TDelimiterLine(getContext());
        _verticalDelimiter.getLayoutParams().height = LayoutParams.MATCH_PARENT;
        _verticalDelimiter.getLayoutParams().width = ThemeManager.CHART_DELIMITER_FATNESS_PX;
        _verticalDelimiter.setVisibility(INVISIBLE);
        addView(_verticalDelimiter);

//        _oldIndexShowedPopup = -1;
//
//        _containerCircleViewsPopup = new TViewContainerCircleViews(getContext());
//        addView(_containerCircleViewsPopup);
//
//        for (Map.Entry<String, Line> entry : _chart.getLines().entrySet()) {
//            int color = entry.getValue().getColor();
//            _containerCircleViewsPopup.initNewCircle(color, entry.getKey());
//        }
    }

    private final String POPUP_DATE_FORMAT = "EEE, d MMM yyyy";
    @SuppressLint("SimpleDateFormat")
    private void drawPopupViews(int indexShowedPart) {

//        float currentX = _partAxisXForGraph[indexShowedPart];
//        _verticalDelimiter.setX(currentX);
        _verticalDelimiter.setVisibility(VISIBLE);

        //todo logic in popup
        PopupWindow popupWindow = AndroidApp.popupWindow;
        View popupView = popupWindow.getContentView();
        TColorfulTextView titleTextPopup = popupView.findViewById(R.id.titleTextPopup);
        titleTextPopup.setTextSizeDP(ThemeManager.TEXT_SMALL_SIZE_DP);
        titleTextPopup.setTypeface(ThemeManager.rBoldTypeface);

        int globalIndexArray;
        if (_leftInArray != 0) {
            globalIndexArray = indexShowedPart + _leftInArray - 1;
        } else {
            globalIndexArray = indexShowedPart + _leftInArray;
        }
        if (globalIndexArray < 0) {
            globalIndexArray = 0;
        }

        long date = _chart.getAxisX().getData()[globalIndexArray];
        SimpleDateFormat formatter = new SimpleDateFormat(POPUP_DATE_FORMAT);
        String dateString = formatter.format(new Date(date));
        titleTextPopup.setText(dateString);

        TCellDescriptionTexts cellForPopup;
        String title; int color; String value;
        Line line;    boolean isActiveChart;

        LinearLayout container = popupView.findViewById(R.id.containerPopup);
        container.removeAllViews();
        LinearLayout.LayoutParams layoutParamsText = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsText.weight = 1f;
        layoutParamsText.setMargins(0, 0, ThemeManager.MARGIN_8DP_IN_PX, 0);
        LinearLayout.LayoutParams layoutParamsDescription = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (Map.Entry<String, Line> entry : _chart.getLines().entrySet()) {
            String key = entry.getKey();
            line = _chart.getLines().get(key);
            isActiveChart = line.getIsActive();

            title = line.getName();
            color = line.getColor();
            value = String.valueOf(line.getData()[globalIndexArray]);
            cellForPopup = new TCellDescriptionTexts(getContext(), title, value, color, layoutParamsText, layoutParamsDescription);
            container.addView(cellForPopup);

//            long y = _partAxisesY.get(key)[indexShowedPart];
//
//            boolean isVisibleCircle = isActiveChart;
//            _containerCircleViewsPopup.setNewPositionAndAnimate(key, currentX, y, isVisibleCircle);
        }

//        if (_verticalDelimiterHeightForPopup == 0) {
//            _verticalDelimiterHeightForPopup = _verticalDelimiter.getHeight();
//            _xoffForPopup = -_verticalDelimiterHeightForPopup >> 3;
//        }
//        popupWindow.showAsDropDown(_verticalDelimiter, _xoffForPopup, -_verticalDelimiterHeightForPopup + _xoffForPopup);
    }

    private void hidePopupViews() {
//        if (_containerCircleViewsPopup != null) {
//            _containerCircleViewsPopup.hideAllViewsWithoutAnimation();
//        }
        if (_verticalDelimiter != null && _verticalDelimiter.getVisibility() == VISIBLE) {
            _verticalDelimiter.setVisibility(INVISIBLE);
        }
        if (AndroidApp.popupWindow.isShowing()) {
            AndroidApp.popupWindow.dismiss();
        }
    }
//
//    private void recalculatePopupViews() {
//        Line line; boolean isActiveChart;
//        for (Map.Entry<String, Line> entry : _chart.getLines().entrySet()) {
//            String key = entry.getKey();
//            line = _chart.getLines().get(key);
//            isActiveChart = line.getIsActive();
//            _containerCircleViewsPopup.hideOrShowViewWithTag(key, isActiveChart);
//        }
//    }

    @Override
    protected int getChartVerticalPadding() {
        return ThemeManager.CHART_BIG_VERTICAL_PADDING_SUM_PX;
    }

    @Override
    protected int getChartHalfVerticalPadding() {
        return ThemeManager.CHART_BIG_VERTICAL_PADDING_HALF_PX;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        _motionManagerBig = new MotionManagerForBigChart(getContext(), this, this);
        _motionManagerBig.attachView();
        this.getViewTreeObserver().addOnScrollChangedListener(this::hidePopupViews);
    }

    @Override
    protected void onDetachedFromWindow() {
        _motionManagerBig.deattachView();
        super.onDetachedFromWindow();
    }
}
