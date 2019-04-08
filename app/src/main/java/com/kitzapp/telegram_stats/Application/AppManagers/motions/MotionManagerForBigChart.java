package com.kitzapp.telegram_stats.Application.AppManagers.motions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import com.kitzapp.telegram_stats.Application.AppManagers.ObserverManager;

/**
 * Created by Ivan Kuzmin on 29.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class MotionManagerForBigChart extends BaseMotionManager  {

    public interface OnMyTouchListener {
        void onXwasDetected(float newX);
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
                _myTouchListener.onXwasDetected(event.getX());
                break;
        }
        return true;
    }

    @Override
    protected byte getKeyNotifyObservers() {
        return ObserverManager.KEY_OBSERVER_BIG_CHART_PROHIBITED_TO_SCROLL;
    }
}
