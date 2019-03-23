package com.kitzapp.telegram_stats.domain.model.chart;

import com.kitzapp.telegram_stats.domain.model.chart.impl.AxisX;
import com.kitzapp.telegram_stats.domain.model.chart.impl.Line;

import java.util.HashMap;

public class Chart {
    public static final String KEY_COLORS_CHART = "colors";
    public static final String KEY_COLUMNS_CHART = "columns";
    public static final String KEY_TYPES_CHART = "types";
    public static final String KEY_NAMES_CHART = "names";

    private HashMap<String, Line> lines;
    private AxisX axisX;

    public Chart(AxisX axisX, HashMap<String, Line> lines) {
        this.axisX = axisX;
        this.lines = lines;
    }

    public Line findLine(String key) {
        return lines.get(key);
    }

    public AxisX getAxisX() {
        return axisX;
    }

    public int getCountDots() {
        return axisX.getCountDots();
    }
}
