package com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.domain.model.chart.Chart;
import com.kitzapp.telegram_stats.domain.model.chart.impl.Line;
import com.kitzapp.telegram_stats.presentation.ui.components.impl.TCheckBox;
import com.kitzapp.telegram_stats.presentation.ui.components.impl.TDelimiterLine;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ivan Kuzmin on 25.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class ViewChBoxChartIsActive extends LinearLayout {

    public ViewChBoxChartIsActive(Context context) {
        super(context);
    }

    public ViewChBoxChartIsActive(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewChBoxChartIsActive(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewChBoxChartIsActive(Context context, Chart chart, TCheckBox.Listener listener) {
        super(context);
        this.init();

        int count = 0;
        HashMap<String, Line> lines = chart.getLines();
        for (Map.Entry<String, Line> entry: lines.entrySet()) {
            count++;
            Line line = entry.getValue();
            TCheckBox checkBox = new TCheckBox(getContext(),
                    line.getKey(),
                    line.getName(),
                    line.getColor(),
                    listener);
            this.addView(checkBox);

            if (count < lines.size()) {
                TDelimiterLine delimiterLine = new TDelimiterLine(getContext());
                this.addView(delimiterLine);
            }
        }
    }

    private void init() {
        this.setOrientation(VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);
    }
}
