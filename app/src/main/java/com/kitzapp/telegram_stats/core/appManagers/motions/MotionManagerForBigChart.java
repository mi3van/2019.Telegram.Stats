package com.kitzapp.telegram_stats.core.appManagers.motions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import com.kitzapp.telegram_stats.core.appManagers.ObserverManager;

/**
 * Created by Ivan Kuzmin on 29.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class MotionManagerForBigChart extends BaseMotionManager  {

    boolean isMiniatureViewLocked = false;

    public interface OnMyTouchListener {
        void onXTouchWasDetected(float newX);

        void onMiniatureViewIsLocked(boolean isLocked);
    }

    private OnMyTouchListener _myTouchListener;

    public MotionManagerForBigChart(Context context, View motionView, OnMyTouchListener myTouchListener) {
        super(context, motionView);
        this._myTouchListener = myTouchListener;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        super.onTouch(v, event);
        if (_myTouchListener == null) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                _myTouchListener.onXTouchWasDetected(event.getX());
                this.onMiniatureViewIsLocked(true);
                break;
        }
        return true;
    }

    @Override
    protected void motionCancel() {
        this.onMiniatureViewIsLocked(false);
    }

    private void onMiniatureViewIsLocked(boolean isLocked) {
        if (_myTouchListener != null && isMiniatureViewLocked != isLocked) {
            isMiniatureViewLocked = isLocked;
            _myTouchListener.onMiniatureViewIsLocked(isLocked);
        }
    }
}
