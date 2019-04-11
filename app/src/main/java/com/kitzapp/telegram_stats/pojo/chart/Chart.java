package com.kitzapp.telegram_stats.pojo.chart;

import com.kitzapp.telegram_stats.pojo.chart.impl.AxisX;
import com.kitzapp.telegram_stats.pojo.chart.impl.Line;

import java.util.HashMap;

interface ChartInterface {
    Line findLine(String key);

    HashMap<String, Line> getLines();

    AxisX getAxisX();

    int getCountDots();

    byte getChartType();
}

public class Chart implements ChartInterface {
    public static final String KEY_COLORS_CHART = "colors";
    public static final String KEY_COLUMNS_CHART = "columns";
    public static final String KEY_TYPES_CHART = "types";
    public static final String KEY_NAMES_CHART = "names";

    private static byte localInit = 0;
    public static final byte CHART_TYPE_ONCE_Y = localInit++;
    public static final byte CHART_TYPE_TWO_Y = localInit++;
    public static final byte CHART_TYPE_STACKED = localInit++;
    public static final byte CHART_TYPE_DAILY = localInit++;
    public static final byte CHART_TYPE_PERCENTAGE = localInit++;

    private HashMap<String, Line> lines;
    private AxisX axisX;

    public Chart(AxisX axisX, HashMap<String, Line> lines) {
        this.axisX = axisX;
        this.lines = lines;
    }

    public Line findLine(String key) {
        return lines.get(key);
    }

    public HashMap<String, Line> getLines() {
        return lines;
    }

    public AxisX getAxisX() {
        return axisX;
    }

    public int getCountDots() {
        return axisX.getCountDots();
    }

    //        todo распознавать типы графиков
    public byte getChartType() {
        return CHART_TYPE_ONCE_Y;
    }
}
