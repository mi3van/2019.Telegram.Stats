package com.kitzapp.telegram_stats.core.mainChart.list;

import androidx.annotation.NonNull;
import com.kitzapp.telegram_stats.clean_mvp.mvp.MvpAdapter.base.BasePresenterForAdapter;
import com.kitzapp.telegram_stats.pojo.chart.Chart;

/**
 * Created by Ivan Kuzmin on 09.04.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

class TChartListPresenter extends BasePresenterForAdapter<TChartAdapter, TChartViewHolder, TChartListModel>
        implements TChartListContract.TPresenter<TChartAdapter, TChartViewHolder> {

    public TChartListPresenter(@NonNull TChartListModel model) {
        super(model);
    }

    @Override
    public void bindViewHolderOnPosition(@NonNull TChartViewHolder viewHolder, int position) throws Exception {
        Chart chart = model.getChartOnPosition(position);

        viewHolder.tFullCellView.loadData(chart);
    }

    @Override
    public void viewIsReady() {

    }

    @Override
    public void onError(String message) {

    }
}
