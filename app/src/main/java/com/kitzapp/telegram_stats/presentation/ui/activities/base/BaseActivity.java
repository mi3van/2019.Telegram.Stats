package com.kitzapp.telegram_stats.presentation.ui.activities.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.kitzapp.telegram_stats.Application.AndroidApp;

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

        if (getApplicationContext() instanceof AndroidApp) {
            ((AndroidApp) getApplicationContext()).setCurrentActivity(this);
        }
    }

    protected abstract void initVariables();

    protected abstract int getLayoutID();

    protected abstract void initViews();

    protected void onDestroy() {
        this.clearReferences();
        super.onDestroy();
    }

    private void clearReferences(){
        if (getApplicationContext() instanceof AndroidApp) {
            ((AndroidApp) getApplicationContext()).setCurrentActivity(null);
        }
    }
}
