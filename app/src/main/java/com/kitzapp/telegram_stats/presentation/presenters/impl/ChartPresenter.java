package com.kitzapp.telegram_stats.presentation.presenters.impl;

import com.kitzapp.telegram_stats.presentation.presenters.base.BasePresenter;
import com.kitzapp.telegram_stats.presentation.ui.base.BaseView;


public interface ChartPresenter extends BasePresenter {

    interface View extends BaseView {

        void displayJson(String json);

    }

    int getCurrentTheme();

    int changeCurrentTheme();

    void runAnalyzeJson();
}
