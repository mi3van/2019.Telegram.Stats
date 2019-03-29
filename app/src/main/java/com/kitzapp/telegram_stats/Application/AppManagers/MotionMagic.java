package com.kitzapp.telegram_stats.Application.AppManagers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import com.kitzapp.telegram_stats.Application.AndroidApp;
import com.kitzapp.telegram_stats.presentation.ui.activities.ChartActivity;

import java.util.Observable;

/**
 * Created by Ivan Kuzmin on 27.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class MotionMagic extends Observable implements View.OnTouchListener {
    private final byte MOTION_UNDEFINED = -2;
    private final byte MOTION_CENTER = 0;
    private final byte MOTION_LEFT = 2;
    private final byte MOTION_RIGHT = 4;

    private final float COEF_CURSOR_PX = 0.05f;
    private boolean isAllowTouchEventForScrollView;
    private final float _maxCursorWidth;

    public interface MotionListener {
        void onMoveLeftSide(float newLeftCursor);

        void onMoveRightSide(float newRightCursor);

        void onMoveCenter(float newLeftCursor, float newRightCursor);

        float getLeftCursor();

        float getRightCursor();

        float getCanvasWidth();
    }

    private float _oldPersentX;
    private float _widthViewForCenter;

    private MotionListener _motionListener;
    private byte _currentMotion = MOTION_UNDEFINED;
    private Context _context;

    public MotionMagic(Context context, View motionView, MotionListener motionListener, float maxCursorWidth) {
        _motionListener = motionListener;
        _context = context;
        _maxCursorWidth = maxCursorWidth;

        this.addObserver();

        motionView.setOnTouchListener(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (_motionListener == null) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (_currentMotion == MOTION_UNDEFINED) {
                    _oldPersentX = event.getX() / _motionListener.getCanvasWidth();
                    _currentMotion = this.getNewMotion(_oldPersentX);
                    _widthViewForCenter = _motionListener.getRightCursor() - _motionListener.getLeftCursor();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (_currentMotion != MOTION_UNDEFINED) {
                    this.updateIsAllowTouchAndNotifyObservers(true);
                    this.wasMove(event.getX() / _motionListener.getCanvasWidth());
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (_currentMotion != MOTION_UNDEFINED) {
                    this.updateIsAllowTouchAndNotifyObservers(false);
                    _currentMotion = MOTION_UNDEFINED;
                }
                break;
        }
        return true;
    }

    private void wasMove(float persentX) {
        if (_motionListener == null) {
            return;
        }
        float leftCursor = _motionListener.getLeftCursor();
        float rightCursor = _motionListener.getRightCursor();
        float difference = persentX - _oldPersentX;
        // CHECK RIGHT AND LEFT CANVAS LIMITS
        if (leftCursor + difference < 0) {
            leftCursor = 0;
        }
        float canvasWidthPersent = 1f;
        if (rightCursor + difference > canvasWidthPersent) {
            rightCursor = canvasWidthPersent;
        }

        switch (_currentMotion) {
            case MOTION_LEFT:
                leftCursor += difference;
                float limitToLeftCursor = rightCursor - _maxCursorWidth;
                if (leftCursor > limitToLeftCursor) {
                    leftCursor = limitToLeftCursor;
                }
                _motionListener.onMoveLeftSide(leftCursor);
                break;
            case MOTION_RIGHT:
                rightCursor += difference;
                float limitToRightCursor = leftCursor + _maxCursorWidth;
                if (rightCursor < limitToRightCursor) {
                    rightCursor = limitToRightCursor;
                }
                _motionListener.onMoveRightSide(rightCursor);
                break;
            case MOTION_CENTER:
                leftCursor += difference;
                rightCursor += difference;
                float leftCursorRightLimit = canvasWidthPersent - _widthViewForCenter;
                if (leftCursor > leftCursorRightLimit) {
                    leftCursor = leftCursorRightLimit;
                }
                if (rightCursor < _widthViewForCenter) {
                    rightCursor = _widthViewForCenter;
                }
                _motionListener.onMoveCenter(leftCursor, rightCursor);
                break;
        }
        _oldPersentX = persentX;
    }

    private byte getNewMotion(float persentX) {
        byte motion = MOTION_UNDEFINED;
        if (_motionListener == null) {
            return motion;
        }

        float leftCursor = _motionListener.getLeftCursor();
        float rightCursor = _motionListener.getRightCursor();
        if (persentX > leftCursor - COEF_CURSOR_PX && persentX < rightCursor + COEF_CURSOR_PX) {
            if (persentX < leftCursor + COEF_CURSOR_PX) {
                motion = MOTION_LEFT;
            } else if (persentX > rightCursor - COEF_CURSOR_PX) {
                motion = MOTION_RIGHT;
            } else {
                motion = MOTION_CENTER;
            }
        }
        return motion;
    }

    private void updateIsAllowTouchAndNotifyObservers(boolean isAllowTouchEventForScrollView) {
        this.isAllowTouchEventForScrollView = isAllowTouchEventForScrollView;
        this.setChanged();
        this.notifyObservers(ObserverManager.KEY_OBSERVER_ALLOW_TOUCH_SCROLLVIEW_FOR_RECT_SELECT);
    }

    public boolean getIsAllowTouchEventForScrollView() {
        return isAllowTouchEventForScrollView;
    }

    public void deattachView() {
        this.deleteObserver();
    }

    private void addObserver() {
        if (_context.getApplicationContext() instanceof AndroidApp) {
            Activity currentActivity = ((AndroidApp) _context.getApplicationContext()).getCurrentActivity();
            if (currentActivity instanceof ChartActivity) {
                addObserver((ChartActivity) currentActivity);
            }
        }
    }

    private void deleteObserver() {
        if (_context.getApplicationContext() instanceof AndroidApp) {
            Activity currentActivity = ((AndroidApp) _context.getApplicationContext()).getCurrentActivity();
            if (currentActivity instanceof ChartActivity) {
                deleteObserver((ChartActivity) currentActivity);
            }
        }
    }
}
