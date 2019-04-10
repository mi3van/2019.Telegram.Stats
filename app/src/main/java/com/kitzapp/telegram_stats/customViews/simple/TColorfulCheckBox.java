package com.kitzapp.telegram_stats.customViews.simple;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.CheckBox;
import com.kitzapp.telegram_stats.AndroidApp;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.core.appManagers.ObserverManager;
import com.kitzapp.telegram_stats.core.appManagers.TViewObserver;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;

import java.util.Observable;

/**
 * Created by Ivan Kuzmin on 25.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TColorfulCheckBox extends CheckBox implements TViewObserver {

    private int _oldTextColor;

    public interface Listener {
        void onBoxWasChecked(String key, boolean isChecked);
    }

    private String key;
    private String name;
    private int color;
    private Listener listener;

    public TColorfulCheckBox(Context context) {
        super(context);
    }

    public TColorfulCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TColorfulCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TColorfulCheckBox(Context context, String key, String name, int color, Listener listener) {
        super(context);
        this.key = key;
        this.name = name;
        this.color = color;
        this.listener = listener;

        this.init();
    }

    @Override
    public void init() {
        this.setTypeface(ThemeManager.rMediumTypeface);
        this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ThemeManager.TEXT_MEDIUM_SIZE_DP);
        this.setText(name);

        this.setChecked(true);
        this.setGravity(Gravity.CENTER_VERTICAL);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            this.setTextColor(color); // red or other text color
        } else {
            _oldTextColor = getCurrentColor();
            this.setTextColor(_oldTextColor); // black and white text smooth colors
            this.setButtonTintList(ColorStateList.valueOf(color));
        }

        setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onBoxWasChecked(key, isChecked);
            }
        });
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if ((byte) arg == ObserverManager.KEY_OBSERVER_THEME_UPDATED) {
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
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getLayoutParams().height = ThemeManager.CELL_HEIGHT_48DP_IN_PX;
        this.addObserver();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.deleteObserver();
    }

    private int getCurrentColor() {
        return ThemeManager.getColor(ThemeManager.key_blackWhiteTextColor);
    }
}
