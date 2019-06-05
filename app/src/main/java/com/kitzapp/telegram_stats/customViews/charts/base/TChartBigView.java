package com.kitzapp.telegram_stats.customViews.charts.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.kitzapp.telegram_stats.AndroidApp;
import com.kitzapp.telegram_stats.R;
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

import static com.kitzapp.telegram_stats.core.appManagers.ThemeManager.CHART_LINE_IN_MINIATURE_WIDTH_PX;

public abstract class TChartBigView extends TAbstractChartBase implements TChartBigViewInterface,
                                                                            TChartMiniatureVInterface.Listener,
                                                                            MotionManagerForBigChart.OnMyTouchListener {
    private TViewChartInfoVert _tViewChartInfoVert;
    private TDelimiterLine _verticalDelimiter;

    private HashMap<String, long[]> _axisesYFlipAndCalculated = new HashMap<>();

    private float _leftCursor, _rightCursor;
    private int _leftInArray, _rightInArray;

    private MotionManagerForBigChart _motionManagerBig;
    private TChartBigViewInterface.Listener _chartBigListener;

    // MATRIX VALUE
    private int _pxMatrix = getChartHorizPadding() - (CHART_LINE_IN_MINIATURE_WIDTH_PX >> 1);
    private float _pyMatrix;

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
        super.loadData(chart);

        if (_chartBigListener != null) {
            long[] axisX = _chart.getAxisX().getData();
            _chartBigListener.onDatesWasChanged(axisX);
        }
        isOriginalArrayWasFlipped = false;
    }

    private boolean isOriginalArrayWasFlipped = false;

    @Override
    public void onDataWasRecalculated(float[] axisXForCanvas, HashMap<String, long[]> orginalYArray,
                                      int maxConstAxisX, long maxConstAxisY) {
        this.updateSizeValues();

        _constMaxAxisXx = maxConstAxisX;
        _constMaxAxisY = maxConstAxisY;
        if (!isOriginalArrayWasFlipped) {
            _axisesYOriginalArrays = orginalYArray;
            _axisesYFlipAndCalculated = this.calcArrayForViewFlipVert(_axisesYOriginalArrays, maxConstAxisY);
            isOriginalArrayWasFlipped = true;

            _pyMatrix = _calculatingViewHeight;
        }
        _axisXForCanvas = axisXForCanvas;

        _isFirstDraw = true;
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
    public void onRectCursorsWasChanged(float leftCursor, float rightCursor) {
        if (_leftCursor == leftCursor && _rightCursor == rightCursor && !_isFirstDraw) {
            return;
        }
        _leftCursor = leftCursor;_rightCursor = rightCursor;

        _leftInArray = Math.round (_constMaxAxisXx * leftCursor);
        _rightInArray = Math.round (_constMaxAxisXx * rightCursor);

        int countPoints = _rightInArray - _leftInArray;

        if (countPoints < 2) {
            return;
        }

        if (_chartBigListener != null) {
            _chartBigListener.onDatesChangeSection(_leftInArray, _rightInArray);
        }

        initPathsForDraw();

        scaleYAnimationStart();
    }

    @Override
    protected void initPathsForDraw() {
        super.initPathsForDraw();

        _tViewChartInfoVert.setDatesAndInit(_tempMaxAxisY, 0);
    }

    @Override
    protected int getLeftInArray() {
        return _leftInArray;
    }

    @Override
    protected int getRightInArray() {
        return _rightInArray;
    }

    @Override
    protected HashMap<String, long[]> getCalculatedYArrays() {
        return _axisesYFlipAndCalculated;
    }

    @Override
    protected int getPxForMatrix() {
        return _pxMatrix;
    }

    @Override
    protected float getPyForMatrix() {
        return _pyMatrix;
    }

    @Override
    protected float getLeftCursor() {
        return _leftCursor;
    }

    @Override
    protected float getRightCursor() {
        return _rightCursor;
    }

    @Override
    protected void needRecalculatePathYScale(float newYScale) {
        super.needRecalculatePathYScale(newYScale);
        initPathsForDraw();
    }

    private HashMap<String, long[]> calcArrayForViewFlipVert(HashMap<String, long[]> axisesYOriginalArrays, long maxAxisY) {
        HashMap<String, long[]> flippedArray = new HashMap<>();
        if (axisesYOriginalArrays.isEmpty()) {
            return flippedArray;
        }
        float coeff = maxAxisY / _calculatingViewHeight;
        long[] array;
        for (Map.Entry<String, long[]> entry : axisesYOriginalArrays.entrySet()) {
            array = entry.getValue().clone();
            float tempValue;
            for (int i = 0; i < array.length; i++) {
                tempValue = array[i] / coeff;
                array[i] = (long) (_calculatingViewHeight - tempValue + getChartHalfVerticalPadding());
            }
            flippedArray.put(entry.getKey(), array);
        }
        return flippedArray;
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
