package com.kitzapp.telegram_stats.presentation.ui.base;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Ivan Kuzmin on 2019-03-21.
 * Copyright © 2019 Example. All rights reserved.
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.initVariables();
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        this.initViews();
    }

    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
            theme.applyStyle(getCurrentTheme(), true);
        return theme;
    }

    protected abstract void initVariables();

    protected abstract int getLayoutID();

    protected abstract void initViews();

    protected abstract int getCurrentTheme();
}
