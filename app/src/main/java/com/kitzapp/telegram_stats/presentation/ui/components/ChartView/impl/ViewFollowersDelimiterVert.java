package com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.presentation.ui.components.simple.TChartTextView;
import com.kitzapp.telegram_stats.presentation.ui.components.simple.TDelimiterLine;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ivan Kuzmin on 28.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

class ViewFollowersDelimiterVert extends LinearLayout {
    private final String MMM_D_FORMAT = "MMM d";
    private final String MMM_Y_FORMAT = "MMM y";

    private TChartTextView _tTextView1;
    private TChartTextView _tTextView2;
    private TChartTextView _tTextView3;
    private TChartTextView _tTextView4;
    private TChartTextView _tTextView5;
    private TChartTextView _tTextView6;

    private long[] _dates = null;
    private int hashCodeDates;

    public ViewFollowersDelimiterVert(Context context) {
        super(context);
        this.init();
    }

    public ViewFollowersDelimiterVert(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public ViewFollowersDelimiterVert(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        setWillNotDraw(false);
        this.setOrientation(VERTICAL);

        _tTextView1 = this.getNewTextView(true);
        _tTextView2 = this.getNewTextView();
        _tTextView3 = this.getNewTextView();
        _tTextView4 = this.getNewTextView();
        _tTextView5 = this.getNewTextView();
        _tTextView6 = this.getNewTextView();

        this.addView(_tTextView1);
        this.addView(this.getDelimiterLine());
        this.addView(_tTextView2);
        this.addView(this.getDelimiterLine());
        this.addView(_tTextView3);
        this.addView(this.getDelimiterLine());
        this.addView(_tTextView4);
        this.addView(this.getDelimiterLine());
        this.addView(_tTextView5);
        this.addView(this.getDelimiterLine());
        this.addView(_tTextView6);
        this.addView(this.getDelimiterLine(true));
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
                    dateFormat = MMM_Y_FORMAT;
                } else {
                    dateFormat = MMM_D_FORMAT;
                }
            } else {
                dateFormat = MMM_D_FORMAT;
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

    private TChartTextView getCurrentTextView(int index) {
        TChartTextView tChartTextView = null;
        switch (index) {
            case 0:
                tChartTextView = _tTextView1;
                break;
            case 1:
                tChartTextView = _tTextView2;
                break;
            case 2:
                tChartTextView = _tTextView3;
                break;
            case 3:
                tChartTextView = _tTextView4;
                break;
            case 4:
                tChartTextView = _tTextView5;
                break;
            case 5:
                tChartTextView = _tTextView6;
                break;
        }
        return tChartTextView;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getLayoutParams().height = LayoutParams.MATCH_PARENT;
        getLayoutParams().height = LayoutParams.MATCH_PARENT;
    }

    private TChartTextView getNewTextView() {
        return this.getNewTextView(false);
    }

    private TChartTextView getNewTextView(boolean isFirst) {
        TChartTextView tChartTextView = new TChartTextView(getContext());
        tChartTextView.setGravity(Gravity.BOTTOM | Gravity.START);
        if (!isFirst) {
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f);
            tChartTextView.setLayoutParams(layoutParams);
        }
        return tChartTextView;
    }

    private TDelimiterLine getDelimiterLine() {
        return this.getDelimiterLine(false);
    }

    private TDelimiterLine getDelimiterLine(boolean isLast) {
        TDelimiterLine delimiterLine = new TDelimiterLine(getContext());
        LinearLayout.LayoutParams layoutParams = (LayoutParams) delimiterLine.getLayoutParams();
        float alpha;
        if (isLast) {
            alpha = 1f;
        } else {
            layoutParams.height = 1;
            alpha = 1f;
        }
        delimiterLine.setAlpha(alpha);
        layoutParams.setMargins(0, 0, 0, 0);
        return delimiterLine;
    }
}
