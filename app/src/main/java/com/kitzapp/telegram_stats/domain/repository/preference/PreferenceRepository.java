package com.kitzapp.telegram_stats.domain.repository.preference;

/**
 * A sample repository with CRUD operations on a model.
 */
public interface PreferenceRepository {

    int getCurrentTheme();

    void changeCurrentTheme();

}
