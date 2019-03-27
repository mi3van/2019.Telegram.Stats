package com.kitzapp.telegram_stats.presentation.presenters.impl;

import android.content.Context;
import com.kitzapp.telegram_stats.presentation.presenters.base.BasePresenter;
import com.kitzapp.telegram_stats.presentation.ui.activities.base.BaseView;
import com.kitzapp.telegram_stats.presentation.ui.components.TChartView;


public interface ChartPresenter extends BasePresenter {

    interface View extends BaseView {

        void clearChartsContainer();

        void addChartToContainer(TChartView chartView);

        Context getContext();
    }

    void changeCurrentTheme();

    void runAnalyzeJson();
}
