package com.kitzapp.telegram_stats.customViews.charts.base.impl;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ChartSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private ChartThreadHardGrraphics _chartThread;

    private ChartSurfaceViewInterface.Listener _listener;

    public ChartSurfaceView(Context context) {
        super(context);
        this.init();
    }

    public ChartSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public ChartSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    public void setDelegate(ChartSurfaceViewInterface.Listener listener) {
        _listener = listener;
    }

    private void init() {
        getHolder().addCallback(this);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (_listener != null) {
            _listener.onSurfaceDraw(canvas);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        _chartThread = new ChartThreadHardGrraphics(getHolder(), this);
        _chartThread.setRunning(true);
        _chartThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        _chartThread.setRunning(false);
        while (retry) {
            try {
                _chartThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }
}
