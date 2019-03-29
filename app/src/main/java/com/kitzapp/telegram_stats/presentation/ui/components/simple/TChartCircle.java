package com.kitzapp.telegram_stats.presentation.ui.components.simple;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;
import com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl.ViewChartPart;

/**
 * Created by Ivan Kuzmin on 29.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TChartCircle extends View implements ViewChartPart.OnChartPopupListener {

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

    public TChartCircle(Context context, Paint _paint) {
        super(context);
        this._paint = _paint;
        getLayoutParams().height = ThemeManager.CHART_CIRCLE_SIZE_PX;
        getLayoutParams().width = ThemeManager.CHART_CIRCLE_SIZE_PX;
    }

    public ViewChartPart.OnChartPopupListener getDelegate() {
        return this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (_paint != null) {
            int center = ThemeManager.CHART_CIRCLE_SIZE_PX >> 1;
            canvas.drawCircle(center, center, center >> 2, _paint);
        }
    }

    @Override
    public void hideViews() {
        ((ViewGroup)getParent()).removeView(this);
    }
}
