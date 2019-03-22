package com.kitzapp.telegram_stats.presentation.ui.components;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import com.kitzapp.telegram_stats.AndroidApplication;
import com.kitzapp.telegram_stats.presentation.ui.ObserverManager;
import com.kitzapp.telegram_stats.presentation.ui.Theme;

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

    @Override
    public void addObserver() {
        AndroidApplication.observerManager.addObserver(this);
    }

    @Override
    public void deleteObserver() {
        AndroidApplication.observerManager.deleteObserver(this);
    }

    private int getCurrentColor() {
        return Theme.getColor(Theme.key_toolbarBackColor);
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
        this.addObserver();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.deleteObserver();
    }
}
