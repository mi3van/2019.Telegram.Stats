package com.kitzapp.telegram_stats.customViews.charts.impl;

import android.content.Context;
import android.util.AttributeSet;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
import com.kitzapp.telegram_stats.core.appManagers.motions.MotionManagerForBigChart;
import com.kitzapp.telegram_stats.customViews.simple.TDelimiterLine;
import com.kitzapp.telegram_stats.customViews.simple.TViewChartInfoVert;
import com.kitzapp.telegram_stats.customViews.simple.TViewRectSelect;

import java.util.HashMap;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public abstract class TAbstractChartBig extends TPrivateChartBAAse implements TAbstractChartBigInterface,
                                                                            TViewRectSelect.RectListener,
                                                                            MotionManagerForBigChart.OnMyTouchListener {
    private final String PART_DATE_FORMAT = "EEE, d MMM yyyy";

    private TViewChartInfoVert _tViewChartInfoVert;
    private TDelimiterLine _verticalDelimiter;

    private HashMap<String, long[]> _partAxisesY = new HashMap<>();
    private float[] _partAxisXForGraph = null;
    private float _leftCursor;
    private float _rightCursor;
    private MotionManagerForBigChart _motionManagerBig;
    private int _oldIndexShowed;
    private int _leftInArray;
    private TAbstractChartBigInterface.Listener _chartBigListener;

    private TViewContainerCircleViews _containerForCircleViews;
    private int _verticalDelimiterHeight;
    private int _xoffVerticalDelimiterH;

    public TAbstractChartBig(Context context) {
        super(context);
    }

    public TAbstractChartBig(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TAbstractChartBig(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TAbstractChartBig(Context context, TAbstractChartBigInterface.Listener chartBigListener) {
        super(context);
        _chartBigListener = chartBigListener;
    }

    @Override
    public void onXTouchWasDetected(float newX) {

    }

    @Override
    public void onMiniatureViewIsLocked(boolean isLocked) {

    }

    @Override
    public TViewRectSelect.RectListener getRectListener() {
        return null;
    }

    @Override
    protected int getViewHeightForLayout() {
        return 0;
    }

    @Override
    protected int getLinePaintWidth() {
        return 0;
    }

    @Override
    public void onRectCursorsWasChanged(float leftCursor, float rightCursor) {

    }

    @Override
    public void wasChangedActiveChart() {

    }

    //    @Override
//    protected void firstInitAxisesAndVariables(boolean isNeedInitForCanvas) {
//        super.firstInitAxisesAndVariables(false);
//
//        _tViewChartInfoVert = new TViewChartInfoVert(getContext());
//        addView(_tViewChartInfoVert);
//
//        SETUP VERTICAL DELIMITER
//        _verticalDelimiter = new TDelimiterLine(getContext());
//        _verticalDelimiter.getLayoutParams().height = LayoutParams.MATCH_PARENT;
//        _verticalDelimiter.getLayoutParams().width = ThemeManager.CHART_DELIMITER_FATNESS_PX;
//        _verticalDelimiter.setVisibility(INVISIBLE);
//        addView(_verticalDelimiter);
//
//        _oldIndexShowed = -1;
//
//        this.initPopup();
//    }
//
//    private boolean _isCoeffXForPopupNeedCalculate = true;
//    private float _coeffXForPopup = 0;
//
//    @Override
//    public void onXTouchWasDetected(float newX) {
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
//            if (tempIndex >=0 && tempIndex != _oldIndexShowed) {
//                _oldIndexShowed = tempIndex;
//                this.drawPopupViews(_oldIndexShowed);
//            }
//        }
//    }
//
//    @Override
//    public void onMiniatureViewIsLocked(boolean isLocked) {
//        if (_chartBigListener != null) {
//            _chartBigListener.onMiniatureViewIsLocked(isLocked);
//        }
//    }
//
//    @Override
//    protected void drawLines(Canvas canvas, float[] axisX, HashMap<String, long[]> partAxisesY) {
//        if (_partAxisXForGraph != null) {
//            super.drawLines(canvas, _partAxisXForGraph, _partAxisesY);
//        }
//    }
//
//    public void recalculateYAndUpdateView() {
//        this.onRectCursorsWasChanged(_leftCursor, _rightCursor);
//    }
//
//    @Override
//    public void onRectCursorsWasChanged(float leftCursor, float rightCursor) {
//        _leftCursor = leftCursor;_rightCursor = rightCursor;
//        _isCoeffXForPopupNeedCalculate = true;
//
//        _leftInArray = (int) (_maxAxisXx * leftCursor);
//        int _rightInArray = (int) (_maxAxisXx * rightCursor);
//
//        int countPoints = _rightInArray - _leftInArray;
//
//        if (countPoints < 2) {
//            return;
//        }
//
//        this.hidePopupViews();
//
//        if (_chartBigListener != null) {
//            long[] dates = this.getDatesForSend(_leftInArray, _rightInArray);
//            _chartBigListener.onDatesWasChanged(dates);
//        }
//
//        // Get new part arrays for draw Y
//        _partAxisesY = getPartOfFullHashAxisY(_leftInArray, _rightInArray);
//        // Get new part arrays for draw X
//        _partAxisXForGraph = getPartOfFullAxisX(_leftInArray, _rightInArray);
//
//        float leftInPx = leftCursor * _viewWidth;
//        float rightInPx = rightCursor * _viewWidth;
//        _partAxisXForGraph = getAxisXCalculated(leftInPx, rightInPx, _partAxisXForGraph);
//
//        this.updateMaxAndMin();
//
//        invalidate();
//    }
//
//    private float[] getAxisXCalculated(float leftInPx, float rightInPx, float[] originalAxisX) {
//        int lengthX = originalAxisX.length;
//        float[] newAxis = new float[lengthX];
//
//        float widthInPx = rightInPx - leftInPx;
//        float persent = widthInPx / _viewWidth;
//        float tempValueX;
//        for (int i = 0; i < lengthX; i++) {
//            tempValueX = originalAxisX[i] - leftInPx;
//            tempValueX /= persent;
//            newAxis[i] = tempValueX;
//        }
//
//        return newAxis;
//    }
//
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
//        _tViewChartInfoVert.setDatesAndInit(_tempMaxAxisY, _tempMinAxisY);
//
//        _partAxisesY = getAxisesForCanvas(_partAxisesY, _tempMaxAxisY, _tempMinAxisY);
//    }
//
//    private MyLongPair getMaxAndMinInHashMap(HashMap<String, long[]> hashMap) {
//        long max = INTEGER_MIN_VALUE;
//        long min = INTEGER_MAX_VALUE;
//        boolean isActiveChart;
//        long[] valuesArray;
//        for (Map.Entry<String, long[]> entry: hashMap.entrySet()) {
//            isActiveChart = getChartIsActive(entry.getKey());
//            if (!isActiveChart) {
//                continue;
//            }
//            valuesArray = hashMap.get(entry.getKey());
//            for (long value : valuesArray) {
//                if (value > max) {
//                    max = value;
//                }
//                if (value < min) {
//                    min = value;
//                }
//            }
//        }
//        return new MyLongPair(max, min);
//    }
//
//    public TViewRectSelect.RectListener getRectListener() {
//        return this;
//    }
//
//    @Override
//    int getViewHeightForLayout() {
//        return ThemeManager.CHART_PART_HEIGHT_PX;
//    }
//
//    @Override
//    int getLinePaintWidth() {
//        return ThemeManager.CHART_LINE_IN_BIG_WIDTH_PX;
//    }
//
//    @Override
//    int getMaxDotsForApproxChart() {
//        return _maxAxisXx;
//    }
//
//    private long[] getDatesForSend(int leftCursorArray, int rightCursorArray) {
//        long[] partdatesArray = ArraysUtilites.getRange(leftCursorArray, rightCursorArray, _chart.getAxisX().getData());
//        int lengthPartArray = partdatesArray.length;
//
//        int maxSizeSendingArray = 6;
//        long[] sendingArray;
//
//        if (lengthPartArray > maxSizeSendingArray) {
//            sendingArray = new long[5];
//            int index1 = 0;
//            int index5 = lengthPartArray - 1;
//            int index3 = index5 >> 1;
//            int index2 = index3 >> 1;
//            int index4 = index2 + index3;
//            sendingArray[0] = partdatesArray[index1];
//            sendingArray[1] = partdatesArray[index2];
//            sendingArray[2] = partdatesArray[index3];
//            sendingArray[3] = partdatesArray[index4];
//            sendingArray[4] = partdatesArray[index5];
//        } else {
//            sendingArray = partdatesArray;
//        }
//
//        return sendingArray;
//    }
//
//    private void initPopup() {
//        _containerForCircleViews = new TViewContainerCircleViews(getContext());
//        addView(_containerForCircleViews);
//
//        for (Map.Entry<String, Line> entry : _chart.getLines().entrySet()) {
//            int color = entry.getValue().getColor();
//            _containerForCircleViews.initNewCircle(color, entry.getKey());
//        }
//    }
//
//    @SuppressLint("SimpleDateFormat")
//    private void drawPopupViews(int indexShowedPart) {
//
//        float currentX = _partAxisXForGraph[indexShowedPart];
//        _verticalDelimiter.setX(currentX);
//        _verticalDelimiter.setVisibility(VISIBLE);
//
//        //todo logic in popup
//        PopupWindow popupWindow = AndroidApp.popupWindow;
//        View popupView = popupWindow.getContentView();
//        TColorfulTextView titleTextPopup = popupView.findViewById(R.id.titleTextPopup);
//        titleTextPopup.setTextSizeDP(ThemeManager.TEXT_SMALL_SIZE_DP);
//        titleTextPopup.setTypeface(ThemeManager.rBoldTypeface);
//
//        int globalIndexArray;
//        if (_leftInArray != 0) {
//            globalIndexArray = indexShowedPart + _leftInArray - 1;
//        } else {
//            globalIndexArray = indexShowedPart + _leftInArray;
//        }
//        if (globalIndexArray < 0) {
//            globalIndexArray = 0;
//        }
//
//        long date = _chart.getAxisX().getData()[globalIndexArray];
//        SimpleDateFormat formatter = new SimpleDateFormat(PART_DATE_FORMAT);
//        String dateString = formatter.format(new Date(date));
//        titleTextPopup.setText(dateString);
//
//        TCellDescriptionTexts cellForPopup;
//        String title; int color; String value;
//        Line line;    boolean isActiveChart;
//
//        LinearLayout container = popupView.findViewById(R.id.containerPopup);
//        container.removeAllViews();
//        LinearLayout.LayoutParams layoutParamsText = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
//        layoutParamsText.weight = 1f;
//        layoutParamsText.setMargins(0, 0, ThemeManager.MARGIN_8DP_IN_PX, 0);
//        LinearLayout.LayoutParams layoutParamsDescription = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//        for (Map.Entry<String, Line> entry : _chart.getLines().entrySet()) {
//            String key = entry.getKey();
//            line = _chart.getLines().get(key);
//            isActiveChart = line.getIsActive();
//
//            title = line.getName();
//            color = line.getColor();
//            value = String.valueOf(line.getData()[globalIndexArray]);
//            cellForPopup = new TCellDescriptionTexts(getContext(), title, value, color, layoutParamsText, layoutParamsDescription);
//            container.addView(cellForPopup);
//
//            long y = _partAxisesY.get(key)[indexShowedPart];
//
//            boolean isVisibleCircle = isActiveChart;
//            _containerForCircleViews.setNewPositionAndAnimate(key, currentX, y, isVisibleCircle);
//        }
//
//        if (_verticalDelimiterHeight == 0) {
//            _verticalDelimiterHeight = _verticalDelimiter.getHeight();
//            _xoffVerticalDelimiterH = -_verticalDelimiterHeight >> 3;
//        }
//        popupWindow.showAsDropDown(_verticalDelimiter, _xoffVerticalDelimiterH, -_verticalDelimiterHeight + _xoffVerticalDelimiterH);
//    }
//
//    private void hidePopupViews() {
//        if (_containerForCircleViews != null) {
//            _containerForCircleViews.hideAllViewsWithoutAnimation();
//        }
//        if (_verticalDelimiter != null && _verticalDelimiter.getVisibility() == VISIBLE) {
//            _verticalDelimiter.setVisibility(INVISIBLE);
//        }
//        if (AndroidApp.popupWindow.isShowing()) {
//            AndroidApp.popupWindow.dismiss();
//        }
//    }
//
//    private void recalculatePopupViews() {
//        Line line; boolean isActiveChart;
//        for (Map.Entry<String, Line> entry : _chart.getLines().entrySet()) {
//            String key = entry.getKey();
//            line = _chart.getLines().get(key);
//            isActiveChart = line.getIsActive();
//            _containerForCircleViews.hideOrShowViewWithTag(key, isActiveChart);
//        }
//    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        _motionManagerBig = new MotionManagerForBigChart(getContext(), this, this);
        _motionManagerBig.attachView();
//        this.getViewTreeObserver().addOnScrollChangedListener(this::hidePopupViews);
    }



    @Override
    protected void onDetachedFromWindow() {
        _motionManagerBig.deattachView();
        super.onDetachedFromWindow();
    }

    @Override
    protected int getChartVerticalPadding() {
        return ThemeManager.CHART_BIG_VERTICAL_PADDING_SUM_PX;
    }

    @Override
    protected int getChartHalfVerticalPadding() {
        return ThemeManager.CHART_BIG_VERTICAL_PADDING_HALF_PX;
    }
}
