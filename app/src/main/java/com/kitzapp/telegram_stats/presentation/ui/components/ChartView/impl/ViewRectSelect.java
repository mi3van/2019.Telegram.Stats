package com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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

    private Paint _verticalPaint;
    private Paint _horizontalPaint;
    private int _halfWidthVPaint;
    private int _widthVPaint;
    private int _halfHeightHPaint;

    private float _minLeftCursor = 0f;
    private float _maxLeftCursor = 0.7f;
    private float _leftCursor = _maxLeftCursor;  // 0 - 0.7 % is available

    private float _minRightCursor = 0.3f;
    private float _maxRightCursor = 1f;
    private float _rightCursor = _maxRightCursor; // 0.3 - 1 % is available

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

    private void init() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(layoutParams);

        int colorPaint = ThemeManager.getColor(ThemeManager.key_rectSelectColor);

        _horizontalPaint = AndroidUtilites.getPaint(colorPaint, ThemeManager.CHART_DELIMITER_WIDTH_PX);
        _verticalPaint = AndroidUtilites.getPaint(colorPaint, ThemeManager.CHART_RECT_SELECT_WIDTH_PX);

        _widthVPaint = (int) _verticalPaint.getStrokeWidth();
        _halfWidthVPaint = (_widthVPaint >> 1) - 1;
        _halfHeightHPaint = ((int) _horizontalPaint.getStrokeWidth()) >> 1;
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
            _leftCursor = Math.max(leftCursor, _minLeftCursor);
            if (_leftCursor > _maxLeftCursor) {
                _leftCursor = _maxLeftCursor;
            }
            _rightCursor = Math.max(rightCursor, _minRightCursor);
            if (_rightCursor > _maxRightCursor) {
                _rightCursor = _maxRightCursor;
            }
            invalidate();
        }
    }

    private void drawRect(Canvas canvas) {
        int canvasHeight = canvas.getHeight();                  int canvasWidth = canvas.getWidth();
        int leftCursorInPX = (int) (canvasWidth * _leftCursor); int rightCursorInPX = (int) (canvasWidth * _rightCursor);
        int leftCurrentV = _halfWidthVPaint + leftCursorInPX;   int currentRightV = rightCursorInPX - _halfWidthVPaint;
        int leftCurrentH = _widthVPaint + leftCursorInPX;       int currentRightH = rightCursorInPX - _widthVPaint;
        canvas.drawLine(leftCurrentV, 0, leftCurrentV, canvasHeight, _verticalPaint);
        canvas.drawLine(currentRightV, 0, currentRightV, canvasHeight, _verticalPaint);
        canvas.drawLine(leftCurrentH, _halfHeightHPaint, currentRightH, _halfHeightHPaint, _horizontalPaint);
        int bottomH = canvasHeight - _halfHeightHPaint;
        canvas.drawLine(leftCurrentH, bottomH, currentRightH, bottomH, _horizontalPaint);
    }
}
