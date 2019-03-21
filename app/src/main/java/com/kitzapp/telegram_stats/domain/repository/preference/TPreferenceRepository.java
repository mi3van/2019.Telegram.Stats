package com.kitzapp.telegram_stats.domain.repository.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kitzapp.telegram_stats.R;
import com.kitzapp.telegram_stats.common.SettingsKeys;

/**
 * Created by Ivan Kuzmin on 2019-03-20.
 * Copyright © 2019 Example. All rights reserved.
 */

public class TPreferenceRepository implements PreferenceRepository {
    public static int THEME_LIGHT = R.style.AppTheme_Light;
    public static int THEME_DARK = R.style.AppTheme_Dark;

    private SharedPreferences _preference;

    public TPreferenceRepository(Context context) {
        _preference = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public int getCurrentTheme() {
        boolean themeIsDark = this.getThemeIsDark();
        int currentTheme = themeIsDark ? THEME_DARK : THEME_LIGHT;
        return currentTheme;
    }

    @Override
    public int changeThemeAndGetNew() {
        boolean isDark = this.getThemeIsDark();
        boolean newThemeIsDark = !isDark;
        this.saveThemeIsDark(newThemeIsDark);

        return newThemeIsDark ? THEME_DARK : THEME_LIGHT;
    }

    private boolean getThemeIsDark() {
        return _preference.getBoolean(SettingsKeys.APP_THEME_IS_DARK, false);
    }

    private void saveThemeIsDark(boolean isDark) {
        _preference.edit().putBoolean(SettingsKeys.APP_THEME_IS_DARK, isDark).apply();
    }
}
