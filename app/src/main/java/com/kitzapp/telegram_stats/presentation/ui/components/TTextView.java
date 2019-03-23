package com.kitzapp.telegram_stats.presentation.ui.components;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.AndroidApplication;
import com.kitzapp.telegram_stats.presentation.ui.ObserverManager;
import com.kitzapp.telegram_stats.presentation.ui.ThemeManager;

import java.util.Observable;

/**
 * Created by Ivan Kuzmin on 22.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TTextView extends TextView implements TViewObserver {

    int _oldTextColor;

    public TTextView(Context context) {
        super(context);
        this.init();
    }

    public TTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    @Override
    public void init() {
        _oldTextColor = getCurrentColor();
        this.setTypeface(ThemeManager.simpleTextPaint.getTypeface());
        this.setTextColor(_oldTextColor);
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
}
