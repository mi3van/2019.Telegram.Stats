package com.kitzapp.telegram_stats.customViews.ChartView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.kitzapp.telegram_stats.AndroidApp;
import com.kitzapp.telegram_stats.core.appManagers.ObserverManager;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
import com.kitzapp.telegram_stats.R;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.core.appManagers.TViewObserver;
import com.kitzapp.telegram_stats.customViews.simple.TTextPaint;
import com.kitzapp.telegram_stats.customViews.simple.TTextView;

import java.util.Observable;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TViewChartTitle extends LinearLayout implements TViewObserver {

    private int _oldTitleColor;

    private TTextView tTextView;

    public TViewChartTitle(Context context) {
        super(context);
        this.init();
    }

    public TViewChartTitle(Context context,  AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TViewChartTitle(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    public void setText(String text) {
        tTextView.setText(text);
    }

    @Override
    public void init() {
        this.setOrientation(VERTICAL);
        _oldTitleColor = ThemeManager.chartTitleTextPaint.getColor();

        tTextView = new TTextView(getContext());
        TTextPaint chartTitleTextPaint = ThemeManager.chartTitleTextPaint;
        tTextView.setTypeface(chartTitleTextPaint.getTypeface());
        tTextView.setTextColor(_oldTitleColor);
        tTextView.setTextSizeDP(chartTitleTextPaint.getTextSize());
        tTextView.setText(getResources().getString(R.string.followers_title));

        setGravity(Gravity.CENTER_VERTICAL);
        addView(tTextView);

        int RightLeftPadding = ThemeManager.CHART_CELL_RIGHTLEFT_MARGIN_PX;
        setPadding(RightLeftPadding, 0, RightLeftPadding, 0);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.addObserver();
        getLayoutParams().height = ThemeManager.CELL_HEIGHT_56DP_IN_PX;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.deleteObserver();
    }

    @Override
    public void update(Observable o, Object arg) {
        if ((byte) arg == ObserverManager.KEY_OBSERVER_THEME_UPDATED) {
            int newTitleColor = ThemeManager.chartTitleTextPaint.getColor();

            // TITLE CHANGE COLOR
            if (newTitleColor != _oldTitleColor) {
                ValueAnimator textRGBAnim = AndroidUtilites.getArgbAnimator(
                        _oldTitleColor,
                        newTitleColor,
                        animation -> tTextView.setTextColor((int) animation.getAnimatedValue()));
                textRGBAnim.start();
                _oldTitleColor = newTitleColor;
            }
        }
    }

    @Override
    public void addObserver() {
        AndroidApp.observerManager.addObserver(this);
    }

    @Override
    public void deleteObserver() {
        AndroidApp.observerManager.deleteObserver(this);
    }
}
