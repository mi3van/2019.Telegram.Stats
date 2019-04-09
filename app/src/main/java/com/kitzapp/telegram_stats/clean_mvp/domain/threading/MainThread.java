package com.kitzapp.telegram_stats.clean_mvp.domain.threading;

import android.os.Handler;
import android.os.Looper;


/**
 * This class makes sure that the runnable we provide will be run on the main UI thread.
 */
public class MainThread implements Thread {

    private static Thread sMainThread;

    private Handler mHandler;

    private MainThread() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(Runnable runnable) {
        mHandler.post(runnable);
    }

    public static Thread getInstance() {
        if (sMainThread == null) {
            sMainThread = new MainThread();
        }

        return sMainThread;
    }
}
