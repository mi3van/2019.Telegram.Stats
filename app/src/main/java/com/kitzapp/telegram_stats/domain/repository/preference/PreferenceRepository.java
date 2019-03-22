package com.kitzapp.telegram_stats.domain.repository.preference;

/**
 * Preference layer, save data
 */
public interface PreferenceRepository {

    int getCurrentTheme();

    void saveNewTheme(int theme);

}
