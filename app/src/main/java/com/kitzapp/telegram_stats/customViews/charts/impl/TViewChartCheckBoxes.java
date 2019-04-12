package com.kitzapp.telegram_stats.customViews.charts.impl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
import com.kitzapp.telegram_stats.customViews.simple.TColorfulCheckBox;
import com.kitzapp.telegram_stats.customViews.simple.TDelimiterLine;
import com.kitzapp.telegram_stats.pojo.chart.Chart;
import com.kitzapp.telegram_stats.pojo.chart.impl.Line;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ivan Kuzmin on 25.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TViewChartCheckBoxes extends LinearLayout {

    private TColorfulCheckBox.Listener _listener;

    public TViewChartCheckBoxes(Context context) {
        super(context);
    }

    public TViewChartCheckBoxes(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TViewChartCheckBoxes(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TViewChartCheckBoxes(Context context, TColorfulCheckBox.Listener listener) {
        super(context);
        this.init();

         _listener = listener;
    }

    public void loadData(Chart chart) {
        int count = 0;
        HashMap<String, Line> lines = chart.getLines();
        for (Map.Entry<String, Line> entry: lines.entrySet()) {
            count++;
            Line line = entry.getValue();
            TColorfulCheckBox checkBox = new TColorfulCheckBox(getContext(),
                    line.getKey(),
                    line.getName(),
                    line.getColor(),
                    _listener);
            this.addView(checkBox);

            if (count < lines.size()) {
                TDelimiterLine delimiterLine = this.getDelimiterLine();
                this.addView(delimiterLine);
            }
        }
    }

    private void init() {
        this.setOrientation(VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);

        setPadding(ThemeManager.MARGIN_16DP_IN_PX, 0, ThemeManager.MARGIN_16DP_IN_PX, 0);
    }

    private TDelimiterLine getDelimiterLine() {
        TDelimiterLine delimiterLine = new TDelimiterLine(getContext());
        LinearLayout.LayoutParams layoutParams = (LayoutParams) delimiterLine.getLayoutParams();
        layoutParams.setMargins(ThemeManager.MARGIN_32DP_IN_PX, 0, 0, 0);
        return delimiterLine;
    }
}
