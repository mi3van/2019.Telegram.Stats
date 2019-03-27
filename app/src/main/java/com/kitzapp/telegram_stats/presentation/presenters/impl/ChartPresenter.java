package com.kitzapp.telegram_stats.presentation.presenters.impl;

import com.kitzapp.telegram_stats.presentation.presenters.base.BasePresenter;
import com.kitzapp.telegram_stats.presentation.ui.activities.base.BaseView;
import com.kitzapp.telegram_stats.presentation.ui.components.TChartView;


public interface ChartPresenter extends BasePresenter {

    interface View extends BaseView {

        void clearChartsContainer();

        void addChartToContainer(TChartView chartView);
    }

    void changeCurrentTheme();

    void runAnalyzeJson();
}
