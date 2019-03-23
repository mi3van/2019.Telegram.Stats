package com.kitzapp.telegram_stats.presentation.ui.components;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import com.kitzapp.telegram_stats.Application.AndroidApp;
import com.kitzapp.telegram_stats.Application.AppManagers.ObserverManager;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;

import java.util.Observable;

/**
 * Created by Ivan Kuzmin on 22.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TToolbar extends Toolbar implements TViewObserver {

    private int _oldBackColor;

    public TToolbar(Context context) {
        super(context);
        this.init();
    }

    public TToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    @Override
    public void init() {
        setWillNotDraw(false);
        _oldBackColor = getCurrentColor();
        this.setBackgroundColor(_oldBackColor);
    }

    private void changeToolbarFont() {
        for (int i = 0; i < this.getChildCount(); i++) {
            View view = this.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                if (tv.getText().equals(this.getTitle())) {
                    Typeface toolbartypeface = ThemeManager.toolbarTextPaint.getTypeface();
                    tv.setTypeface(toolbartypeface);
                    break;
                }
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

    private int getCurrentColor() {
        return ThemeManager.getColor(ThemeManager.key_toolbarBackColor);
    }

    @Override
    public void update(Observable o, Object arg) {
        if ((int) arg == ObserverManager.KEY_OBSERVER_THEME_UPDATED) {
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
        this.changeToolbarFont();
        this.addObserver();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.deleteObserver();
    }
}
