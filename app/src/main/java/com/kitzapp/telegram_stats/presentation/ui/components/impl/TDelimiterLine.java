package com.kitzapp.telegram_stats.presentation.ui.components.impl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;

/**
 * Created by Ivan Kuzmin on 25.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TDelimiterLine extends View {
    public TDelimiterLine(Context context) {
        super(context);
        this.init();
    }

    public TDelimiterLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TDelimiterLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,1);
        layoutParams.setMargins(ThemeManager.CHART_CELL_RIGHTLEFT_MARGIN_PX +
                ThemeManager.CHART_CELL_RIGHTLEFT_MARGIN_PX +
                ThemeManager.CHART_CELL_RIGHTLEFT_MARGIN_PX, 0, 0, 0);
        setLayoutParams(layoutParams);
    }
}
