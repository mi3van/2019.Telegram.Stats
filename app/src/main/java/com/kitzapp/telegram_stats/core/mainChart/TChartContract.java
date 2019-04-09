package com.kitzapp.telegram_stats.core.mainChart;

import android.content.Context;
import com.kitzapp.telegram_stats.clean_mvp.mvp.MvpModel;
import com.kitzapp.telegram_stats.clean_mvp.mvp.MvpPresenter;
import com.kitzapp.telegram_stats.clean_mvp.mvp.MvpView;
import com.kitzapp.telegram_stats.pojo.chart.ChartsList;

interface TChartContract {

    interface TPresenter<V extends MvpView> extends MvpPresenter<V> {
        void changeCurrentTheme();

        void runAnalyzeJson();
    }

    interface TView extends MvpView {

        void updateChartsData(ChartsList chartsList);

        Context getContext();
        void showMessageToast(String message);
        void showProgress();
        void hideProgress();
    }

    interface TModel extends MvpModel {

        ChartsList getCharts(String fileName, Context context) throws Exception;

    }
}
