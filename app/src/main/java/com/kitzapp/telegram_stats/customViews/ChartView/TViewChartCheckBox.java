package com.kitzapp.telegram_stats.customViews.ChartView;

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

public class TViewChartCheckBox extends LinearLayout {

    private TColorfulCheckBox.Listener _listener;

    public TViewChartCheckBox(Context context) {
        super(context);
    }

    public TViewChartCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TViewChartCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TViewChartCheckBox(Context context, TColorfulCheckBox.Listener listener) {
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

        int RightLeftPadding = ThemeManager.CHART_CELL_LEFT_PADDING_CH_BOX_PX;
        setPadding(RightLeftPadding - 20, 0, 0, 0);
    }

    private TDelimiterLine getDelimiterLine() {
        TDelimiterLine delimiterLine = new TDelimiterLine(getContext());
        LinearLayout.LayoutParams layoutParams = (LayoutParams) delimiterLine.getLayoutParams();
        layoutParams.setMargins(ThemeManager.CHART_CELL_RIGHTLEFT_MARGIN_PX +
                ThemeManager.CHART_CELL_RIGHTLEFT_MARGIN_PX, 0, 0, 0);
        return delimiterLine;
    }
}
