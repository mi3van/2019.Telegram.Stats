package com.kitzapp.telegram_stats.presentation.presenters.impl;

import com.kitzapp.telegram_stats.presentation.presenters.base.BasePresenter;
import com.kitzapp.telegram_stats.presentation.ui.BaseView;


public interface ChartPresenter extends BasePresenter {

    interface View extends BaseView {

        void displayJson(String json);

    }

    void runAnalyzeJson();
}
