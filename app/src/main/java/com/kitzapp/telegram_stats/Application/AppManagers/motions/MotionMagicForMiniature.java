package com.kitzapp.telegram_stats.Application.AppManagers.motions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import com.kitzapp.telegram_stats.Application.AppManagers.ObserverManager;

/**
 * Created by Ivan Kuzmin on 27.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class MotionMagicForMiniature extends BaseMotionManager {
    private final byte MOTION_UNDEFINED = -2;
    private final byte MOTION_CENTER = 0;
    private final byte MOTION_LEFT_CURSOR = 1;
    private final byte MOTION_RIGHT_CURSOR = 2;

    private boolean isMiniatureLocked = false;
    private final float COEF_CURSOR_PX = 0.05f;
    private final float _maxCursorWidth;
    private byte _currentMotion = MOTION_UNDEFINED;

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

    public MotionMagicForMiniature(Context context, View motionView, MotionListener motionListener, float maxCursorWidth) {
        super(context, motionView);
        _motionListener = motionListener;
        _maxCursorWidth = maxCursorWidth;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!isMiniatureLocked) {
            super.onTouch(v, event);
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
                        this.wasMove(event.getX() / _motionListener.getCanvasWidth());
                    }
                    break;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void motionCancel() {
        if (_currentMotion != MOTION_UNDEFINED) {
            _currentMotion = MOTION_UNDEFINED;
        }
    }

    @Override
    void setIsProhibitedScrollToObservers(boolean isProhibitedScroll) {
        if (isProhibitedScroll && _currentMotion == MOTION_UNDEFINED) {
            return;
        }
        super.setIsProhibitedScrollToObservers(isProhibitedScroll);
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
            case MOTION_LEFT_CURSOR:
                leftCursor += difference;
                float limitToLeftCursor = rightCursor - _maxCursorWidth;
                if (leftCursor > limitToLeftCursor) {
                    leftCursor = limitToLeftCursor;
                }
                _motionListener.onMoveLeftSide(leftCursor);
                break;
            case MOTION_RIGHT_CURSOR:
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
                motion = MOTION_LEFT_CURSOR;
            } else if (persentX > rightCursor - COEF_CURSOR_PX) {
                motion = MOTION_RIGHT_CURSOR;
            } else {
                motion = MOTION_CENTER;
            }
        }
        return motion;
    }

    public void setMiniatureIsLocked(boolean miniatureIsLocked) {
        if (miniatureIsLocked != isMiniatureLocked) {
            isMiniatureLocked = miniatureIsLocked;
            _isProhibitedScroll = true;
        }
    }

    @Override
    protected byte getKeyNotifyObservers() {
        return ObserverManager.KEY_OBSERVER_PROHIBITED_SCROLL;
    }
}
