package com.kitzapp.telegram_stats.presentation.ui.components.simple;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;
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

public class TColorfulChartCircle extends View implements TViewObserver {

    private Paint _paint;
    private ShapeDrawable _shapeDrawable;
    private int _oldColor;
    private int _sizeInsideCircle;
    private int _center;
    private int _width;

    public TColorfulChartCircle(Context context) {
        super(context);
    }

    public TColorfulChartCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TColorfulChartCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TColorfulChartCircle(Context context, int color) {
        super(context);
        this._paint = AndroidUtilites.getPaint(color, ThemeManager.CHART_LINE_IN_PART_WIDTH_PX);
        this.init();
    }

    @Override
    public void init() {
        if (_shapeDrawable == null) {
            _center = ThemeManager.CHART_CIRCLE_SIZE_PX >> 1;
            _width = _center - ThemeManager.CHART_LINE_FULL_WIDTH_PX;

            _oldColor = getCurrentColor();
            _shapeDrawable = getCurrentDrawable(_oldColor);

            this.setBackground(_shapeDrawable);
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (_paint != null) {
            canvas.drawCircle(_center, _center, _width, _paint);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if ((int) arg == ObserverManager.KEY_OBSERVER_THEME_UPDATED) {
            int newColor = getCurrentColor();

            if (_oldColor != newColor) {
                // BACKGROUND CHANGE COLOR
                ValueAnimator backRGBAnim = AndroidUtilites.getArgbAnimator(
                        _oldColor,
                        newColor,
                        animation -> {
                            int color = ((int) animation.getAnimatedValue());
                            Drawable backgr = this.getBackground();

                            backgr.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                        });
                backRGBAnim.start();
                _oldColor = newColor;
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
        getLayoutParams().height = ThemeManager.CHART_CIRCLE_SIZE_PX;
        getLayoutParams().width = ThemeManager.CHART_CIRCLE_SIZE_PX;
    }

    private int getCurrentColor() {
        int color = ThemeManager.getColor(ThemeManager.key_cellBackColor);
        return color;
    }

    private ShapeDrawable getCurrentDrawable(int color) {
        ShapeDrawable oval = new ShapeDrawable (new OvalShape());
        oval.getPaint().setColor(color);
        return oval;
    }
}
