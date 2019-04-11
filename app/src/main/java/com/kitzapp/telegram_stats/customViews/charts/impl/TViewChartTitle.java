package com.kitzapp.telegram_stats.customViews.charts.impl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import com.kitzapp.telegram_stats.R;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
import com.kitzapp.telegram_stats.customViews.simple.TColorfulTextView;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TViewChartTitle extends LinearLayout {

    private TColorfulTextView tTextView;

    public TViewChartTitle(Context context) {
        super(context);
        this.init();
    }

    public TViewChartTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TViewChartTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    public void setText(String text) {
        tTextView.setText(text);
    }

    public void init() {
        this.setOrientation(VERTICAL);

        tTextView = new TColorfulTextView(getContext());
        tTextView.setTypeface(ThemeManager.rBoldTypeface);
        tTextView.setTextSizeDP(ThemeManager.TEXT_BIG_SIZE_DP);
        tTextView.setText(getResources().getString(R.string.followers_title));

        setGravity(Gravity.CENTER_VERTICAL);
        addView(tTextView);

        int RightLeftPadding = ThemeManager.MARGIN_16DP_IN_PX;
        setPadding(RightLeftPadding, 0, RightLeftPadding, 0);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getLayoutParams().height = ThemeManager.CELL_HEIGHT_56DP_IN_PX;
    }
}
