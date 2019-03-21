package com.kitzapp.telegram_stats.domain.threading;

import android.os.Handler;
import android.os.Looper;


/**
 * This class makes sure that the runnable we provide will be run on the main UI thread.
 */
public class TMainThread implements MainThread {

    private static MainThread sMainThread;

    private Handler mHandler;

    private TMainThread() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(Runnable runnable) {
        mHandler.post(runnable);
    }

    public static MainThread getInstance() {
        if (sMainThread == null) {
            sMainThread = new TMainThread();
        }

        return sMainThread;
    }
}
