package com.kitzapp.telegram_stats.presentation.presenters.impl;

import android.content.Context;

import com.kitzapp.telegram_stats.domain.executor.Executor;
import com.kitzapp.telegram_stats.domain.interactors.impl.chart.TChartInteractor;
import com.kitzapp.telegram_stats.domain.repository.chart.TChartRepository;
import com.kitzapp.telegram_stats.domain.threading.MainThread;
import com.kitzapp.telegram_stats.domain.interactors.impl.chart.ChartInteractor;
import com.kitzapp.telegram_stats.presentation.presenters.base.AbstractPresenter;

public class TChartPresenter extends AbstractPresenter implements ChartPresenter,
        ChartInteractor.Callback {

    private TChartRepository _chartRepository;
    private ChartPresenter.View mView;

    public TChartPresenter(Context context,
                           Executor executor,
                           MainThread mainThread,
                           View view) {
        super(executor, mainThread);
        _chartRepository = new TChartRepository(context);
        mView = view;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void onError(String message) {
        mView.showError(message);
    }

    @Override
    public void onJsonRetrieved(String json) {
        mView.hideProgress();
        mView.displayJson(json);
    }

    @Override
    public void onRetrievalFailed(String error) {
        mView.hideProgress();
        onError(error);
    }

    @Override
    public void runAnalyzeJson() {
        mView.showProgress();

        TChartInteractor interactor = new TChartInteractor(
                mExecutor,
                mMainThread,
                this,
                _chartRepository
        );

        interactor.execute();
    }
}
