package com.kitzapp.telegram_stats.Application;

import android.app.Application;
import android.content.Context;

import com.kitzapp.telegram_stats.Application.AppManagers.PreferenceManagerT;
import com.kitzapp.telegram_stats.Application.AppManagers.ObserverManager;

public class AndroidApp extends Application {

    public static volatile Context applicationContext;
    public static volatile PreferenceManagerT mainRepository;
    public static volatile ObserverManager observerManager;

    @Override
    public void onCreate() {
        try {
            applicationContext = getApplicationContext();
        } catch (Throwable ignore) {
        }

        super.onCreate();

        if (applicationContext == null) {
            applicationContext = getApplicationContext();
        }

        mainRepository = new PreferenceManagerT(applicationContext);
        observerManager = new ObserverManager();
    }

}
