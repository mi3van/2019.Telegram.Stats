package com.kitzapp.telegram_stats.domain.interactors.impl;


import com.kitzapp.telegram_stats.domain.interactors.base.Interactor;
import com.kitzapp.telegram_stats.domain.model.ChartsList;


public interface ChartInteractor extends Interactor {

    interface Callback {
        void onJsonRetrieved(ChartsList chartModel);

        void onRetrievalFailed(String error);
    }
}
