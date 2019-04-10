package com.kitzapp.telegram_stats.core.mainChart;

import android.content.Context;
import android.view.View;
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
        Context getContext();

        void addViewToContainer(View View);
        void clearChartsContainer();

        void showMessageToast(String message);
        void showProgress();
        void hideProgress();
    }

    interface TModel extends MvpModel {

        ChartsList getCharts(String fileName, Context context) throws Exception;

    }
}
