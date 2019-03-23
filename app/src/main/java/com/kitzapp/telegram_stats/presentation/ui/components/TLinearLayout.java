package com.kitzapp.telegram_stats.presentation.ui.components;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.kitzapp.telegram_stats.AndroidApplication;
import com.kitzapp.telegram_stats.presentation.ui.ObserverManager;
import com.kitzapp.telegram_stats.presentation.ui.ThemeManager;

import androidx.annotation.Nullable;

import java.util.Observable;

/**
 * Created by Ivan Kuzmin on 2019-03-22.
 * Copyright © 2019 Example. All rights reserved.
 */

class TLinearLayout extends LinearLayout implements TViewObserver {

    private int _oldColor;

    public TLinearLayout(Context context) {
        super(context);
        this.init();
    }

    public TLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    @Override
    public void init() {
        setWillNotDraw(false);
        _oldColor = getCurrentColor();
        this.setBackgroundColor(_oldColor);
    }

    @Override
    public void addObserver() {
        AndroidApplication.observerManager.addObserver(this);
    }

    @Override
    public void deleteObserver() {
        AndroidApplication.observerManager.deleteObserver(this);
    }

    private int getCurrentColor() {
        return ThemeManager.getColor(ThemeManager.key_totalBackColor);
    }

    @Override
    public void update(Observable o, Object arg) {
        if ((int) arg == ObserverManager.KEY_OBSERVER_THEME_UPDATED) {
            int newColor = getCurrentColor();
            if (_oldColor != newColor) {
                ValueAnimator valueAnimator = AndroidUtilites.getArgbAnimator(
                        _oldColor,
                        newColor,
                        animation -> setBackgroundColor((int) animation.getAnimatedValue()));
                valueAnimator.start();
                _oldColor = newColor;
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
}
