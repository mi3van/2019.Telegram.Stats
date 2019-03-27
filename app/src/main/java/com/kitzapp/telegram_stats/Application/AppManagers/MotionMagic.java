package com.kitzapp.telegram_stats.Application.AppManagers;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Ivan Kuzmin on 27.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class MotionMagic implements View.OnTouchListener {
    private final byte MOTION_UNDEFINED = -2;
    private final byte MOTION_CENTER = 0;
    private final byte MOTION_LEFT = 2;
    private final byte MOTION_RIGHT = 4;

    private final float COEF_CURSOR_PX = 15;

    public interface MotionListener {
        void onMoveLeftSide(float newLeftCursor);

        void onMoveRightSide(float newRightCursor);

        void onMoveCenter(float newLeftCursor, float newRightCursor);

        float getLeftCursor();

        float getRightCursor();
    }

    private float _oldX;

    private MotionListener _motionListener;
    private byte _currentMotion = MOTION_UNDEFINED;

    public MotionMagic(View motionView) {
        motionView.setOnTouchListener(this);
//        motionView.set
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (_currentMotion == MOTION_UNDEFINED) {
                    _oldX = event.getX();
                    _currentMotion = this.getNewMotion(_oldX);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                this.wasMove(event.getX());
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                _currentMotion = MOTION_UNDEFINED;
                break;
        }
        return false;
    }

    private void wasMove(float x) {
        if (_motionListener == null) {
            return;
        }
        float leftCursor = _motionListener.getLeftCursor();
        float rightCursor = _motionListener.getRightCursor();
        float differencePX = x - _oldX;

        switch (_currentMotion) {
            case MOTION_LEFT:
//                leftCursor += differencePX;
                _motionListener.onMoveLeftSide(leftCursor);
                break;
            case MOTION_RIGHT:
//                rightCursor += differencePX;
                _motionListener.onMoveRightSide(rightCursor);
                break;
            case MOTION_CENTER:
//                leftCursor += differencePX;
//                rightCursor += differencePX;
                _motionListener.onMoveCenter(leftCursor, rightCursor);
                break;
        }
        _oldX = x;
    }

    private byte getNewMotion(float x) {
        byte motion = MOTION_UNDEFINED;
        if (_motionListener == null) {
            return motion;
        }

        float leftCursor = _motionListener.getLeftCursor();
        float rightCursor = _motionListener.getRightCursor();
        if (x > leftCursor - COEF_CURSOR_PX && x < rightCursor + COEF_CURSOR_PX) {
            if (x < leftCursor + COEF_CURSOR_PX) {
                motion = MOTION_LEFT;
            } else if (x > rightCursor - COEF_CURSOR_PX) {
                motion = MOTION_RIGHT;
            } else {
                motion = MOTION_CENTER;
            }
        }
        return motion;
    }
}
