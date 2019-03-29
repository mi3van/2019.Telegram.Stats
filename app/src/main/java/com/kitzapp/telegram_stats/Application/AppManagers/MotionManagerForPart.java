package com.kitzapp.telegram_stats.Application.AppManagers;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import java.util.Observable;

/**
 * Created by Ivan Kuzmin on 29.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class MotionManagerForPart extends Observable implements View.OnTouchListener {
    public interface OnMyTouchListener {
        void onXwasDetected(float newX);
    }

//    private boolean isAllowTouchEventForScrollView;

    private OnMyTouchListener _myTouchListener;
//    private Context _context;

    public MotionManagerForPart(Context context, View motionView, OnMyTouchListener myTouchListener) {
        this._myTouchListener = myTouchListener;
//        _context = context;
        motionView.setOnTouchListener(this);

        this.addObserver();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (_myTouchListener == null) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.updateIsAllowTouchAndNotifyObservers(true);
                _myTouchListener.onXwasDetected(event.getX());
                break;
            case MotionEvent.ACTION_MOVE:
                this.updateIsAllowTouchAndNotifyObservers(true);
                _myTouchListener.onXwasDetected(event.getX());
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                this.updateIsAllowTouchAndNotifyObservers(false);
                break;
        }
        return true;
    }

    public void updateIsAllowTouchAndNotifyObservers(boolean isAllowTouchEventForScrollView){
//        this.isAllowTouchEventForScrollView = isAllowTouchEventForScrollView;
//        this.setChanged();
//        this.notifyObservers(ObserverManager.KEY_OBSERVER_ALLOW_TOUCH_SCROLLVIEW_FOR_PART);
    }

    public boolean getIsAllowTouchEventForScrollView() {
        return true;
//        return isAllowTouchEventForScrollView;
    }

    public void deattachView() {
        this.deleteObserver();
    }

    private void addObserver() {
//        if (_context.getApplicationContext() instanceof AndroidApp) {
//            Activity currentActivity = ((AndroidApp) _context.getApplicationContext()).getCurrentActivity();
//            if (currentActivity instanceof ChartActivity) {
//                addObserver((ChartActivity) currentActivity);
//            }
//        }
    }

    private void deleteObserver() {
//        if (_context.getApplicationContext() instanceof AndroidApp) {
//            Activity currentActivity = ((AndroidApp) _context.getApplicationContext()).getCurrentActivity();
//            if (currentActivity instanceof ChartActivity) {
//                deleteObserver((ChartActivity) currentActivity);
//            }
//        }
    }
}
