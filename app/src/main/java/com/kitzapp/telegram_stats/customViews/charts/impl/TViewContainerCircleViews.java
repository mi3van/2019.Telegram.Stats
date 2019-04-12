package com.kitzapp.telegram_stats.customViews.charts.impl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
import com.kitzapp.telegram_stats.customViews.simple.TColorfulChartCircle;

import java.util.HashMap;
import java.util.Map;


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

    private final float NOT_ALLOW_INDEX = Float.MIN_VALUE;

    private HashMap<String, TColorfulChartCircle> _circleViews = new HashMap<>();
    private int _halfSize;
    private float _stepX;

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

    void setNewPositionAndAnimate(String viewKey, float x, float y, boolean isVisible) {
        if (_circleViews.isEmpty()) {
            return;
        }
        View circleView = _circleViews.get(viewKey);
        if (circleView == null) {
            return;
        }

        float oldX = circleView.getX();
        float oldY = circleView.getY();

        float currentX = (x == NOT_ALLOW_INDEX)? oldX: x - _halfSize;
        float currentY = (y == NOT_ALLOW_INDEX)? oldY: y - _halfSize;

        float alpha;
        if (isVisible) {
            if (!circleView.isShown()) {
                circleView.setVisibility(VISIBLE);
                circleView.setAlpha(0f);
            }
            alpha = 1f;
        } else {
            alpha = 0f;
        }

//        if (animationType == ANIMATION_TYPE_NANI) {
            circleView.setAlpha(alpha);
            circleView.setX(currentX);
            circleView.setY(currentY);
//        } else {
//            if (x == oldX && y == oldY) {
//                return;
//            }
//            ViewPropertyAnimator animate = circleView.animate();
//            if (animationType == ANIMATION_TYPE_SCALE) {
//                animate.alpha(alpha).
//                        scaleY(alpha).
//                        scaleX(alpha).
//                        setDuration(DELAY_ELEMENTS_ANIM_MIDDLE);
//                circleView.setX(currentX);
//                circleView.setY(currentY);
//            } else if (animationType == ANIMATION_TYPE_TRANSLATION) {
//                animate.x(currentX).
//                        y(currentY).
//                        setDuration(DELAY_ELEMENTS_ANIM_MIDDLE);
//            }
//            animate.setInterpolator(new AccelerateDecelerateInterpolator()).
//                    start();
//        }
    }

    void hideAllViewsWithoutAnimation() {
        if (_circleViews.isEmpty()) {
            return;
        }
        for (Map.Entry<String, TColorfulChartCircle> entry: _circleViews.entrySet()) {
            View view = entry.getValue();
            if (view == null) {
                return;
            }
            view.setAlpha(0f);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
        layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
    }

    @Override
    public void removeAllViews() {
        _circleViews = new HashMap<>();
        super.removeAllViews();
    }

}
