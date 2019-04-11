package com.kitzapp.telegram_stats.customViews.simple;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.kitzapp.telegram_stats.AndroidApp;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.core.appManagers.ObserverManager;
import com.kitzapp.telegram_stats.core.appManagers.TViewObserver;

import java.util.Observable;

import static com.kitzapp.telegram_stats.core.appManagers.ThemeManager.*;

/**
 * Created by Ivan Kuzmin on 29.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TColorfulChartCircle extends View implements TViewObserver {

    private Paint _paintForCircle;
    private Paint _paintForBackgroundInside;
    private int _oldBackColor;
    private int _center;
    private int _circleRadius;
    private int _insideCircleRadius;

    public TColorfulChartCircle(Context context) {
        super(context);
    }

    public TColorfulChartCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TColorfulChartCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TColorfulChartCircle(Context context, int circleColor) {
        super(context);
        this._paintForCircle = AndroidUtilites.getPaint(circleColor, CHART_CIRCLE_LINE_WIDTH_PX);
        this.init();
    }

    public void init() {
        _center = CHART_CIRCLE_HALF_SIZE_PX;
        _circleRadius = CHART_CIRCLE_RADIUS_PX;
        _insideCircleRadius = CHART_CIRCLE_RADIUS_INSIDE_PX;
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (_paintForCircle != null) {
            canvas.drawCircle(_center, _center, _circleRadius, _paintForCircle);
        }
        if (_paintForBackgroundInside != null) {
            canvas.drawCircle(_center, _center, _insideCircleRadius, _paintForBackgroundInside);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if ((byte) arg == ObserverManager.KEY_OBSERVER_THEME_UPDATED) {
            int newColor = getCurrentColor();

            if (_oldBackColor != newColor) {
                // BACKGROUND CHANGE COLOR
                ValueAnimator backRGBAnim = AndroidUtilites.getArgbAnimator(
                        _oldBackColor,
                        newColor,
                        animation -> {
                            int color = ((int) animation.getAnimatedValue());
                            _paintForBackgroundInside.setColor(color);
                            this.invalidate();
                        });
                backRGBAnim.start();
                _oldBackColor = newColor;
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.deleteObserver();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.addObserver();
        getLayoutParams().height = CHART_CIRCLE_SIZE_PX;
        getLayoutParams().width = CHART_CIRCLE_SIZE_PX;

        _oldBackColor = getCurrentColor();
        _paintForBackgroundInside =  AndroidUtilites.getPaintFill(_oldBackColor);
    }

    private int getCurrentColor() {
        int color = getColor(key_cellBackColor);
        return color;
    }
}
