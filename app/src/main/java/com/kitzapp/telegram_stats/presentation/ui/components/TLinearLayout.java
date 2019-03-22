package com.kitzapp.telegram_stats.presentation.ui.components;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.kitzapp.telegram_stats.presentation.ui.Theme;

import androidx.annotation.Nullable;

/**
 * Created by Ivan Kuzmin on 2019-03-22.
 * Copyright © 2019 Example. All rights reserved.
 */

class TLinearLayout extends LinearLayout {

    public TLinearLayout(Context context) {
        super(context);
        this.init();
    }

    public TLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int color = Theme.getColor(Theme.key_totalBackColor);
        this.setBackgroundColor(color);
    }
}
