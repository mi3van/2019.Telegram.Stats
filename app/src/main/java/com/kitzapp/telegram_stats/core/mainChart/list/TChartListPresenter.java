package com.kitzapp.telegram_stats.core.mainChart.list;

import com.kitzapp.telegram_stats.clean_mvp.mvp.BasePresenter;
import com.kitzapp.telegram_stats.pojo.chart.Chart;

/**
 * Created by Ivan Kuzmin on 09.04.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

class TChartListPresenter extends BasePresenter<TChartAdapter, TChartListModel>
        implements TChartListContract.TPresenter<TChartAdapter> {

    public TChartListPresenter( TChartListModel model) {
        super(model);
    }

    @Override
    public void viewIsReady() {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public int getItemCount() {
        return model.getItemCount();
    }

    @Override
    public Chart getChartOnPosition(int position) throws Exception {
        return model.getChartOnPosition(position);
    }
}
