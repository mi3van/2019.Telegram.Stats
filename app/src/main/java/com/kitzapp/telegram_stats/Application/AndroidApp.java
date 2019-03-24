package com.kitzapp.telegram_stats.Application;

import android.app.Application;
import android.content.Context;

import android.content.res.Resources;
import com.kitzapp.telegram_stats.Application.AppManagers.PreferenceManagerT;
import com.kitzapp.telegram_stats.Application.AppManagers.ObserverManager;

public class AndroidApp extends Application {

    public static volatile Context context;
    public static volatile Resources resources;
    public static volatile PreferenceManagerT mainRepository;
    public static volatile ObserverManager observerManager;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        resources = getResources();
        mainRepository = new PreferenceManagerT(context);
        observerManager = new ObserverManager();
    }

}
