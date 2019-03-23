package com.kitzapp.telegram_stats.Application.AppManagers;

/**
 * Preference layer, save data
 */
public interface PreferenceMInterface {

    int getCurrentTheme();

    void saveNewTheme(int theme);

}
