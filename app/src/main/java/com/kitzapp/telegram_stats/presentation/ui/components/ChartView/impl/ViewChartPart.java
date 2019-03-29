package com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.Application.AndroidApp;
import com.kitzapp.telegram_stats.Application.AppManagers.MotionManagerForPart;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;
import com.kitzapp.telegram_stats.R;
import com.kitzapp.telegram_stats.common.ArraysUtilites;
import com.kitzapp.telegram_stats.common.MyLongPair;
import com.kitzapp.telegram_stats.domain.model.chart.Chart;
import com.kitzapp.telegram_stats.domain.model.chart.impl.Line;
import com.kitzapp.telegram_stats.presentation.ui.components.TSimpleTextView;
import com.kitzapp.telegram_stats.presentation.ui.components.TTotalLinLayout;
import com.kitzapp.telegram_stats.presentation.ui.components.simple.TDelimiterLine;
import com.kitzapp.telegram_stats.presentation.ui.components.simple.TInfoCellForPopup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import static com.kitzapp.telegram_stats.common.AppConts.INTEGER_MAX_VALUE;
import static com.kitzapp.telegram_stats.common.AppConts.INTEGER_MIN_VALUE;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class ViewChartPart extends ViewChartBase implements ViewRectSelect.RectListener, MotionManagerForPart.OnMyTouchListener {
    private final String PART_DATE_FORMAT = "E, MMM d";

    private ViewFollowersDelimiterVert _viewFollowersVert;
    private TDelimiterLine _verticalDelimiter;

    private HashMap<String, long[]> _partAxisesY = new HashMap<>();
    private float[] _partAxisXForGraph = null;
    private float _leftCursor;
    private float _rightCursor;
    private MotionManagerForPart _motionManagerForPart;
    private int _oldIndexShowed;
    private int _leftInArray;
    private ViewChartDates.Listener _datesListener;

    private CellContainerForCircleViews _containerForCircleViews;

    public ViewChartPart(Context context) {
        super(context);
    }

    public ViewChartPart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewChartPart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    ViewChartPart(Context context, @NonNull Chart chart, ViewChartDates.Listener datesListener) {
        super(context, chart);
        _datesListener = datesListener;
    }

    @Override
    protected void firstInitAxisesAndVariables(boolean isNeedInitForCanvas) {
        super.firstInitAxisesAndVariables(false);

        _viewFollowersVert = new ViewFollowersDelimiterVert(getContext());
        addView(_viewFollowersVert);

//        SETUP VERTICAL DELIMITER
        _verticalDelimiter = new TDelimiterLine(getContext());
        _verticalDelimiter.getLayoutParams().height = LayoutParams.MATCH_PARENT;
        _verticalDelimiter.getLayoutParams().width = ThemeManager.CHART_DELIMITER_FATNESS_PX;
        addView(_verticalDelimiter);

        _motionManagerForPart = new MotionManagerForPart(getContext(), this, this);
        _oldIndexShowed = -1;

        _containerForCircleViews = new CellContainerForCircleViews(getContext());
        addView(_containerForCircleViews);
    }

    @Override
    public void onXwasDetected(float newX) {
        if (_partAxisXForGraph != null) {
            int tempIndex = -1;
            float coefficient = (_partAxisXForGraph[1] - _partAxisXForGraph[0]) / 2;
            for (int i = 0; i < _partAxisXForGraph.length; i++) {
                float currentPoint = _partAxisXForGraph[i] - coefficient;
                float nextPoint = _partAxisXForGraph[i] + coefficient;
                if (newX > currentPoint && newX <= nextPoint) {
                    tempIndex = i;
                    break;
                }
            }
            if (tempIndex >=0 && tempIndex != _oldIndexShowed) {
                _oldIndexShowed = tempIndex;
                drawPopupViews(_oldIndexShowed);
            }
        }
    }

    @Override
    protected void drawLines(Canvas canvas, float[] axisX, HashMap<String, long[]> partAxisesY) {
        if (_partAxisXForGraph != null) {
            super.drawLines(canvas, _partAxisXForGraph, _partAxisesY);
        }
    }

    void recalculateYAndUpdateView() {
        this.onRectCursorsWasChanged(_leftCursor, _rightCursor);
    }

    @Override
    public void onRectCursorsWasChanged(float leftCursor, float rightCursor) {
        _leftCursor = leftCursor;
        _rightCursor = rightCursor;
        _leftInArray = (int) (_maxAxisXx * leftCursor);
        int rightInArray = (int) (_maxAxisXx * rightCursor) + 1;

        int countPoints = rightInArray - _leftInArray;

        if (countPoints < 2) {
            return;
        }

        this.hidePopupViews();

        if (_datesListener != null) {
            long[] dates = this.getDatesForSend(_leftInArray, rightInArray);
            _datesListener.onDatesWasChanged(dates);
        }

        // Get new part arrays for draw Y
        _partAxisesY = getPartOfFullHashAxisY(_leftInArray, rightInArray);
        // Get new part arrays for draw X
        _partAxisXForGraph = getPartOfFullAxisX(_leftInArray, rightInArray);

        float leftInPx = leftCursor * _viewWidth;
        float rightInPx = rightCursor * _viewWidth;
        _partAxisXForGraph = getAxisXCalculated(leftInPx, rightInPx, _partAxisXForGraph);

        this.updateMaxAndMin();

        invalidate();
    }

    private float[] getAxisXCalculated(float leftInPx, float rightInPx, float[] originalAxisX) {
        int lengthX = originalAxisX.length;
        float[] newAxis = new float[lengthX];

        float widthInPx = rightInPx - leftInPx;
        float persent = widthInPx / _viewWidth;
        float tempValueX;
        for (int i = 0; i < lengthX; i++) {
            tempValueX = originalAxisX[i] - leftInPx;
            tempValueX /= persent;
            newAxis[i] = tempValueX;
        }

        return newAxis;
    }

    private void updateMaxAndMin() {

        MyLongPair maxAndMinInPoint = this.getMaxAndMinInHashMap(_partAxisesY);

        long _tempMaxAxisY = maxAndMinInPoint.getMax();
        if (_tempMaxAxisY == INTEGER_MIN_VALUE) {
            return;
        }
        long _tempMinAxisY = maxAndMinInPoint.getMin();

        _viewFollowersVert.setDatesAndInit(_tempMaxAxisY, _tempMinAxisY);

        _partAxisesY = getAxisesForCanvas(_partAxisesY, _tempMaxAxisY, _tempMinAxisY);
    }

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

    @Override
    public void update(Observable o, Object arg) {

    }

    ViewRectSelect.RectListener getRectListener() {
        return this;
    }

    @Override
    int getViewHeightForLayout() {
        return ThemeManager.CHART_PART_HEIGHT_PX;
    }

    @Override
    int getLinePaintWidth() {
        return ThemeManager.CHART_LINE_IN_PART_WIDTH_PX;
    }

    @Override
    int getMaxDotsForApproxChart() {
        return _maxAxisXx;
    }

    private long[] getDatesForSend(int leftCursorArray, int rightCursorArray) {
        long[] partdatesArray = ArraysUtilites.getRange(leftCursorArray, rightCursorArray, _chart.getAxisX().getData());
        int lengthPartArray = partdatesArray.length;

        int maxSizeSendingArray = 6;
        long[] sendingArray;

        if (lengthPartArray > maxSizeSendingArray) {
            sendingArray = new long[5];
            int index1 = 0;
            int index5 = lengthPartArray - 1;
            int index3 = index5 >> 1;
            int index2 = index3 >> 1;
            int index4 = index2 + index3;
            sendingArray[0] = partdatesArray[index1];
            sendingArray[1] = partdatesArray[index2];
            sendingArray[2] = partdatesArray[index3];
            sendingArray[3] = partdatesArray[index4];
            sendingArray[4] = partdatesArray[index5];
        } else {
            sendingArray = partdatesArray;
        }

        return sendingArray;
    }

    @SuppressLint("SimpleDateFormat")
    private void drawPopupViews(int indexShowedPart) {

        _verticalDelimiter.setX(_partAxisXForGraph[indexShowedPart]);

        PopupWindow popupWindow = AndroidApp.popupWindow;
        View popupView = popupWindow.getContentView();
        TTotalLinLayout linLayout = popupView.findViewById(R.id.totalLayoutPopup);
        linLayout.init();
        TSimpleTextView simpleText = popupView.findViewById(R.id.simpleTextPopup);
        simpleText.init();
        simpleText.setTextSizeDP(ThemeManager.chartDescrTextPaint.getTextSize());
        simpleText.setTypeface(ThemeManager.chartTitleTextPaint.getTypeface());
        int globalIndex;
        if (_leftInArray != 0) {
            globalIndex = indexShowedPart + _leftInArray - 1;
        } else {
            globalIndex = indexShowedPart + _leftInArray;
        }
        if (globalIndex < 0) {
            globalIndex = 0;
        }

        long date = _chart.getAxisX().getData()[globalIndex];
        SimpleDateFormat formatter = new SimpleDateFormat(PART_DATE_FORMAT);
        String dateString = formatter.format(new Date(date));
        simpleText.setText(dateString);

        TInfoCellForPopup cellForPopup;
        String title; int color; String value;
        Line line;
        boolean isActiveChart;
        LinearLayout container = popupView.findViewById(R.id.containerPopup);
        container.removeAllViews();
        _containerForCircleViews.setVisibility(VISIBLE);
        _containerForCircleViews.removeAllViews();
        for (Map.Entry<String, Line> entry : _chart.getLines().entrySet()) {
            line = _chart.getLines().get(entry.getKey());
            isActiveChart = line.getIsActive();
            if (isActiveChart) {
                title = line.getName();
                color = line.getColor();
                value = String.valueOf(line.getData()[globalIndex]);
                cellForPopup = new TInfoCellForPopup(getContext(), title, value, color);
                container.addView(cellForPopup);
                _containerForCircleViews.addCircle(_partAxisXForGraph[indexShowedPart],
                        _partAxisesY.get(entry.getKey())[indexShowedPart], color);
            }
        }

        int _verticalDelimiterHeight = _verticalDelimiter.getHeight();
        int _xoffVerticalDelimiterH = -_verticalDelimiterHeight >> 3;
        popupWindow.showAsDropDown(_verticalDelimiter, _xoffVerticalDelimiterH, -_verticalDelimiterHeight + _xoffVerticalDelimiterH);

    }

    private void hidePopupViews() {
        PopupWindow popupWindow = AndroidApp.popupWindow;
        if ((popupWindow != null && popupWindow.isShowing()) ||
                (_verticalDelimiter != null && _verticalDelimiter.getVisibility() == VISIBLE)) {
            assert popupWindow != null;
            _verticalDelimiter.setX(-10);
            popupWindow.dismiss();
            _containerForCircleViews.removeAllViews();
            _containerForCircleViews.setVisibility(GONE);
            invalidate();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        this.getViewTreeObserver().addOnScrollChangedListener(this::hidePopupViews);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        _motionManagerForPart.deattachView();
    }
}
