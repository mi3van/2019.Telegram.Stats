package com.kitzapp.telegram_stats.presentation.presenters.impl;

import android.annotation.SuppressLint;
import com.kitzapp.telegram_stats.Application.AndroidApp;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;
import com.kitzapp.telegram_stats.BuildConfig;
import com.kitzapp.telegram_stats.domain.executor.Executor;
import com.kitzapp.telegram_stats.domain.interactors.impl.ChartInteractor;
import com.kitzapp.telegram_stats.domain.interactors.impl.TChartInteractor;
import com.kitzapp.telegram_stats.domain.model.ChartsList;
import com.kitzapp.telegram_stats.domain.model.chart.Chart;
import com.kitzapp.telegram_stats.domain.repository.chart.TChartRepository;
import com.kitzapp.telegram_stats.domain.threading.MainThread;
import com.kitzapp.telegram_stats.presentation.presenters.base.AbstractPresenter;
import com.kitzapp.telegram_stats.presentation.ui.components.ChartView.TChartView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TChartPresenter extends AbstractPresenter implements ChartPresenter,
        ChartInteractor.Callback {
    private TChartRepository _chartRepository;
    private ChartPresenter.View _view;

    private long _dateNanoForCheck;

    public TChartPresenter(Executor executor,
                           MainThread mainThread,
                           View view) {
        super(executor, mainThread);
        _chartRepository = new TChartRepository();
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
        _view.showMessageSnackbar(message);
    }

    @Override
    public void onJsonRetrieved(ChartsList chartsList) {
        _view.hideProgress();
        this.initChartsInView(chartsList);
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
        if (BuildConfig.DEBUG) {
            _dateNanoForCheck = System.currentTimeMillis();
        }
        _view.showProgress();

        TChartInteractor interactor = new TChartInteractor(
                mExecutor,
                mMainThread,
                this,
                _chartRepository
        );

        interactor.execute();
    }

    private void initChartsInView(ChartsList chartsList) {
        _view.clearChartsContainer();
        try {
            for (Chart chart: chartsList.getCharts()) {
                TChartView chartView = createChartView(chart);
                _view.addChartToContainer(chartView);
            }
        } catch (Exception e) {
            _view.showMessageSnackbar(e.getMessage());
        } finally {
            if (BuildConfig.DEBUG) {
                _dateNanoForCheck = System.currentTimeMillis() - _dateNanoForCheck;
                String dateFormat = "mm:ss.SSS";
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
                String dateString = formatter.format(new Date(_dateNanoForCheck));
                _view.showMessageSnackbar(dateString);
            }
        }
    }

    private TChartView createChartView(Chart chart) {
        TChartView chartView = new TChartView(AndroidApp.context, chart);
        return chartView;
    }
}
