package com.kitzapp.telegram_stats.presentation.ui.components.simple;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;
import com.kitzapp.telegram_stats.common.AndroidUtilites;

/**
 * Created by Ivan Kuzmin on 29.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TChartCircle extends View {

    Paint _paint;

    public TChartCircle(Context context) {
        super(context);
    }

    public TChartCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TChartCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TChartCircle(Context context, int color) {
        super(context);
        this._paint = AndroidUtilites.getPaint(color, ThemeManager.CHART_LINE_IN_PART_WIDTH_PX);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (_paint != null) {
            int center = ThemeManager.CHART_CIRCLE_SIZE_PX >> 1;
            int width = center - ThemeManager.CHART_LINE_FULL_WIDTH_PX;
            canvas.drawCircle(center, center, width, _paint);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getLayoutParams().height = ThemeManager.CHART_CIRCLE_SIZE_PX;
        getLayoutParams().width = ThemeManager.CHART_CIRCLE_SIZE_PX;
    }
}
