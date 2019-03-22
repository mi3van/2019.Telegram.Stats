package com.kitzapp.telegram_stats.presentation.ui.components;

import android.graphics.Paint;
import android.text.TextPaint;

/**
 * Created by Ivan Kuzmin on 2019-03-22.
 * Copyright © 2019 Example. All rights reserved.
 */

public class RFontTextPaint extends TextPaint {
    public RFontTextPaint() {
        super(Paint.ANTI_ALIAS_FLAG);
        this.setTypeface(AndroidUtilites.getTypeface("fonts/rregular.ttf"));
        setColor(0xffffffff);
    }
}
