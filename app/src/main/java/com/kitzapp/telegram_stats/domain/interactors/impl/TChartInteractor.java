package com.kitzapp.telegram_stats.domain.interactors.impl;

import com.kitzapp.telegram_stats.common.AppConts;
import com.kitzapp.telegram_stats.domain.executor.Executor;
import com.kitzapp.telegram_stats.domain.interactors.base.AbstractInteractor;
import com.kitzapp.telegram_stats.domain.model.ChartsList;
import com.kitzapp.telegram_stats.domain.model.chart.Chart;
import com.kitzapp.telegram_stats.domain.repository.chart.ChartRepository;
import com.kitzapp.telegram_stats.domain.threading.MainThread;

/**
 * This is an interactor boilerplate with a reference to a model repository.
 * <p/>
 */
public class TChartInteractor extends AbstractInteractor implements ChartInteractor {

    private ChartInteractor.Callback _chartCallback;
    private ChartRepository _chartRepository;

    public TChartInteractor(
                            Executor threadExecutor,
                            MainThread mainThread,
                            Callback callback,
                            ChartRepository repository) {
        super(threadExecutor, mainThread);
        _chartCallback = callback;
        _chartRepository = repository;
    }

    @Override
    public void run() {
        try {
            ChartsList chartsList = _chartRepository.getCharts(AppConts.JSON_CHART_FILENAME);
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
