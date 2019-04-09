package com.kitzapp.telegram_stats.core.mainChart;

import android.content.Context;
import com.kitzapp.telegram_stats.clean_mvp.domain.interactors.AbstractInteractor;
import com.kitzapp.telegram_stats.clean_mvp.domain.interactors.Interactor;
import com.kitzapp.telegram_stats.common.AppConts;
import com.kitzapp.telegram_stats.pojo.chart.ChartsList;

/**
 * This is an interactor boilerplate with a reference to a model repository.
 * <p/>
 */

interface ChartInteractor extends Interactor {

    interface Callback {
        void onJsonRetrieved(ChartsList chartModel);

        void onRetrievalFailed(String error);
    }
}

class TChartLoadInteractor extends AbstractInteractor implements ChartInteractor {

    private ChartInteractor.Callback _chartCallback;
    private TChartContract.TModel _chartRepository;
    private Context _context;

    public TChartLoadInteractor(Context context,
                                Callback callback,
                                TChartContract.TModel repository) {
        _context = context;
        _chartCallback = callback;
        _chartRepository = repository;
    }

    @Override
    public void run() {
        try {
            ChartsList chartsList = _chartRepository.getCharts(AppConts.JSON_CHART_FILENAME, _context);
            if (chartsList != null) {
                this.postChartModel(chartsList);
            } else {
                throw new Exception("Read charts error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.notifyError(e.getMessage());
        }
    }

    private void notifyError(String errorMessage) {
        _mainThread.post(() -> _chartCallback.onRetrievalFailed(errorMessage));
    }

    private void postChartModel(final ChartsList chartsList) {
        _mainThread.post(() -> _chartCallback.onJsonRetrieved(chartsList));
    }
}
