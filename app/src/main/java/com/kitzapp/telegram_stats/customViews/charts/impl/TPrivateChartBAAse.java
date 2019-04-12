package com.kitzapp.telegram_stats.customViews.charts.impl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.common.AppConts;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
import com.kitzapp.telegram_stats.pojo.chart.Chart;
import com.kitzapp.telegram_stats.pojo.chart.impl.Line;

import java.util.HashMap;
import java.util.Map;

import static com.kitzapp.telegram_stats.common.AppConts.*;

/**
 * Created by Ivan Kuzmin on 25.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

abstract class TPrivateChartBAAse extends FrameLayout {

    protected Chart _chart = null;
    private HashMap<String, Paint> _paints = new HashMap<>();
    protected HashMap<String, Path> _linesPathes = new HashMap<>();

    protected float _viewHeight;
    protected float _viewWidth;

    protected int _maxAxisXx;
    protected long _maxAxisY;

    public TPrivateChartBAAse(Context context) {
        super(context);
        this.init();
    }

    public TPrivateChartBAAse(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TPrivateChartBAAse(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    protected void init() {
        setWillNotDraw(false);
    }

    public void loadData(Chart chart) {
        this._chart = chart;

        _paints = getNewPaints(_chart);
    }

    protected abstract int getLinePaintWidth();
    protected abstract int getChartVerticalPadding();
    protected abstract int getChartHalfVerticalPadding();
    protected abstract int getViewHeightForLayout();

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = this.getViewHeightForLayout();

        int paddingPx = this.getPaddingRightLeft();
        setPadding(paddingPx, 0, paddingPx, 0);
        this.setLayoutParams(layoutParams);
    }

    public void wasChangedActiveChart() {
        this.invalidate();
    };

    protected int getPaddingRightLeft() {
        return ThemeManager.MARGIN_16DP_IN_PX;
    }

    protected void drawPathes(Canvas canvas, HashMap<String, Path> pathHashMap, boolean withAnimation) {
        canvas.clipRect(0, 0, canvas.getWidth(), canvas.getHeight());

        boolean isActiveChart;  Path path;  Paint paint;

        for (Map.Entry<String, Path> entry : pathHashMap.entrySet()) {
            paint = _paints.get(entry.getKey());
            path = entry.getValue();
            isActiveChart = this.getChartIsActive(entry.getKey());

            if (paint == null && entry.getValue() == null) {
                continue;
            }

            if (withAnimation) {
                canvas.drawPath(path, paint);

                int newAlpha = paint.getAlpha();
                if ((isActiveChart && newAlpha == MAX_VALUE_ALPHA) || (!isActiveChart && newAlpha == MIN_VALUE_ALPHA)) {
                    continue;
                }


                if (isActiveChart) {
                    if (newAlpha + STEP_ALPHA_FOR_ANIM <= MAX_VALUE_ALPHA) {
                        newAlpha += STEP_ALPHA_FOR_ANIM;
                    } else {
                        newAlpha = MAX_VALUE_ALPHA;
                    }
                } else {
                    if (newAlpha - STEP_ALPHA_FOR_ANIM >= MIN_VALUE_ALPHA) {
                        newAlpha -= STEP_ALPHA_FOR_ANIM;
                    } else {
                        newAlpha = MIN_VALUE_ALPHA;
                    }
                }
                paint.setAlpha(newAlpha);

                postInvalidateDelayed(AppConts.DELAY_STEP_ELEMENTS_ANIM);
            } else {
                int alpha = isActiveChart? MAX_VALUE_ALPHA : MIN_VALUE_ALPHA;
                paint.setAlpha(alpha);
                canvas.drawPath(path, paint);
            }
        }
    }

    protected boolean getChartIsActive(String key) {
        boolean isActive = false;
        try {
            isActive = _chart.getLines().get(key).getIsActive();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return isActive;
    }

    private HashMap<String, Paint> getNewPaints(Chart chart) {
        HashMap<String, Paint> tempPaints = new HashMap<>();

        Line line;
        for (Map.Entry<String, Line> entry : _chart.getLines().entrySet()) {
            line = entry.getValue();
            // INIT PAINT
            Paint linePaint = AndroidUtilites.getPaint(line.getColor(), this.getLinePaintWidth());
            tempPaints.put(entry.getKey(), linePaint);
        }
        return tempPaints;
    }
}
