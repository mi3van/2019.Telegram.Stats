package com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;
import com.kitzapp.telegram_stats.common.AndroidUtilites;

/**
 * Created by Ivan Kuzmin on 2019-03-26;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

class ViewRectSelect extends View {
    private final float MIN_LEFT_CURSOR_VALUE = 0f;
    private final float MAX_LEFT_CURSOR_VALUE = 0.7f;
    private final float MIN_RIGHT_CURSOR = 0.3f;
    private final float MAX_RIGHT_CURSOR = 1f;

    interface RectListener {
        void onRectCursorsWasChanged(float leftCursor, float rightCursor);
    }

    private Paint _verticalPaint;
    private int _halfWidthVPaint;
    private int _vertCoeff = 1;

    private float _leftCursor;  // 0 - 0.7 % is available
    private float _rightCursor; // 0.3 - 1 % is available

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

    private void init() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(layoutParams);

        int colorPaint = ThemeManager.getColor(ThemeManager.key_rectSelectColor);

        _verticalPaint = AndroidUtilites.getPaint(colorPaint, ThemeManager.CHART_RECT_SELECT_WIDTH_PX);

        int _widthVPaint = (int) _verticalPaint.getStrokeWidth();
        _halfWidthVPaint = _widthVPaint >> 1;

        setOnClickListener(l -> this.setCursors(_leftCursor - 0.01f, _rightCursor));
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
        if (rightCursor - leftCursor >= 0.3f) {
            _leftCursor = Math.max(leftCursor, MIN_LEFT_CURSOR_VALUE);
            if (_leftCursor > MAX_LEFT_CURSOR_VALUE) {
                _leftCursor = MAX_LEFT_CURSOR_VALUE;
            }
            _rightCursor = Math.max(rightCursor, MIN_RIGHT_CURSOR);
            if (_rightCursor > MAX_RIGHT_CURSOR) {
                _rightCursor = MAX_RIGHT_CURSOR;
            }

            invalidate();

            this.sendNewCursors(_leftCursor, _rightCursor);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // first init
        this.setCursors(MAX_LEFT_CURSOR_VALUE, MAX_RIGHT_CURSOR);
    }

    private void drawRect(Canvas canvas) {
        int canvasHeight = canvas.getHeight();                  int canvasWidth = canvas.getWidth();
        int leftCursorInPX = (int) (canvasWidth * _leftCursor); int rightCursorInPX = (int) (canvasWidth * _rightCursor);
        int leftCurrentV = _halfWidthVPaint + leftCursorInPX;   int currentRightV = rightCursorInPX - _halfWidthVPaint;

        Rect rect = new Rect();
        rect.top = -_vertCoeff;
        rect.bottom = canvasHeight + _vertCoeff;
        rect.left = leftCurrentV;   rect.right = currentRightV;
        canvas.drawRect(rect, _verticalPaint);
    }

    private void sendNewCursors(float leftCursor, float rightCursor) {
        if (_rectListener != null) {
            _rectListener.onRectCursorsWasChanged(leftCursor, rightCursor);
        }
    }
}
