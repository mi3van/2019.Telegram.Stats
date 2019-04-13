package com.kitzapp.telegram_stats.customViews.charts.base.impl;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Ivan Kuzmin on 13.04.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class ChartThreadHardGrraphics extends Thread {
    private final int REDRAW_TIME = 10; // Update frequency  (ms)

    private final SurfaceHolder _threadSurfaceHolder;
    private ChartSurfaceView _surfaceView;
    private boolean _isThreadRun = false;
    private long _prevDrawTime;

    public ChartThreadHardGrraphics(SurfaceHolder surfaceHolder,
                                    ChartSurfaceView surfaceView) {
        _threadSurfaceHolder = surfaceHolder;
        _surfaceView = surfaceView;
    }

    @Override
    public void run() {
        Canvas canvas;

        while (_isThreadRun) {
            long curTime = this.getTime();
            long elapsedTime = curTime - _prevDrawTime;
            if (elapsedTime < REDRAW_TIME)
                continue;

            canvas = null;
            try {
                canvas = _threadSurfaceHolder.lockCanvas();
                synchronized (_threadSurfaceHolder) {
                    _surfaceView.draw(canvas);
                }
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            }
            finally {
                if (canvas != null)
                    _threadSurfaceHolder.unlockCanvasAndPost(canvas);
            }

            _prevDrawTime = curTime;
        }
    }

    void setRunning(boolean whyAreYouRunning) {
        _isThreadRun = whyAreYouRunning;
        _prevDrawTime = this.getTime();
    }

    private long getTime() {
        return System.currentTimeMillis();
    }
}