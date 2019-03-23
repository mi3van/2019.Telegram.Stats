package com.kitzapp.telegram_stats.Application;

import android.app.Application;
import android.content.Context;

import com.kitzapp.telegram_stats.domain.repository.preference.TPreferenceRepository;
import com.kitzapp.telegram_stats.Application.AppManagers.ObserverManager;

public class AndroidApp extends Application {

    public static volatile Context applicationContext;
    public static volatile TPreferenceRepository mainRepository;
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

        mainRepository = new TPreferenceRepository(applicationContext);
        observerManager = new ObserverManager();
    }

}
