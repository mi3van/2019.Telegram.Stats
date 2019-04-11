package com.kitzapp.telegram_stats.customViews.charts.impl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
import com.kitzapp.telegram_stats.customViews.simple.TColorfulChartCircle;

import java.util.HashMap;
import java.util.Map;

import static com.kitzapp.telegram_stats.common.AppConts.DELAY_ELEMENTS_ANIM;

/**
 * Created by Ivan Kuzmin on 29.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TViewContainerCircleViews extends FrameLayout {
    private static byte localInit = 0;
    public static final byte ANIMATION_TYPE_NANI = localInit++;
    public static final byte ANIMATION_TYPE_SCALE = localInit++;
    public static final byte ANIMATION_TYPE_TRANSLATION = localInit++;

    private final float NOT_ALLOW_INDEX = -20f;

    private HashMap<String, View> _circleViews = new HashMap<>();
    private int _halfSize;
    private int _oldXIndex = 0;

    public TViewContainerCircleViews(Context context) {
        super(context);
        this.init();
    }

    public TViewContainerCircleViews(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TViewContainerCircleViews(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        setWillNotDraw(false);
        _halfSize = ThemeManager.CHART_CIRCLE_HALF_SIZE_PX;
    }

    void initNewCircle(int color, String viewKey) {
        TColorfulChartCircle tChartCircle = new TColorfulChartCircle(getContext(), color);
        tChartCircle.setVisibility(INVISIBLE);
        _circleViews.put(viewKey, tChartCircle);
        this.addView(tChartCircle);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
        layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
    }

    void hideOrShowViewWithTag(String viewKey, boolean isActivate) {
        View circleView = this.getView(viewKey);
        if (circleView != null) {
            float alphaAndScale = isActivate? 1f: 0f;
            if (circleView.getAlpha() != alphaAndScale) {
                circleView.animate().alpha(alphaAndScale).scaleX(alphaAndScale).scaleY(alphaAndScale).setDuration(DELAY_ELEMENTS_ANIM).start();
            }
        }
    }

    private void setNewXAndAnimate(String viewKey, float x, boolean isActive) {
        this.setNewPositionAndAnimate(x, NOT_ALLOW_INDEX, viewKey, isActive);
    }

    private void setNewYAndAnimate(String viewKey, float y, boolean isActive) {
        this.setNewPositionAndAnimate(NOT_ALLOW_INDEX, y, viewKey, isActive);
    }

    void setNewPositionAndAnimate(float x, float y, String viewKey, boolean isActive) {
        View circleView = getView(viewKey);
        if (circleView == null) {
            return;
        }

        this.hideViewWithCallback(viewKey, () -> {
            if (isActive) {
                if (x != NOT_ALLOW_INDEX) {
                    float x1 = x - _halfSize;
                    circleView.setX(x1);
                }
                if (y != NOT_ALLOW_INDEX) {
                    float y1 = y - _halfSize;
                    circleView.setY(y1);
                }

                this.hideOrShowViewWithTag(viewKey, isActive);
            }
        });
    }

    private void hideViewWithCallback(String viewKey, Runnable callback) {
        View circleView = this.getView(viewKey);
        if (circleView != null) {
            float alpha = 0f;
            circleView.animate().
                    alpha(alpha).
                    scaleX(alpha).
                    scaleY(alpha).
                    setDuration(DELAY_ELEMENTS_ANIM).
                    withEndAction(callback).start();
        }
    }

    private View getView(String key) {
        if (_circleViews.isEmpty()) {
            return null;
        }
        return _circleViews.get(key);
    }

    @Override
    public void removeAllViews() {
        _circleViews = new HashMap<>();
        super.removeAllViews();
    }

    void hideAllViewsWithoutAnimation() {
        if (_circleViews.isEmpty()) {
            return;
        }
        for (Map.Entry<String, View> entry: _circleViews.entrySet()) {
            View circleView = getView(entry.getKey());
            if (circleView == null) {
                return;
            }
            circleView.setAlpha(0f);
        }
    }
}
