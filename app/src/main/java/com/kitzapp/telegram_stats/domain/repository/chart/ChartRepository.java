package com.kitzapp.telegram_stats.domain.repository.chart;

import com.kitzapp.telegram_stats.domain.model.ChartsList;

/**
 * A repository for charts
 */
public interface ChartRepository {

    ChartsList getCharts(String fileName) throws Exception;

}
