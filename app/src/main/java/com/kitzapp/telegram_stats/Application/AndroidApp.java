package com.kitzapp.telegram_stats.Application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import com.kitzapp.telegram_stats.Application.AppManagers.ObserverManager;
import com.kitzapp.telegram_stats.Application.AppManagers.PreferenceManagerT;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;

public class AndroidApp extends Application {
    public static volatile Resources resources;
    public static volatile PreferenceManagerT mainRepository;
    public static volatile ObserverManager observerManager;

    private volatile Activity _currentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        resources = getResources();
        Context context = getBaseContext();

        observerManager = new ObserverManager();
        mainRepository = new PreferenceManagerT(context);
        ThemeManager.initTextFonts(context);
    }

    public Activity getCurrentActivity() {
        return _currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this._currentActivity = currentActivity;
    }
}
