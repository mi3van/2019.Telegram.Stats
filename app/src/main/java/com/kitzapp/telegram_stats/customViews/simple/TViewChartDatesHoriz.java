package com.kitzapp.telegram_stats.customViews.simple;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ivan Kuzmin on 28.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TViewChartDatesHoriz extends LinearLayout {
    private final int TEXT_VIEW_TYPE_DEFAULT = 0;
    private final int TEXT_VIEW_TYPE_LEFT = -2;
    private final int TEXT_VIEW_TYPE_RIGHT = 2;
    private final String MMM_D_FORMAT = "MMM d";

    private TChartDescrTextView _tTextView1;
    private TChartDescrTextView _tTextView2;
    private TChartDescrTextView _tTextView3;
    private TChartDescrTextView _tTextView4;
    private TChartDescrTextView _tTextView5;

    private int _oldLeftIndex, _oldRightIndex;
    private String[] _fullDatesArray = new String[]{""};
    private int[] _showingIndexies = new int[5];

    public TViewChartDatesHoriz(Context context) {
        super(context);
        this.init();
    }

    public TViewChartDatesHoriz(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TViewChartDatesHoriz(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    public void setNewDates(long[] arrayDates) {
        if (arrayDates == null) {
            return;
        }
        _oldLeftIndex = -1;
        _oldRightIndex = -1;

        _fullDatesArray = this.getFullDatasStringsArray(arrayDates);
    }

    public void onDatesChangeSection(int leftInArray, int rightInArray) {
        boolean isIndexesInOutSector = leftInArray < 0 || rightInArray > _fullDatesArray.length;
        if (isIndexesInOutSector) {
            return;
        }
        boolean isDatesCalculated = _oldLeftIndex == leftInArray && _oldRightIndex == rightInArray;
        if (isDatesCalculated) {
            return;
        }
        _oldLeftIndex = leftInArray;
        _oldRightIndex = rightInArray;

        _showingIndexies = this.getIndexesForShow(leftInArray, rightInArray);

        for(int i = 0; i < _showingIndexies.length; i++) {
            int globalIndex = _showingIndexies[i];
            String dataText = _fullDatesArray[globalIndex];
            getCurrentTextView(i).setText(dataText);
        }
    }

    private int[] getIndexesForShow(int leftCursorArray, int rightCursorArray) {

        int lengthPartArray = rightCursorArray - leftCursorArray;

        int[] sendingArray = new int[5];
        sendingArray[0] = 0;
        sendingArray[4] = lengthPartArray - 1;
        sendingArray[2] = sendingArray[4] >> 1;
        sendingArray[1] = sendingArray[2] >> 1;
        sendingArray[3] = sendingArray[1]  + sendingArray[2];

        for (int i = 0; i < sendingArray.length; i++) {
            sendingArray[i] = sendingArray[i] + leftCursorArray;
        }

        return sendingArray;
    }

    @SuppressLint("SimpleDateFormat")
    private String[] getFullDatasStringsArray(long[] dates) {
        int datesLength = dates.length;

        String[] newStringArray = new String[datesLength];

        String dateString;
        SimpleDateFormat formatter;
        long date;

        for (int i = 0; i < datesLength; i++) {
            date = dates[i];

            formatter = new SimpleDateFormat(MMM_D_FORMAT);
            dateString = formatter.format(new Date(date));

            newStringArray[i] = dateString;
        }
        return newStringArray;
    }

    private TChartDescrTextView getCurrentTextView(int index) {
        TChartDescrTextView _tChartTextView = null;
        switch (index) {
            case 0:
                _tChartTextView = _tTextView1;
                break;
            case 1:
                _tChartTextView = _tTextView2;
                break;
            case 2:
                _tChartTextView = _tTextView3;
                break;
            case 3:
                _tChartTextView = _tTextView4;
                break;
            case 4:
                _tChartTextView = _tTextView5;
                break;
        }
        return _tChartTextView;
    }

    private void init() {
        setWillNotDraw(false);
        this.setOrientation(HORIZONTAL);

        _tTextView1 = this.getNewTextView(TEXT_VIEW_TYPE_LEFT);
        _tTextView2 = this.getNewTextView(TEXT_VIEW_TYPE_DEFAULT);
        _tTextView3 = this.getNewTextView(TEXT_VIEW_TYPE_DEFAULT);
        _tTextView4 = this.getNewTextView(TEXT_VIEW_TYPE_DEFAULT);
        _tTextView5 = this.getNewTextView(TEXT_VIEW_TYPE_RIGHT);

        this.addView(_tTextView1);
        this.addView(_tTextView2);
        this.addView(_tTextView3);
        this.addView(_tTextView4);
        this.addView(_tTextView5);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LinearLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        layoutParams.height = ThemeManager.CELL_HEIGHT_48DP_IN_PX >> 1;
        int marginPx = ThemeManager.MARGIN_16DP_IN_PX;
        layoutParams.setMargins(marginPx, 0, marginPx, ThemeManager.MARGIN_8DP_IN_PX);
    }

    private TChartDescrTextView getNewTextView(int typeTexView) {
        TChartDescrTextView tChartTextView = new TChartDescrTextView(getContext());

        float weight;
        switch (typeTexView) {
            case TEXT_VIEW_TYPE_LEFT:
                tChartTextView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                weight = 1.1f;
                break;
            case TEXT_VIEW_TYPE_RIGHT:
                tChartTextView.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                weight = 1.1f;
                break;
            default:
                tChartTextView.setGravity(Gravity.CENTER);
                weight = 1f;
                break;
        }

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, weight);
        tChartTextView.setLayoutParams(layoutParams);

        return tChartTextView;
    }

//    private long getFormattedDay(long originalDate, Calendar calendar) {
//        long newDate = originalDate;
//        calendar.setTimeInMillis(newDate);
//
//        int currentDayInMonth = calendar.get(Calendar.DAY_OF_MONTH);
//
//        if (currentDayInMonth % 2 != 0) {
//            if (currentDayInMonth == 31) {
//                newDate += DateUtils.DAY_IN_MILLIS << 1;
//            } else {
//                newDate += DateUtils.DAY_IN_MILLIS;
//            }
//        }
//
//        return newDate;
//    }
}
