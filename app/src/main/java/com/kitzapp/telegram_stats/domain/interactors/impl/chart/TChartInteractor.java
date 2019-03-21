package com.kitzapp.telegram_stats.domain.interactors.impl.chart;

import com.kitzapp.telegram_stats.domain.executor.Executor;
import com.kitzapp.telegram_stats.domain.interactors.base.AbstractInteractor;
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
                            Callback callback, ChartRepository repository) {
        super(threadExecutor, mainThread);
        _chartCallback = callback;
        _chartRepository = repository;
    }

    @Override
    public void run() {
        String jsonForChart = _chartRepository.getJsonForChart();
        if (jsonForChart == null || jsonForChart.length() == 0) {

            this.notifyError();

        } else {

            this.postJsonString(jsonForChart);

        }
    }

    private void notifyError() {
        mMainThread.post(() -> _chartCallback.onRetrievalFailed("Nothing to welcome you with :("));
    }

    private void postJsonString(final String jsonChart) {
        mMainThread.post(() -> _chartCallback.onJsonRetrieved(jsonChart));
    }
}
