package com.kitzapp.telegram_stats.core.mainChart;

import androidx.annotation.NonNull;
import com.kitzapp.telegram_stats.BuildConfig;
import com.kitzapp.telegram_stats.clean_mvp.mvp.BasePresenter;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
import com.kitzapp.telegram_stats.core.mainChart.TChartContract.TPresenter;
import com.kitzapp.telegram_stats.pojo.chart.ChartsList;

import static com.kitzapp.telegram_stats.core.mainChart.TChartContract.TModel;
import static com.kitzapp.telegram_stats.core.mainChart.TChartContract.TView;

class TChartPresenter extends BasePresenter<TView, TModel> implements TPresenter<TView> {

    private long _dateMillisForCheck;

    public TChartPresenter(@NonNull TChartModel model) {
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
                        view.updateChartsData(chartsList);
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

//    private void initChartsInView(ChartsList chartsList) {
//        if (view == null) {
//            return;
//        }
//        view.clearChartsContainer();
//        try {
//            for (Chart chart: chartsList.getCharts()) {
//                TFullCellView chartView = createChartView(chart);
//                if (chartView != null) {
//                    view.addChartToContainer(chartView);
//                }
//            }
//        } catch (Exception e) {
//            view.showMessageToast(e.getMessage());
//        } finally {
//            if (BuildConfig.DEBUG) {
//                _dateMillisForCheck = System.currentTimeMillis() - _dateMillisForCheck;
//                String dateFormat = "mm:ss.SSS";
//                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
//                String dateString = formatter.format(new Date(_dateMillisForCheck));
//                view.showMessageToast(dateString);
//            }
//        }
//    }

    @Override
    public void viewIsReady() {

    }
}