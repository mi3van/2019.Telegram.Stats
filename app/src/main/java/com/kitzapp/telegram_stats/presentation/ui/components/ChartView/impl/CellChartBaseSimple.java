package com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class CellChartBaseSimple extends LinearLayout {
    public CellChartBaseSimple(Context context) {
        super(context);
        this.init();
    }

    public CellChartBaseSimple(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public CellChartBaseSimple(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    protected void init() {
        this.setOrientation(VERTICAL);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getLayoutParams().height = ThemeManager.CHART_CELL_HEIGHT_PX;
    }
}
