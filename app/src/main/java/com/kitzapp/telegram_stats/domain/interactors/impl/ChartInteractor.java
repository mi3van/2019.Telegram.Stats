package com.kitzapp.telegram_stats.domain.interactors.impl;


import com.kitzapp.telegram_stats.domain.interactors.base.Interactor;


public interface ChartInteractor extends Interactor {

    interface Callback {

        void onJsonRetrieved(String json);

        void onRetrievalFailed(String error);
    }
}
