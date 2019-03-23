package com.kitzapp.telegram_stats.domain.repository.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kitzapp.telegram_stats.common.SettingsKeys;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;

/**
 * Created by Ivan Kuzmin on 2019-03-20.
 * Copyright © 2019 Example. All rights reserved.
 */

public class TPreferenceRepository implements PreferenceRepository {

    private SharedPreferences _preference;

    public TPreferenceRepository(Context context) {
        _preference = PreferenceManager.getDefaultSharedPreferences(context);
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
