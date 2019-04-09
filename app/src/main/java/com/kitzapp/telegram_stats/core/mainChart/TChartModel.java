package com.kitzapp.telegram_stats.core.mainChart;

import android.content.Context;
import android.graphics.Color;
import com.kitzapp.telegram_stats.core.appManagers.JsonManager;
import com.kitzapp.telegram_stats.pojo.chart.Chart;
import com.kitzapp.telegram_stats.pojo.chart.ChartsList;
import com.kitzapp.telegram_stats.pojo.chart.impl.AxisX;
import com.kitzapp.telegram_stats.pojo.chart.impl.Line;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.kitzapp.telegram_stats.pojo.chart.Chart.*;

/**
 * Created by Ivan Kuzmin on 2019-03-20.
 * Copyright © 2019 Example. All rights reserved.
 */

class TChartModel implements TChartContract.TModel {

    @Override
    public ChartsList getCharts(String fileName, Context context) throws Exception {
        String chartJsonInString = JsonManager.getJsonStringFromFile(fileName, context);
        ChartsList chartsList = new ChartsList();
        if (chartJsonInString != null) {
            try {
                JSONArray chartsJsonArray = new JSONArray(chartJsonInString);

                for (int i = 0; i < chartsJsonArray.length(); i++) {
                    Chart chart = getChart((JSONObject) chartsJsonArray.get(i));
                    chartsList.addChart(chart);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                throw new Exception("Working with JSON exception");
            }
        } else {
            throw new Exception("Read JSON file exception");
        }
        return chartsList;
    }

    private Chart getChart(JSONObject chartJson) throws Exception {
        HashMap<String, Line> linesMap = new HashMap<>();
        AxisX axisX = null;
        try {
            JSONArray columnsBaseJson = chartJson.getJSONArray(KEY_COLUMNS_CHART);
            JSONObject typesJson = chartJson.getJSONObject(KEY_TYPES_CHART);
            JSONObject namesJson = chartJson.getJSONObject(KEY_NAMES_CHART);
            JSONObject colorsJson = chartJson.getJSONObject(KEY_COLORS_CHART);

            for (int i = 0; i < columnsBaseJson.length(); i++) {
                JSONArray columnsWithKey = new JSONArray(columnsBaseJson.get(i).toString());
                String key = columnsWithKey.get(0).toString();

                String type = typesJson.getString(key);
                byte currentType = this.getTypeInByte(type);
                switch (currentType) {
                    case Line.TYPE_LINE:
                        long[] dots = new long[columnsWithKey.length() - 1];
                        for (int j = 1; j < columnsWithKey.length(); j++) {
                            dots[j - 1] = (columnsWithKey.getInt(j));
                        }
                        String name = namesJson.getString(key);
                        String hexColor = colorsJson.getString(key);
                        int normalColor = Color.parseColor(hexColor);
                        Line line = new Line(key, dots, currentType, name, normalColor);
                        linesMap.put(key, line);
                        break;

                    case Line.TYPE_X_AXIS:
                        long[] dates = new long[columnsWithKey.length() - 1];
                        for (int j = 1; j < columnsWithKey.length(); j++) {
                            dates[j - 1] = columnsWithKey.getLong(j);
                        }
                        axisX = new AxisX(key, dates, currentType);
                        break;
                }
            }

            if (axisX == null) {
                throw new Exception("Axis X is null exception");
            }
            Chart chart = new Chart(axisX, linesMap);
            return chart;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new Exception("Working with JSON exception");
        }
    }

    private byte getTypeInByte(String type) throws Exception {
        if (type.equals("x")) {
            return Line.TYPE_X_AXIS;
        } else if (type.equals("line")) {
            return Line.TYPE_LINE;
        } else {
            throw new Exception("Type for object in Chart not found");
        }
    }
}
