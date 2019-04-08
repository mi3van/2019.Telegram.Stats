package com.kitzapp.telegram_stats.presentation.ui.components.simple;

import android.animation.ValueAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.Application.AndroidApp;
import com.kitzapp.telegram_stats.Application.AppManagers.ObserverManager;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.presentation.ui.components.TViewObserver;

import java.util.Observable;

/**
 * Created by Ivan Kuzmin on 29.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TChartTextView extends TTextView implements TViewObserver {
    
    private int _oldTitleColor;
    
    public TChartTextView(Context context) {
        super(context);
        this.init();
    }

    public TChartTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TChartTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    @Override
    public void init() {
        _oldTitleColor = getCurrentColor();

        TTextPaint chartDescrTextPaint = ThemeManager.chartDescrTextPaint;
        setTypeface(chartDescrTextPaint.getTypeface());
        setTextColor(_oldTitleColor);
        setTextSizeDP(chartDescrTextPaint.getTextSize());
        setSingleLine(true);
        setEllipsize(TextUtils.TruncateAt.END);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        setLayoutParams(layoutParams);
    }


    @Override
    public void addObserver() {
        AndroidApp.observerManager.addObserver(this);
    }

    @Override
    public void deleteObserver() {
        AndroidApp.observerManager.deleteObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if ((byte) arg == ObserverManager.KEY_OBSERVER_THEME_UPDATED) {
            int newTitleColor = getCurrentColor();
            // TITLE CHANGE COLOR
            if (newTitleColor != _oldTitleColor) {
                ValueAnimator textRGBAnim = AndroidUtilites.getArgbAnimator(
                        _oldTitleColor,
                        newTitleColor,
                        animation -> {
                            int animColor = (int) animation.getAnimatedValue();
                            setTextColor(animColor);
                        });
                textRGBAnim.start();
                _oldTitleColor = newTitleColor;
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


    private int getCurrentColor() {
        return ThemeManager.chartDescrTextPaint.getColor();
    }

}
