package com.kitzapp.telegram_stats.presentation.ui.components.impl;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.CheckBox;
import androidx.core.widget.CompoundButtonCompat;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;

/**
 * Created by Ivan Kuzmin on 25.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TCheckBox extends CheckBox {

    public interface Listener {
        void onBoxWasChecked(String key, boolean isChecked);
    }

    private String key;
    private String name;
    private int color;
    private Listener listener;

    public TCheckBox(Context context) {
        super(context);
    }

    public TCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TCheckBox(Context context, String key, String name, int color, Listener listener) {
        super(context);
        this.key = key;
        this.name = name;
        this.color = color;
        this.listener = listener;

        this.init();
    }

    private void init() {
        this.setChecked(true);
        this.setGravity(Gravity.CENTER_VERTICAL);

        this.setText(name);

        if (Build.VERSION.SDK_INT < 21) {
            CompoundButtonCompat.setButtonTintList(this, ColorStateList.valueOf(color));
        } else {
            this.setButtonTintList(ColorStateList.valueOf(color));
        }

        setOnCheckedChangeListener((buttonView, isChecked) -> listener.onBoxWasChecked(key, isChecked));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getLayoutParams().height = ThemeManager.CHART_CELL_HEIGHT_PX;
    }
}
