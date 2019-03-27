package com.kitzapp.telegram_stats.presentation.ui.components.simple;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;
import androidx.annotation.Nullable;

/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TTextView extends TextView {

    public TTextView(Context context) {
        super(context);
    }

    public TTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTextSizeDP(float size) {
        super.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
    }
}
