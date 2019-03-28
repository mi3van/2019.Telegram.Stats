package com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.Application.AppManagers.ObserverManager;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.presentation.ui.components.TTotalLinLayout;
import com.kitzapp.telegram_stats.presentation.ui.components.simple.TTextPaint;
import com.kitzapp.telegram_stats.presentation.ui.components.simple.TTextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;

/**
 * Created by Ivan Kuzmin on 28.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class ViewChartDates extends TTotalLinLayout {
    public interface Listener {
        void onDatesWasChecked(long[] dates);
    }

    private TTextView _tTextView1;
    private TTextView _tTextView2;
    private TTextView _tTextView3;
    private TTextView _tTextView4;
    private TTextView _tTextView5;

    private int _oldTitleColor;
    private long[] _dates = null;
    private int hashCodeDates;

    public ViewChartDates(Context context) {
        super(context);
    }

    public ViewChartDates(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewChartDates(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init() {
        setWillNotDraw(false);
        this.setOrientation(HORIZONTAL);
        _oldTitleColor = ThemeManager.chartDescrTextPaint.getColor();

        _tTextView1 = this.getNewTextView();
        _tTextView2 = this.getNewTextView();
        _tTextView3 = this.getNewTextView();
        _tTextView4 = this.getNewTextView();
        _tTextView5 = this.getNewTextView();

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
        TTextView currentTextView;
        String dateFormat; String dateString;
        SimpleDateFormat formatter;

        int currentDayInYear;   int nextDayInYear;  int nextIndex;
        long date;              long nextDate = 0;

        for (int i = 0; i < datesLength; i++) {
            date = i == 0 ? dates[0] : nextDate;
            dateFormat = "MMM d";

            nextIndex = i + 1;
            if (nextIndex < datesLength) {
                nextDate = dates[nextIndex];

                calendar.setTimeInMillis(nextDate);
                nextDayInYear = calendar.get(Calendar.DAY_OF_YEAR);

                calendar.setTimeInMillis(date);
                currentDayInYear = calendar.get(Calendar.DAY_OF_YEAR);

                if (currentDayInYear > nextDayInYear) {
                    dateFormat = "MMM y";
                }
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

    private TTextView getCurrentTextView(int index) {
        TTextView tTextView = null;
        switch (index) {
            case 0:
                tTextView = _tTextView1;
                break;
            case 1:
                tTextView = _tTextView2;
                break;
            case 2:
                tTextView = _tTextView3;
                break;
            case 3:
                tTextView = _tTextView4;
                break;
            case 4:
                tTextView = _tTextView5;
                break;
        }
        return tTextView;
    }

    @Override
    protected int getCurrentColor() {
        return super.getCurrentColor();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getLayoutParams().height = ThemeManager.CHART_CELL_HEIGHT_PX >> 1;
    }

    @Override
    public void update(Observable observable, Object arg) {
        if ((int) arg == ObserverManager.KEY_OBSERVER_THEME_UPDATED) {
            int newTitleColor = ThemeManager.chartDescrTextPaint.getColor();
            // TITLE CHANGE COLOR
            if (newTitleColor != _oldTitleColor) {
                ValueAnimator textRGBAnim = AndroidUtilites.getArgbAnimator(
                        _oldTitleColor,
                        newTitleColor,
                        animation -> {
                            int animColor = (int) animation.getAnimatedValue();
                            this.setTextsColor(animColor);
                        });
                textRGBAnim.start();
                _oldTitleColor = newTitleColor;
            }
        }
    }

    private TTextView getNewTextView() {
        TTextView tTextView = new TTextView(getContext());

        TTextPaint chartDescrTextPaint = ThemeManager.chartDescrTextPaint;
        tTextView.setTypeface(chartDescrTextPaint.getTypeface());
        tTextView.setTextColor(_oldTitleColor);
        tTextView.setTextSizeDP(chartDescrTextPaint.getTextSize());
        tTextView.setGravity(Gravity.CENTER);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        tTextView.setLayoutParams(layoutParams);

        return tTextView;
    }

    private void setTextsColor(int color) {
        _tTextView1.setTextColor(color);
        _tTextView2.setTextColor(color);
        _tTextView3.setTextColor(color);
        _tTextView4.setTextColor(color);
        _tTextView5.setTextColor(color);
    }
}
