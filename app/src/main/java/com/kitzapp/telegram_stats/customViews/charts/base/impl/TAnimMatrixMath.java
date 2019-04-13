package com.kitzapp.telegram_stats.customViews.charts.base.impl;

import android.graphics.Matrix;

/**
 * Created by Ivan Kuzmin on 12.04.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TAnimMatrixMath {
    private final double ALLOWED_LIMIT = 0.001;

    private boolean _isAnimationEnd = false;

    private float scaleXCurrent = 1f;
    private float scaleXNeed = 1f;
    private float scaleXStep;

    public void reset() {
        scaleXCurrent = 1f;
        scaleXNeed = 1f;
    }

    public void setScaleXNeed(float scaleXNeed) {
        this.scaleXNeed = scaleXNeed;
        scaleXStep = getStepScale(scaleXCurrent, scaleXNeed);
    }

    public void makeStep(Matrix matrix) {
        this.makeScaleStep(matrix);
    }

    public void calculateWithoutAnim(Matrix matrix) {
        this.makeScaleStep(matrix);
    }

    private void makeScaleStep(Matrix matrix) {
        float tempScale = scaleXCurrent * scaleXStep;
//        if (scaleXNeed < scaleXCurrent && tempScale < scaleXCurrent - ALLOWED_LIMIT) {
//
//        } else if (scaleXNeed > scaleXCurrent && tempScale > scaleXCurrent + ALLOWED_LIMIT) {
//
//        }
        matrix.setScale(scaleXStep, 1f);
        scaleXCurrent = tempScale;
    }

    public boolean isAnimationEnd() {
        boolean isScaleAnimEnd = scaleXNeed <= scaleXCurrent + ALLOWED_LIMIT && scaleXNeed >= scaleXCurrent - ALLOWED_LIMIT;
        if (isScaleAnimEnd) {
            _isAnimationEnd = true;
        } else {
            _isAnimationEnd = false;
        }

        return _isAnimationEnd;
    }

    private float getStepScale(float scaleXCurrent, float scaleXNeed) {
        float step = scaleXNeed / scaleXCurrent;
        return step;
    }

    private float getStepTranslate(float currentValue, float needValue) {
        float stepTemp;

        float section = Math.abs(needValue - currentValue);
        if (section < 0.1f) {
            stepTemp = 0.01f;
        } else if (section < 1.2f) {
            stepTemp = 0.1f;
        } else if (section < 6) {
            stepTemp = 0.5f;
        } else if (section < 12){
            stepTemp = 1f;
        } else if (section < 120){
            stepTemp = 10f;
        } else if (section < 240){
            stepTemp = 20f;
        } else if (section < 480){
            stepTemp = 40f;
        } else if (section < 960){
            stepTemp = 80f;
        } else if (section < 960){
            stepTemp = 80f;
        } else if (section < 2000){
            stepTemp = 200f;
        } else {
            stepTemp = 400f;
        }

        return stepTemp;
    }
}
