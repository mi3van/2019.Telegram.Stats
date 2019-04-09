package com.kitzapp.telegram_stats.presentation.ui.components.simple;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.kitzapp.telegram_stats.Application.AndroidApp;
import com.kitzapp.telegram_stats.Application.AppManagers.ObserverManager;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;

import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.presentation.ui.components.TViewObserver;

import java.util.Observable;

/**
 * Created by Ivan Kuzmin on 2019-03-22.
 * Copyright © 2019 Example. All rights reserved.
 */

public class TColorfulLinLayout extends LinearLayout implements TViewObserver {

    private int _oldBackColor;

    public TColorfulLinLayout(Context context) {
        super(context);
        this.init();
    }

    public TColorfulLinLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TColorfulLinLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    @Override
    public void init() {
        setWillNotDraw(false);
        _oldBackColor = getCurrentColor();
        this.setBackgroundColor(_oldBackColor);
    }

    @Override
    public void addObserver() {
        AndroidApp.observerManager.addObserver(this);
    }

    @Override
    public void deleteObserver() {
        AndroidApp.observerManager.deleteObserver(this);
    }

    protected int getCurrentColor() {
        return ThemeManager.getColor(ThemeManager.key_totalBackColor);
    }

    @Override
    public void update(Observable o, Object arg) {
        if ((byte) arg == ObserverManager.KEY_OBSERVER_THEME_UPDATED) {
            int newColor = getCurrentColor();
            if (_oldBackColor != newColor) {
                ValueAnimator valueAnimator = AndroidUtilites.getArgbAnimator(
                        _oldBackColor,
                        newColor,
                        animation -> setBackgroundColor((int) animation.getAnimatedValue()));
                valueAnimator.start();
                _oldBackColor = newColor;
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
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        _oldBackColor = color;
    }
}
