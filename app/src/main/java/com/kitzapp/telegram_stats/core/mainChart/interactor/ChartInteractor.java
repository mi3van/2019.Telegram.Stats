package com.kitzapp.telegram_stats.core.mainChart.interactor;


import com.kitzapp.telegram_stats.clean_mvp.domain.interactors.Interactor;
import com.kitzapp.telegram_stats.pojo.chart.ChartsList;


public interface ChartInteractor extends Interactor {

    interface Callback {
        void onJsonRetrieved(ChartsList chartModel);

        void onRetrievalFailed(String error);
    }
}
