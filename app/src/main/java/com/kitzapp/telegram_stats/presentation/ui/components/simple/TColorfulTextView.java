package com.kitzapp.telegram_stats.presentation.ui.components.simple;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.Application.AndroidApp;
import com.kitzapp.telegram_stats.Application.AppManagers.ObserverManager;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.presentation.ui.components.TViewObserver;

import java.util.Observable;

/**
 * Created by Ivan Kuzmin on 22.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TColorfulTextView extends TTextView implements TViewObserver {

    private int _oldTextColor;

    public TColorfulTextView(Context context) {
        super(context);
        this.init();
    }

    public TColorfulTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TColorfulTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    @Override
    public void init() {
        TTextPaint simpleTextPaint = ThemeManager.simpleTextPaint;
        this.setTypeface(simpleTextPaint.getTypeface());
        this.setTextSizeDP(simpleTextPaint.getTextSize());
        _oldTextColor = getCurrentColor();
        this.setTextColor(_oldTextColor);
    }

    @Override
    public void addObserver() {
        AndroidApp.observerManager.addObserver(this);
    }

    @Override
    public void deleteObserver() {
        AndroidApp.observerManager.deleteObserver(this);
    }

    private int getCurrentColor() {
        return ThemeManager.simpleTextPaint.getColor();
    }

    @Override
    public void update(Observable o, Object arg) {
        if ((int) arg == ObserverManager.KEY_OBSERVER_THEME_UPDATED) {
            int newColor = getCurrentColor();
            if (_oldTextColor != newColor) {
                ValueAnimator valueAnimator = AndroidUtilites.getArgbAnimator(
                        _oldTextColor,
                        newColor,
                        animation -> setTextColor((int) animation.getAnimatedValue()));
                valueAnimator.start();
                _oldTextColor = newColor;
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.addObserver();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.deleteObserver();
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        _oldTextColor = color;
    }
}
