package com.kitzapp.telegram_stats.Application.AppManagers;

import android.content.Context;
import android.content.SharedPreferences;

import com.kitzapp.telegram_stats.common.SettingsKeys;

/**
 * Created by Ivan Kuzmin on 2019-03-20.
 * Copyright © 2019 Example. All rights reserved.
 */

interface PreferenceMInterface {
    int getCurrentTheme();
    void saveNewTheme(int theme);
}

public class PreferenceManagerT implements PreferenceMInterface {

    private SharedPreferences _preference;

    public PreferenceManagerT(Context context) {
        _preference = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public int getCurrentTheme() {
        return _preference.getInt(SettingsKeys.APP_THEME, ThemeManager.LIGHT);
    }

    @Override
    public void saveNewTheme(int theme) {
        _preference.edit().putInt(SettingsKeys.APP_THEME, theme).apply();
    }
}
