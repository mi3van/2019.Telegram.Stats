package com.kitzapp.telegram_stats.core.mainChart;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.LinearLayout;
import com.kitzapp.telegram_stats.BuildConfig;
import com.kitzapp.telegram_stats.clean_mvp.mvp.BasePresenter;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
import com.kitzapp.telegram_stats.core.mainChart.TChartContract.TPresenter;
import com.kitzapp.telegram_stats.customViews.charts.TChartCardCell;
import com.kitzapp.telegram_stats.pojo.chart.Chart;
import com.kitzapp.telegram_stats.pojo.chart.ChartsList;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.kitzapp.telegram_stats.core.mainChart.TChartContract.TModel;
import static com.kitzapp.telegram_stats.core.mainChart.TChartContract.TView;

class TChartPresenter extends BasePresenter<TView, TModel> implements TPresenter<TView> {

    private long _dateMillisForCheck;

    public TChartPresenter( TChartModel model) {
        super(model);
    }

//    @Override
    public void runAnalyzeJson() {
        if (BuildConfig.DEBUG) {
            _dateMillisForCheck = System.currentTimeMillis();
        }
//        view.showProgress();
        if (view == null) {
            return;
        }
        TChartLoadInteractor interactor = new TChartLoadInteractor(view.getContext(),
                new ChartInteractor.Callback() {
                    @Override
                    public void onJsonRetrieved(ChartsList chartsList) {
                //        view.hideProgress();
                        if (view == null) {
                            return;
                        }
                        initChartsInView(chartsList);
                    }

                    @Override
                    public void onRetrievalFailed(String error) {
                //        view.hideProgress();
                        onError(error);
                    }
                },
                model
        );
        interactor.execute();
    }

//    @Override
    public void changeCurrentTheme() {
        ThemeManager.changeThemeAndSave();
    }

    @Override
    public void onError(String message) {
        if (isViewAttached()) {
            view.showMessageToast(message);
        }
    }

    private void initChartsInView(ChartsList chartsList) {
        if (view == null) {
            return;
        }
        view.clearChartsContainer();
        View firstChartsDelimiter = new View(view.getContext());
        firstChartsDelimiter.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                ThemeManager.CHART_CELL_BOTTOM_MARGIN_PX));
        view.addViewToContainer(firstChartsDelimiter);
        try {
            for (Chart chart: chartsList.getCharts()) {
                TChartCardCell chartCardCell = createChartView(chart);
                view.addViewToContainer(chartCardCell);
            }
        } catch (Exception e) {
            view.showMessageToast(e.getMessage());
        } finally {
            if (BuildConfig.DEBUG) {
                _dateMillisForCheck = System.currentTimeMillis() - _dateMillisForCheck;
                String dateFormat = "mm:ss.SSS";
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
                String dateString = formatter.format(new Date(_dateMillisForCheck));
                view.showMessageToast(dateString);
            }
        }
    }

    private TChartCardCell createChartView(Chart chart) throws Exception {
        TChartCardCell chartCardCell = new TChartCardCell(view.getContext());
        chartCardCell.loadData(chart);
        return chartCardCell;
    }

    @Override
    public void viewIsReady() {

    }
}
