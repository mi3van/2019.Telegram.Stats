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
import com.kitzapp.telegram_stats.Application.AppManagers.MotionMagic;
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

class ViewRectSelect extends View implements TViewObserver, MotionMagic.MotionListener {
    private final float MIN_LEFT_CURSOR_VALUE = 0f;
    private final float MAX_LEFT_CURSOR_VALUE = 0.7f;
    private final float MIN_RIGHT_CURSOR = 0.3f;
    private final float MAX_RIGHT_CURSOR = 1f;
    private MotionMagic _motionMagic;

    interface RectListener {
        void onRectCursorsWasChanged(float leftCursor, float rightCursor);
    }

    private Rect _centerRect = new Rect();
    private Rect _rectLeftBack = new Rect();
    private Rect _rectRightBack = new Rect();
    private boolean _isRightBackroundDraw;
    private boolean _isLeftBackgroundDraw;

    private int _oldBackColor;
    private Paint _backPaint;
    private Paint _verticalPaint;
    private int _halfWidthVPaint;

    private float _leftCursor;  // 0 - 0.7 % is available
    private float _rightCursor; // 0.3 - 1 % is available
    private int _canvasWidth;

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

        // LAYOUT PARAMS
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(layoutParams);

        // PAINTS AND COLORS
        int colorPaintRect = ThemeManager.getColor(ThemeManager.key_rectSelectColor);
        _verticalPaint = AndroidUtilites.getPaint(colorPaintRect, ThemeManager.CHART_RECT_SELECT_WIDTH_PX);

        _oldBackColor = this.getCurrentBackgrColor();
        _backPaint = AndroidUtilites.getPaintFill(_oldBackColor);

        int _widthVPaint = (int) _verticalPaint.getStrokeWidth();
        _halfWidthVPaint = _widthVPaint >> 1;

        _motionMagic = new MotionMagic(getContext(), this, this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.drawRect(canvas);
    }

    private void drawRect(Canvas canvas) {
        if (canvas.getWidth() != _canvasWidth) {
            _canvasWidth = canvas.getWidth();

            this.initRectangles(canvas.getHeight());
            this.recalculateCursorsAndDraw();
            return;
        }
        // DRAW CENTER RECT
        canvas.drawRect(_centerRect, _verticalPaint);
        // DRAW LEFT RECT
        if (_isLeftBackgroundDraw) {
            canvas.drawRect(_rectLeftBack, _backPaint);
        }
        // DRAW RIGHT RECT
        if (_isRightBackroundDraw) {
            canvas.drawRect(_rectRightBack, _backPaint);
        }
    }

    private void initRectangles(int heightCanvas) {
        int _vertCoeff = 1;

//            CENTER RECT SETUP
        _centerRect.top = -_vertCoeff;
        _centerRect.bottom = heightCanvas + _vertCoeff;

//            INIT LEFT BACKGR RECT
        _rectLeftBack.top = 0;
        _rectLeftBack.bottom = heightCanvas;
        _rectLeftBack.left = 0;

//            INIT RIGHT BACKGR RECT
        _rectRightBack.top = 0;
        _rectRightBack.bottom = heightCanvas;
        _rectRightBack.right = _canvasWidth;
    }

    @Override
    public void onMoveLeftSide(float newLeftCursor) {
        this.setLeftCursorAndDraw(newLeftCursor);
    }

    @Override
    public void onMoveRightSide(float newRightCursor) {
        this.setRightCursorAndDraw(newRightCursor);
    }

    @Override
    public void onMoveCenter(float newLeftCursor, float newRightCursor) {
        this.setCursorsAndDraw(newLeftCursor, newRightCursor);
    }

    private void recalculateCursorsAndDraw() {
        this.setCursors(_leftCursor, _rightCursor, true, true);
    }

    private void setCursorsAndDraw(float leftCursor, float rightCursor) {
        this.setCursors(leftCursor, rightCursor, false, true);
    }

    private void setCursors(float leftCursor, float rightCursor, boolean calculateAnyway, boolean needInvalidate) {
        this.setLeftCursor(leftCursor, calculateAnyway, false);

        this.setRightCursor(rightCursor, calculateAnyway, needInvalidate);
    }

    private void setLeftCursorAndDraw(float leftCursor) {
        this.setLeftCursor(leftCursor, false, true);
    }

    private void setRightCursorAndDraw(float rightCursor) {
        this.setRightCursor(rightCursor, false, true);
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
        this.setCursorsFirstInit();
    }

    private void setCursorsFirstInit() {
        _leftCursor = MAX_LEFT_CURSOR_VALUE;
        _rightCursor = MAX_RIGHT_CURSOR;
    }

    private void setLeftCursor(float leftCursor, boolean calculateAnyway, boolean needInvalidate) {
//        boolean isCursorAvailWidth = (_rightCursor - leftCursor >= MAX_CURSORS_WIDTH); // CHECKED IN MOTION MAGIC
//        if (isCursorAvailWidth) {
            boolean leftAvail = (leftCursor != _leftCursor) || calculateAnyway;
            if (leftAvail) {

                // SETUP LEFT CURSOR
                _leftCursor = Math.max(leftCursor, MIN_LEFT_CURSOR_VALUE);
                if (_leftCursor > MAX_LEFT_CURSOR_VALUE) {
                    _leftCursor = MAX_LEFT_CURSOR_VALUE;
                }
                // CONFIGURE VALUES
                int leftCursorInPX = (int) (_canvasWidth * _leftCursor);
                int leftCurrentV = _halfWidthVPaint + leftCursorInPX;
                _centerRect.left = leftCurrentV;

                // CONFIGURE LEFT BACKGR
                _isLeftBackgroundDraw = _centerRect.left - _halfWidthVPaint > 0;
                if (_isLeftBackgroundDraw) {
                    _rectLeftBack.right = leftCurrentV - _halfWidthVPaint;
                }

                if (needInvalidate) {
                    this.sendNewCursors(_leftCursor, _rightCursor);
                    invalidate();
                }
            }
//        }
    }

    private void setRightCursor(float rightCursor, boolean calculateAnyway, boolean needInvalidate) {
//        boolean isCursorAvailWidth = (rightCursor - _leftCursor >= MAX_CURSORS_WIDTH); // CHECKED IN MOTION MAGIC
//        if (isCursorAvailWidth) {
            boolean rightAvail = (rightCursor != _rightCursor) || calculateAnyway;
            if (rightAvail) {

                // SETUP RIGHT CURSOR
                _rightCursor = Math.max(rightCursor, MIN_RIGHT_CURSOR);
                if (_rightCursor > MAX_RIGHT_CURSOR) {
                    _rightCursor = MAX_RIGHT_CURSOR;
                }
                // CONFIGURE VALUES
                int rightCursorInPX = (int) (_canvasWidth * _rightCursor);
                int currentRightV = rightCursorInPX - _halfWidthVPaint;
                _centerRect.right = currentRightV;

                // CONFIGURE RIGHT BACKGR
                _isRightBackroundDraw = _centerRect.right + _halfWidthVPaint < _canvasWidth;
                if (_isRightBackroundDraw) {
                    _rectRightBack.left = currentRightV + _halfWidthVPaint;
                }

                if (needInvalidate) {
                    this.sendNewCursors(_leftCursor, _rightCursor);
                    invalidate();
                }
            }
//        }
    }

    @Override
    protected void onDetachedFromWindow() {
        _motionMagic.deattachView();
        this.deleteObserver();
        super.onDetachedFromWindow();
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

    @Override
    public float getLeftCursor() {
        return _leftCursor;
    }

    @Override
    public float getRightCursor() {
        return _rightCursor;
    }

    @Override
    public float getCanvasWidth() {
        return _canvasWidth;
    }
}
