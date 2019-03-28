package com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;
import com.kitzapp.telegram_stats.presentation.ui.components.simple.TChartTextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ivan Kuzmin on 28.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

class ViewChartDates extends LinearLayout {
    private final int TEXT_VIEW_TYPE_DEFAULT = 0;
    private final int TEXT_VIEW_TYPE_LEFT = -2;
    private final int TEXT_VIEW_TYPE_RIGHT = 2;
    private final String MMM_D_FORMAT = "MMM d";
    private final String M_Y_FORMAT = "M/y";

    public interface Listener {
        void onDatesWasChecked(long[] dates);
    }

    private TChartTextView _tTextView1;
    private TChartTextView _tTextView2;
    private TChartTextView _tTextView3;
    private TChartTextView _tTextView4;
    private TChartTextView _tTextView5;

    private long[] _dates = null;
    private int hashCodeDates;

    public ViewChartDates(Context context) {
        super(context);
        this.init();
    }

    public ViewChartDates(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public ViewChartDates(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
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

    public void setDatesAndInit(long[] arrayDates) {
        if (arrayDates != null) {
            int hashCodeNew = Arrays.hashCode(arrayDates);
            if (hashCodeNew == hashCodeDates) {
                return;
            }
            this._dates = arrayDates;
            hashCodeDates = Arrays.hashCode(arrayDates);

            this.initDatesView(_dates);
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void initDatesView(long[] dates) {
        Calendar calendar = Calendar.getInstance();

        int datesLength = dates.length;
        TChartTextView currentTextView;
        String dateFormat; String dateString;
        SimpleDateFormat formatter;

        int currentDayInYear;   int nextDayInYear;  int nextIndex;
        long date;              long nextDate = 0;

        for (int i = 0; i < datesLength; i++) {
            date = i == 0 ? dates[0] : nextDate;

            nextIndex = i + 1;
            if (nextIndex < datesLength) {
                nextDate = dates[nextIndex];

                calendar.setTimeInMillis(nextDate);
                nextDayInYear = calendar.get(Calendar.DAY_OF_YEAR);

                calendar.setTimeInMillis(date);
                currentDayInYear = calendar.get(Calendar.DAY_OF_YEAR);

                if (currentDayInYear > nextDayInYear) {
                    dateFormat = M_Y_FORMAT;
                } else {
                    dateFormat = MMM_D_FORMAT;
                    date = getFormattedDay(date, calendar);
                }
            } else {
                dateFormat = MMM_D_FORMAT;
                date = getFormattedDay(date, calendar);
            }

            currentTextView = getCurrentTextView(i);
            if (currentTextView == null) {
                continue;
            }
            formatter = new SimpleDateFormat(dateFormat);
            dateString = formatter.format(new Date(date));


            currentTextView.setText(dateString);
        }
    }

    private long getFormattedDay(long originalDate, Calendar calendar) {
        long newDate = originalDate;
        calendar.setTimeInMillis(newDate);

        int currentDayInMonth = calendar.get(Calendar.DAY_OF_MONTH);

        if (currentDayInMonth % 2 != 0) {
            if (currentDayInMonth == 31) {
                newDate += DateUtils.DAY_IN_MILLIS << 1;
            } else {
                newDate += DateUtils.DAY_IN_MILLIS;
            }
        }

        return newDate;
    }

    private TChartTextView getCurrentTextView(int index) {
        TChartTextView TChartTextView = null;
        switch (index) {
            case 0:
                TChartTextView = _tTextView1;
                break;
            case 1:
                TChartTextView = _tTextView2;
                break;
            case 2:
                TChartTextView = _tTextView3;
                break;
            case 3:
                TChartTextView = _tTextView4;
                break;
            case 4:
                TChartTextView = _tTextView5;
                break;
        }
        return TChartTextView;
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LinearLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        layoutParams.height = ThemeManager.CHART_CELL_HEIGHT_PX >> 1;
        int marginPx = ThemeManager.CHART_CELL_RIGHTLEFT_MARGIN_PX;
        layoutParams.setMargins(marginPx, 0, marginPx, marginPx >> 1);
    }

    private TChartTextView getNewTextView(int typeTexView) {
        TChartTextView tChartTextView = new TChartTextView(getContext());

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
}
