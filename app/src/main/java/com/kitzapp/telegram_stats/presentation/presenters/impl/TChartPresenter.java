package com.kitzapp.telegram_stats.presentation.presenters.impl;

import android.content.Context;

import com.kitzapp.telegram_stats.domain.executor.Executor;
import com.kitzapp.telegram_stats.domain.interactors.impl.ChartInteractor;
import com.kitzapp.telegram_stats.domain.interactors.impl.TChartInteractor;
import com.kitzapp.telegram_stats.domain.repository.chart.TChartRepository;
import com.kitzapp.telegram_stats.domain.threading.MainThread;
import com.kitzapp.telegram_stats.presentation.presenters.base.AbstractPresenter;
import com.kitzapp.telegram_stats.presentation.ui.ThemeManager;

public class TChartPresenter extends AbstractPresenter implements ChartPresenter,
        ChartInteractor.Callback {

    private TChartRepository _chartRepository;
    private ChartPresenter.View _view;

    public TChartPresenter(Context context,
                           Executor executor,
                           MainThread mainThread,
                           View view) {
        super(executor, mainThread);
        _chartRepository = new TChartRepository(context);
        _view = view;
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
        _view.showError(message);
    }

    @Override
    public void onJsonRetrieved(String json) {
        _view.hideProgress();
        _view.displayJson(json);
    }

    @Override
    public void onRetrievalFailed(String error) {
        _view.hideProgress();
        onError(error);
    }

    @Override
    public void changeCurrentTheme() {
        ThemeManager.changeThemeAndSave();
    }

    @Override
    public void runAnalyzeJson() {
        _view.showProgress();

        TChartInteractor interactor = new TChartInteractor(
                mExecutor,
                mMainThread,
                this,
                _chartRepository
        );

        interactor.execute();
    }
}
