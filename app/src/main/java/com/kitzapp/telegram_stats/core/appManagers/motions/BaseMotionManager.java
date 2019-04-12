package com.kitzapp.telegram_stats.core.appManagers.motions;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import com.kitzapp.telegram_stats.AndroidApp;
import com.kitzapp.telegram_stats.core.appManagers.ObserverManager;
import com.kitzapp.telegram_stats.core.mainChart.TChartView;

import java.util.Observable;

/**
 * Created by Ivan Kuzmin on 2019-04-08;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public abstract class BaseMotionManager extends Observable implements View.OnTouchListener {
    private final byte MOTION_BASE_UNDEFINED = -2;
    private final byte MOTION_IS_HORIZONTAL = -1;
    private final float COEF_CURSOR_PX = 20f;
    private float _firstPressX = -1f;

    private Context _context;

    private byte _baseMotion = MOTION_BASE_UNDEFINED;
    boolean _isProhibitedScroll;

    BaseMotionManager(Context _context, View motionView) {
        this._context = _context;

        if (motionView != null) {
            motionView.setOnTouchListener(this);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (_baseMotion == MOTION_BASE_UNDEFINED) {
                    _firstPressX = event.getX();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (_baseMotion == MOTION_BASE_UNDEFINED) {
                    float currentX = event.getX();
                    float difference = Math.abs(_firstPressX - currentX);
                    if (difference > COEF_CURSOR_PX) {
                        _baseMotion = MOTION_IS_HORIZONTAL;
                        this.setIsProhibitedScrollToObservers(true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                this.motionCancel();
                if (_baseMotion != MOTION_BASE_UNDEFINED) {
                    _baseMotion = MOTION_BASE_UNDEFINED;
                    this.setIsProhibitedScrollToObservers(false);
                }
                break;
        }
        return true;
    }

    void setIsProhibitedScrollToObservers(boolean isProhibitedScroll) {
        this._isProhibitedScroll = isProhibitedScroll;
        this.setChanged();
        this.notifyObservers(this.getKeyNotifyObservers());
    }

    private byte getKeyNotifyObservers() {
        return ObserverManager.KEY_OBSERVER_PROHIBITED_SCROLL;
    }

    protected abstract void motionCancel();

    public boolean getIsProhibitedScroll() {
        return _isProhibitedScroll;
    }

    public void attachView() {
        this.addObserver();
    }

    public void deattachView() {
        this.deleteObserver();
    }

    private void addObserver() {
        if (_context.getApplicationContext() instanceof AndroidApp) {
            Activity currentActivity = ((AndroidApp) _context.getApplicationContext()).getCurrentActivity();
            if (currentActivity instanceof TChartView) {
                addObserver((TChartView) currentActivity);
            }
        }
    }

    private void deleteObserver() {
        if (_context.getApplicationContext() instanceof AndroidApp) {
            Activity currentActivity = ((AndroidApp) _context.getApplicationContext()).getCurrentActivity();
            if (currentActivity instanceof TChartView) {
                deleteObserver((TChartView) currentActivity);
            }
        }
    }
}
