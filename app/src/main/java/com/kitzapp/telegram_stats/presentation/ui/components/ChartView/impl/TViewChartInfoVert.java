package com.kitzapp.telegram_stats.presentation.ui.components.ChartView.impl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.presentation.ui.components.simple.TChartTextView;
import com.kitzapp.telegram_stats.presentation.ui.components.simple.TDelimiterLine;

/**
 * Created by Ivan Kuzmin on 28.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

class TViewChartInfoVert extends LinearLayout {
    private final int COUNT_LINES = 6;

    private TChartTextView _tTextView1;
    private TChartTextView _tTextView2;
    private TChartTextView _tTextView3;
    private TChartTextView _tTextView4;
    private TChartTextView _tTextView5;
    private TChartTextView _tTextView6;

    private long[] _followers = null;
    private long _maxY = 0;
    private long _minY = 0;

    public TViewChartInfoVert(Context context) {
        super(context);
        this.init();
    }

    public TViewChartInfoVert(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TViewChartInfoVert(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        setWillNotDraw(false);
        this.setOrientation(VERTICAL);
        _followers = new long[COUNT_LINES];

        _tTextView1 = this.getNewTextView(true);
        _tTextView2 = this.getNewTextView();
        _tTextView3 = this.getNewTextView();
        _tTextView4 = this.getNewTextView();
        _tTextView5 = this.getNewTextView();
        _tTextView6 = this.getNewTextView();

        this.addView(_tTextView1);
        this.addView(this.getDelimiterLine());
        this.addView(_tTextView2);
        this.addView(this.getDelimiterLine());
        this.addView(_tTextView3);
        this.addView(this.getDelimiterLine());
        this.addView(_tTextView4);
        this.addView(this.getDelimiterLine());
        this.addView(_tTextView5);
        this.addView(this.getDelimiterLine());
        this.addView(_tTextView6);
        this.addView(this.getDelimiterLine(true));
    }

    public void setDatesAndInit(long maxY, long minY) {
        if (maxY == _maxY && minY == _minY) {
            return;
        }
        _maxY = maxY;
        _minY = minY;

        double calculating = _maxY >> 6;
        double topMargin = (long) (calculating + calculating + calculating * 0.5);
        _maxY -= topMargin;

        TChartTextView currentTextView;

        double tempValue = ((double)(_maxY - _minY) / (COUNT_LINES - 1));
        double tempY = _minY;

        for (int i = 0; i < COUNT_LINES; i++) {
            _followers[i] = (long) tempY;
            currentTextView = getCurrentTextView(i);
            currentTextView.setText(this.convertLongToStr(_followers[i]));

            tempY += tempValue;
        }
    }

    @SuppressLint("DefaultLocale")
    private String convertLongToStr(long follower) {
        String folowerStr; String postfix;
        float tempValue;
        if (follower >= 10000000) {//10M
            tempValue = (float) follower / 1000000; postfix = "M";
            folowerStr = String.format("%.0f%s", tempValue, postfix);
        } else {
            float tempFloatFolower = (float) follower;
            if (follower >= 1000000) {//1.5M
                tempValue = tempFloatFolower / 1000000;
                postfix = "M";
                folowerStr = String.format("%.1f%s", tempValue, postfix);
            } else if (follower >= 10000) {//10k
                tempValue = tempFloatFolower / 1000;
                postfix = "K";
                folowerStr = String.format("%.0f%s", tempValue, postfix);
            } else if (follower >= 1000) {//1.5k
                tempValue = tempFloatFolower / 1000;
                postfix = "K";
                folowerStr = String.format("%.1f%s", tempValue, postfix);
            } else {
                folowerStr = String.valueOf(follower);
            }
        }

        return folowerStr;
    }

    private TChartTextView getCurrentTextView(int index) {
        TChartTextView tChartTextView = null;
        switch (index) {
            case 5:
                tChartTextView = _tTextView1;
                break;
            case 4:
                tChartTextView = _tTextView2;
                break;
            case 3:
                tChartTextView = _tTextView3;
                break;
            case 2:
                tChartTextView = _tTextView4;
                break;
            case 1:
                tChartTextView = _tTextView5;
                break;
            case 0:
                tChartTextView = _tTextView6;
                break;
        }
        return tChartTextView;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getLayoutParams().height = LayoutParams.MATCH_PARENT;
        getLayoutParams().height = LayoutParams.MATCH_PARENT;
    }

    private TChartTextView getNewTextView() {
        return this.getNewTextView(false);
    }

    private TChartTextView getNewTextView(boolean isFirst) {
        TChartTextView tChartTextView = new TChartTextView(getContext());
        tChartTextView.setGravity(Gravity.BOTTOM | Gravity.START);
        if (!isFirst) {
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f);
            tChartTextView.setLayoutParams(layoutParams);
        }
        return tChartTextView;
    }

    private TDelimiterLine getDelimiterLine() {
        return this.getDelimiterLine(false);
    }

    private TDelimiterLine getDelimiterLine(boolean isLast) {
        TDelimiterLine delimiterLine = new TDelimiterLine(getContext());
        LinearLayout.LayoutParams layoutParams = (LayoutParams) delimiterLine.getLayoutParams();
        float alpha;
        if (isLast) {
            alpha = 1f;
        } else {
            layoutParams.height = 1;
            alpha = 1f;
        }
        delimiterLine.setAlpha(alpha);
        layoutParams.setMargins(0, 0, 0, 0);
        return delimiterLine;
    }
}
