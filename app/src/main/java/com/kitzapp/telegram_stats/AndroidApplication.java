package com.kitzapp.telegram_stats;

import android.app.Application;
import android.content.Context;

import com.kitzapp.telegram_stats.domain.repository.preference.TPreferenceRepository;

public class AndroidApplication extends Application {

    public static volatile Context applicationContext;
    public static TPreferenceRepository mainRepository;

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
    }

}
