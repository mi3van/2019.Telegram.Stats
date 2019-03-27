package com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.Application.AndroidApp;
import com.kitzapp.telegram_stats.Application.AppManagers.ObserverManager;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.presentation.ui.components.TViewObserver;

import java.util.Observable;

/**
 * Created by Ivan Kuzmin on 2019-03-26;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

class ViewRectSelect extends View implements TViewObserver {
    private final float MIN_LEFT_CURSOR_VALUE = 0f;
    private final float MAX_LEFT_CURSOR_VALUE = 0.7f;
    private final float MIN_RIGHT_CURSOR = 0.3f;
    private final float MAX_RIGHT_CURSOR = 1f;
    private int _oldBackColor;

    interface RectListener {
        void onRectCursorsWasChanged(float leftCursor, float rightCursor);
    }

    private Rect _centerRect = new Rect();
    private Rect _rectLeftBack = new Rect();
    private Rect _rectRightBack = new Rect();

    private Paint _backPaint;
    private Paint _verticalPaint;
    private int _halfWidthVPaint;

    private float _leftCursor;  // 0 - 0.7 % is available
    private float _rightCursor; // 0.3 - 1 % is available
    private int _canvasWidth;

    private boolean _isNeedCalculateValues = false;
    private boolean _isNeedDrawOnly = false;

    private RectListener _rectListener;

    public ViewRectSelect(Context context) {
        super(context);
        this.init();
    }

    public ViewRectSelect(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public ViewRectSelect(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    ViewRectSelect(Context context, RectListener rectListener) {
        super(context);
        this._rectListener = rectListener;
        this.init();
    }

    @Override
    public void init() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(layoutParams);

        int colorPaintRect = ThemeManager.getColor(ThemeManager.key_rectSelectColor);
        _verticalPaint = AndroidUtilites.getPaint(colorPaintRect, ThemeManager.CHART_RECT_SELECT_WIDTH_PX);

        _oldBackColor = this.getCurrentBackgrColor();
        _backPaint = AndroidUtilites.getPaintFill(_oldBackColor);

        int _widthVPaint = (int) _verticalPaint.getStrokeWidth();
        _halfWidthVPaint = _widthVPaint >> 1;

        setOnClickListener(l -> this.setCursors(_leftCursor - 0.02f, _rightCursor - 0.01f));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.drawRect(canvas);
    }

    private void setLeftCursor(float leftCursor) {
        this.setCursors(leftCursor, _rightCursor);
    }

    private void setRightCursor(float rightCursor) {
        this.setCursors(_leftCursor, rightCursor);
    }

    private void setCursors(float leftCursor, float rightCursor) {
        boolean isCursorAvailableChange = (leftCursor != _leftCursor || rightCursor != _rightCursor)
                                                    && (rightCursor - leftCursor >= 0.3f);
        if (isCursorAvailableChange) {
            _leftCursor = Math.max(leftCursor, MIN_LEFT_CURSOR_VALUE);
            if (_leftCursor > MAX_LEFT_CURSOR_VALUE) {
                _leftCursor = MAX_LEFT_CURSOR_VALUE;
            }
            _rightCursor = Math.max(rightCursor, MIN_RIGHT_CURSOR);
            if (_rightCursor > MAX_RIGHT_CURSOR) {
                _rightCursor = MAX_RIGHT_CURSOR;
            }

            _isNeedCalculateValues = true;
            this.sendNewCursors(_leftCursor, _rightCursor);

            invalidate();
        }
    }

    private void drawRect(Canvas canvas) {
        if (!_isNeedDrawOnly) {
            if (canvas.getWidth() == _canvasWidth && !_isNeedCalculateValues) {
                return;
            }
        }
        _isNeedCalculateValues = false;
        int leftCurrentV = 0, currentRightV = 0;
        if (!_isNeedDrawOnly) {
            _canvasWidth = canvas.getWidth();
            int leftCursorInPX = (int) (_canvasWidth * _leftCursor);
            int rightCursorInPX = (int) (_canvasWidth * _rightCursor);
            leftCurrentV = _halfWidthVPaint + leftCursorInPX;
            currentRightV = rightCursorInPX - _halfWidthVPaint;

            // RECT
            int _vertCoeff = 1;
            _centerRect.top = -_vertCoeff;
            _centerRect.bottom = canvas.getHeight() + _vertCoeff;
            _centerRect.left = leftCurrentV;
            _centerRect.right = currentRightV;
        }
        canvas.drawRect(_centerRect, _verticalPaint);

//         LEFT BACK
        boolean isLeftBackAvailableDraw = _centerRect.left - _halfWidthVPaint > 0;
        if (isLeftBackAvailableDraw) {
            if (!_isNeedDrawOnly) {
                _rectLeftBack.top = 0;
                _rectLeftBack.bottom = canvas.getHeight();
                _rectLeftBack.left = 0;
                _rectLeftBack.right = leftCurrentV - _halfWidthVPaint;
            }
            canvas.drawRect(_rectLeftBack, _backPaint);
        }

        // RIGHT BACK
        boolean isRightBackAvailableDraw = _centerRect.right + _halfWidthVPaint < _canvasWidth;
        if (isRightBackAvailableDraw) {
            if (!_isNeedDrawOnly) {
                _rectRightBack.top = 0;     _rectRightBack.bottom = canvas.getHeight();
                _rectRightBack.left = currentRightV + _halfWidthVPaint; _rectRightBack.right = _canvasWidth;
            }
            canvas.drawRect(_rectRightBack, _backPaint);
        }
        _isNeedDrawOnly = false;
    }

    private void sendNewCursors(float leftCursor, float rightCursor) {
        if (_rectListener != null) {
            _rectListener.onRectCursorsWasChanged(leftCursor, rightCursor);
        }
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.addObserver();
        // first init
        this.setCursors(MAX_LEFT_CURSOR_VALUE, MAX_RIGHT_CURSOR);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.deleteObserver();
    }

    private int getCurrentBackgrColor() {
        return ThemeManager.getColor(ThemeManager.key_rectBackColor);
    }

    @Override
    public void update(Observable o, Object arg) {
        if ((int) arg == ObserverManager.KEY_OBSERVER_THEME_UPDATED) {
            int newBackColor = this.getCurrentBackgrColor();

            // TITLE CHANGE COLOR
            if (_oldBackColor != newBackColor) {
                ValueAnimator textRGBAnim = AndroidUtilites.getArgbAnimator(
                        _oldBackColor,
                        newBackColor,
                        animation -> {
                            _backPaint.setColor((int) animation.getAnimatedValue());
                            _isNeedDrawOnly = true;
                            invalidate();
                        });
                textRGBAnim.start();
                _oldBackColor = newBackColor;
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
}
