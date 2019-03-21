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
    public static int THEME_LIGHT = R.style.AppTheme;
    public static int THEME_DARK = R.style.AppThemeDark;

    private SharedPreferences _preference;

    public TPreferenceRepository(Context context) {
        _preference = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public int getCurrentTheme() {
        int currentTheme = this.getThemeIsDark() ? THEME_LIGHT : THEME_DARK;
        return currentTheme;
    }

    @Override
    public void changeCurrentTheme() {
        boolean isDark = this.getThemeIsDark();
        this.saveThemeIsDark(!isDark);
    }

    private boolean getThemeIsDark() {
        return _preference.getBoolean(SettingsKeys.APP_THEME_IS_DARK, false);
    }

    private void saveThemeIsDark(boolean isDark) {
        _preference.edit().putBoolean(SettingsKeys.APP_THEME_IS_DARK, isDark).apply();
    }
}
