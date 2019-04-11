package com.kitzapp.telegram_stats.customViews.simple;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;


/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TTextView extends TextView {

    public TTextView(Context context) {
        super(context);
        this.init();
    }

    public TTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    public void setTextSizeDP(float size) {
        super.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
    }

    protected void init() {
        this.setTypeface(ThemeManager.rRegularTypeface);
    }
}
